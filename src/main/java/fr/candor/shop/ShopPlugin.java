package fr.candor.shop;

import fr.candor.shop.command.balance.BalanceCommand;
import fr.candor.shop.command.manage.ManageCommand;
import fr.candor.shop.player.PlayerManager;
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

    @Override
    public void onEnable() {
        instance = this;

        this.config = new ShopConfig(this);
        this.database = new ShopDatabase(this);
        this.playerManager = new PlayerManager();

        this.addCommands();
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
}
