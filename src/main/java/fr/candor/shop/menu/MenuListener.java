package fr.candor.shop.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class MenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = player.getOpenInventory().getTopInventory();
        if (inventory.getHolder() instanceof MenuHolder holder) {
            event.setCancelled(true);
            MenuItem item = holder.getItemMap().get(event.getSlot());
            if (item == null) return;
            item.click(holder.getMenu(), holder, event, player, event.getClick());
        }
    }
}