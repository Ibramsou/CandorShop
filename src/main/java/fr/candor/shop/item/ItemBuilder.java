package fr.candor.shop.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemBuilder extends ItemCreator<ItemBuilder> {

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    public ItemBuilder returnInstance() {
        return this;
    }
}