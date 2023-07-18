package fr.candor.shop;

import org.bukkit.plugin.java.JavaPlugin;

public class ShopPlugin extends JavaPlugin {

    private static ShopPlugin instance;

    public static ShopPlugin getInstance() {
        return instance;
    }

    private ShopConfig config;

    @Override
    public void onEnable() {
        instance = this;

        this.config = new ShopConfig(this);
    }

    public ShopConfig getConfiguration() {
        return config;
    }
}
