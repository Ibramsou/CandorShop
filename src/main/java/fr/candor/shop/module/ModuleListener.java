package fr.candor.shop.module;

import fr.candor.shop.ShopPlugin;
import org.bukkit.event.Listener;

public abstract class ModuleListener<T extends Module> implements Listener {

    protected final T module;
    protected final ShopPlugin plugin;

    public ModuleListener(T module) {
        this.module = module;
        this.plugin = module.getPlugin();
    }
}
