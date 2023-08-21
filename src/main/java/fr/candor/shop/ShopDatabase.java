package fr.candor.shop;

import fr.candor.shop.database.SqlDatabase;
import fr.candor.shop.player.PlayerData;
import fr.candor.shop.shop.ShopItem;
import fr.candor.shop.storage.ItemByteSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

public class ShopDatabase extends SqlDatabase {

    private final ScheduledExecutorService loadPool = Executors.newSingleThreadScheduledExecutor(), savePool = Executors.newSingleThreadScheduledExecutor();
    private final BlockingQueue<PlayerData> loadQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<PlayerData> saveQueue = new LinkedBlockingQueue<>();
    private final ShopPlugin plugin;

    public ShopDatabase(ShopPlugin plugin) {
        super(plugin.getConfiguration().getSql());
        this.plugin = plugin;

        this.loadTables();
        this.loadPool.scheduleAtFixedRate(this::loadUsers, 50 * 5, 50 * 5, TimeUnit.MILLISECONDS);
        this.savePool.scheduleAtFixedRate(this::saveUsers, 1L, 1L, TimeUnit.MINUTES);
    }

    public void disable() {
        this.loadPool.shutdownNow();
        this.savePool.shutdownNow();
        this.loadUsers();
        this.saveUsers();
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
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) data.setPlayer(player);
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

    public void loadUsers() {
        PlayerData firstElement = this.loadQueue.poll();
        if (firstElement == null) {
            return;
        }

        this.prepareClosingStatement("SELECT balance FROM players WHERE uuid=?", statement -> {
            this.loadUser(firstElement, statement);
            PlayerData data;
            while ((data = this.loadQueue.poll()) != null) {
                this.loadUser(data, statement);
            }
        });
    }

    private void loadUser(PlayerData data, PreparedStatement statement) throws SQLException {
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

    public void saveUsers() {
        PlayerData firstElement = this.saveQueue.poll();
        if (firstElement == null) {
            return;
        }
        this.prepareClosingStatement("INSERT INTO players (uuid, balance) VALUES (?, ?) ON DUPLICATE KEY UPDATE balance = ?", statement -> {
            this.saveUser(firstElement, statement);
            PlayerData data;
            while ((data = this.saveQueue.poll()) != null) {
                this.saveUser(data, statement);
            }
        });
    }

    private void saveUser(PlayerData data, PreparedStatement statement) throws SQLException {
        data.setSaving(false);
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
}
