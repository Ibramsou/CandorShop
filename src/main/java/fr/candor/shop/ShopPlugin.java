package fr.candor.shop;

import fr.candor.shop.command.balance.BalanceCommand;
import fr.candor.shop.command.manage.ManageCommand;
import fr.candor.shop.menu.MenuListener;
import fr.candor.shop.player.PlayerManager;
import fr.candor.shop.shop.ShopManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class ShopPlugin extends JavaPlugin {

    private static ShopPlugin instance;

    public static ShopPlugin getInstance() {
        return instance;
    }

    private ShopConfig config;
    private ShopDatabase database;
    private PlayerManager playerManager;
    private ShopManager shopManager;

    @Override
    public void onEnable() {
        instance = this;

        this.config = new ShopConfig(this);
        this.database = new ShopDatabase(this);
        this.playerManager = new PlayerManager();
        this.shopManager = new ShopManager();

        this.addCommands();
        this.getServer().getPluginManager().registerEvents(new MenuListener(), this); // Pearl Menu API (from mine)
    }

    private void addCommands() {
        Objects.requireNonNull(this.getServer().getPluginCommand("manage")).setExecutor(new ManageCommand(this));
        Objects.requireNonNull(this.getServer().getPluginCommand("balance")).setExecutor(new BalanceCommand(this));
    }

    public ShopDatabase getDatabase() {
        return database;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ShopConfig getConfiguration() {
        return config;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }
}
