package fr.candor.shop.menu.item;

import fr.candor.shop.menu.Menu;
import fr.candor.shop.menu.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface ItemConsumer {

    void accept(Menu menu, MenuHolder holder, InventoryClickEvent event, Player player, ClickType type);
}