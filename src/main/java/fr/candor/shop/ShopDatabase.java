package fr.candor.shop;

import fr.candor.shop.database.SqlDatabase;
import fr.candor.shop.player.PlayerData;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ShopDatabase extends SqlDatabase {

    private final BlockingQueue<PlayerData> loadQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<PlayerData> saveQueue = new LinkedBlockingQueue<>();
    private final ShopPlugin plugin;

    public ShopDatabase(ShopPlugin plugin) {
        super(plugin.getConfiguration().getSql());
        this.plugin = plugin;

        this.loadTables();
        Executors.newSingleThreadScheduledExecutor().schedule(this::runBlockingLoadQueue, 1L, TimeUnit.SECONDS);
        Executors.newSingleThreadScheduledExecutor().schedule(this::runBlockingSaveQueue, 1L, TimeUnit.SECONDS);
    }

    private void loadTables() {
        this.createClosingStatement(statement -> statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS players (uuid VARCHAR(36) UNIQUE NOT NULL, balance DOUBLE, PRIMARY KEY (uuid))"
        ));
    }

    public void loadUser(PlayerData data) {
        this.prepareClosingStatement("SELECT balance FROM players WHERE uuid=?", statement -> {
            statement.setString(1, data.getUniqueId().toString());
            ResultSet result = statement.executeQuery();
            if (result == null || !result.next()) return;
            data.setBalance(result.getDouble("balance"));
        });

        data.setLoaded(true);
    }

    public BlockingQueue<PlayerData> getSaveQueue() {
        return saveQueue;
    }

    public BlockingQueue<PlayerData> getLoadQueue() {
        return loadQueue;
    }

    public void runBlockingLoadQueue() {
        this.prepareClosingStatement("SELECT balance FROM players WHERE uuid=?", statement -> {
            while (this.plugin.isEnabled()) {
                PlayerData data;
                try {
                    data = this.loadQueue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                data.getLock().lock(); // synchronize to avoid save and load being in the same time
                try {
                    statement.setString(1, data.getUniqueId().toString());
                    ResultSet result = statement.executeQuery();
                    if (result != null && result.next()) {
                        double amount = result.getDouble("balance");
                        data.modifyBalance(currentValue -> currentValue + amount, false);
                        data.setLoaded(true);
                    }
                } finally {
                    data.getLock().unlock();
                }
            }
        });
    }

    public void runBlockingSaveQueue() {
        this.prepareClosingStatement("INSERT INTO players (uuid, balance) VALUES (?, ?) ON DUPLICATE KEY UPDATE balance = ?", statement -> {
            while (this.plugin.isEnabled()) {
                PlayerData data;
                try {
                    data = this.saveQueue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                data.getLock().lock();
                try {
                    statement.setString(1, data.getUniqueId().toString());
                    statement.setDouble(2, data.getBalance());
                    statement.setDouble(3, data.getBalance());
                    statement.executeUpdate();
                    data.setSaving(false);
                } finally {
                    data.getLock().unlock();
                }
            }
        });
    }
}
