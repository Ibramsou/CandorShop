package fr.candor.shop.item;

import org.bukkit.inventory.ItemStack;

public class ItemBuilder extends ItemCreator<ItemBuilder> {

    public ItemBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    public ItemBuilder returnInstance() {
        return this;
    }
}