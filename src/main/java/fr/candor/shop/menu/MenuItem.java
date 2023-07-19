package fr.candor.shop.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface MenuItem {

    void click(Menu menu, MenuHolder holder, InventoryClickEvent event, Player player, ClickType type);

    ItemStack buildItem();
}