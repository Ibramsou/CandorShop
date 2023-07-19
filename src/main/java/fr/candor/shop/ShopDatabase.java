package fr.candor.shop;

import fr.candor.shop.database.SqlDatabase;
import fr.candor.shop.player.PlayerData;
import fr.candor.shop.shop.ShopItem;
import fr.candor.shop.storage.ItemByteSerializer;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.util.*;
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
        this.createClosingStatement(statement -> statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS items (id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, uuid VARCHAR(36) UNIQUE NOT NULL, seller VARCHAR(36) NOT NULL, price DOUBLE, item varbinary(1000))"
        ));
    }

    public void sellItem(ShopItem item) {
        byte[] bytes = ItemByteSerializer.serialize(item.item());
        this.prepareClosingStatement("INSERT INTO items (uuid, seller, price, item) VALUES (?, ?, ?, ?)", statement -> {
            statement.setString(1, item.id().toString());
            statement.setString(2, item.seller().toString());
            statement.setDouble(3, item.price());
            statement.setBytes(4, bytes);
            statement.executeUpdate();
        });
    }

    public void dropItem(ShopItem item) {
        this.prepareClosingStatement("DELETE FROM items WHERE uuid=?", statement -> {
            statement.setString(1, item.id().toString());
            statement.executeUpdate();
        });
    }

    public void loadItems(Map<UUID, Set<ShopItem>> sellerItems, Set<ShopItem> menuItems) {
        this.createClosingStatement(statement -> {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM items");
            if (resultSet == null) return;
            while (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString("uuid"));
                UUID seller = UUID.fromString(resultSet.getString("seller"));
                double price = resultSet.getDouble("price");
                byte[] bytes = resultSet.getBytes("item");
                ItemStack itemStack = ItemByteSerializer.deserialize(bytes);
                ShopItem item = new ShopItem(id, seller, itemStack, price);
                sellerItems.computeIfAbsent(seller, uuid -> new HashSet<>()).add(item);
                menuItems.add(item);
            }
        });
    }

    public PlayerData loadUser(UUID uuid) {
        PlayerData data = new PlayerData(this.plugin, uuid);
        this.prepareClosingStatement("SELECT balance FROM players WHERE uuid=?", statement -> {
            statement.setString(1, data.getUniqueId().toString());
            ResultSet result = statement.executeQuery();
            if (result == null || !result.next()) return;
            data.setBalance(result.getDouble("balance"));
        });
        return data;
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
                    data.setSaving(false);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                data.getLock().lock();
                try {
                    statement.setString(1, data.getUniqueId().toString());
                    statement.setDouble(2, data.getBalance());
                    statement.setDouble(3, data.getBalance());
                    statement.executeUpdate();
                } finally {
                    data.getLock().unlock();
                }
            }
        });
    }
}
