package fr.candor.shop.listener;

import fr.candor.shop.ShopPlugin;

public abstract class Module {

    protected final ShopPlugin plugin;

    public Module() {
        this.plugin = ShopPlugin.getInstance();
    }

    public final <T extends Module> void listener(ModuleListener<T> listener) {
        this.plugin.getServer().getPluginManager().registerEvents(listener, listener.plugin);
    }

    public final ShopPlugin getPlugin() {
        return plugin;
    }
}
