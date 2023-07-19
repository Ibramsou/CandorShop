package fr.candor.shop.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class ItemCreator<V> extends ItemStack {

    private final ItemMeta meta;

    public ItemCreator(ItemStack itemStack) {
        super(itemStack);

        this.meta = this.getItemMeta();
    }

    public final V amount(int amount) {
        this.setAmount(amount);
        return returnInstance();
    }

    public final V displayName(String displayName) {
        meta.setDisplayName(displayName);
        return returnInstance();
    }

    public final V lore(List<String> lore) {
        meta.setLore(lore);
        return returnInstance();
    }

    public final V unbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return returnInstance();
    }

    public ItemStack buildItem() {
        this.setItemMeta(this.meta);
        return this;
    }

    protected abstract V returnInstance();
}