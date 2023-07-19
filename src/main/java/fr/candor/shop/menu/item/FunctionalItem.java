package fr.candor.shop.menu.item;

import fr.candor.shop.menu.Menu;
import fr.candor.shop.menu.MenuHolder;
import fr.candor.shop.menu.MenuItem;
import fr.candor.shop.item.ItemCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class FunctionalItem extends ItemCreator<FunctionalItem> implements MenuItem {

    private ItemConsumer consumer;

    public FunctionalItem(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    protected FunctionalItem returnInstance() {
        return this;
    }

    public FunctionalItem setConsumer(ItemConsumer consumer) {
        this.consumer = consumer;
        return this;
    }

    @Override
    public void click(Menu menu, MenuHolder holder, InventoryClickEvent event, Player player, ClickType type) {
        if (this.consumer != null) this.consumer.accept(menu, holder, event, player, type);
    }
}