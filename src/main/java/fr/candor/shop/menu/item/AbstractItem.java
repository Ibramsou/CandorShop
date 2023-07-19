package fr.candor.shop.menu.item;

import fr.candor.shop.menu.MenuItem;
import fr.candor.shop.item.ItemCreator;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractItem extends ItemCreator<AbstractItem> implements MenuItem {

    public AbstractItem(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    protected AbstractItem returnInstance() {
        return this;
    }
}