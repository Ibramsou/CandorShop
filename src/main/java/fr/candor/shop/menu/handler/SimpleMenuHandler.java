package fr.candor.shop.menu.handler;

import fr.candor.shop.menu.Menu;
import fr.candor.shop.menu.MenuItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.Map;

public interface SimpleMenuHandler {

    String title();

    Map<Integer, MenuItem> itemMap();

    default InventoryType inventoryType() {
        return InventoryType.CHEST;
    }

    default int lines() {
        return 6;
    }

    default void onOpen(Menu menu, Player player) {}
}