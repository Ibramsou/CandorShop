package fr.candor.shop;

import org.bukkit.plugin.java.JavaPlugin;

public class ShopPlugin extends JavaPlugin {

    private ShopConfig config;

    @Override
    public void onEnable() {
        this.config = new ShopConfig(this);
    }

    public ShopConfig getConfiguration() {
        return config;
    }
}
