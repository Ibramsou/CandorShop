package fr.candor.shop.menu.type;

import fr.candor.shop.menu.Menu;
import fr.candor.shop.menu.MenuItem;
import fr.candor.shop.menu.handler.SimpleMenuHandler;
import fr.candor.shop.menu.holder.SimpleHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SimpleMenu implements SimpleHolder, Menu {

    private final Inventory inventory;
    private final Map<Integer, MenuItem> itemMap = new HashMap<>();
    private final SimpleMenuHandler handler;

    public SimpleMenu(SimpleMenuHandler menu) {
        InventoryType type = menu.inventoryType();
        if (type == InventoryType.CHEST) {
            this.inventory = Bukkit.createInventory(this, menu.lines() * 9, menu.title());
        } else {
            this.inventory = Bukkit.createInventory(this, type, menu.title());
        }

        this.handler = menu;
        this.refresh();
    }

    @Override
    public void refresh() {
        this.inventory.clear();
        for (Map.Entry<Integer, MenuItem> entry : handler.itemMap().entrySet()) {
            int slot = entry.getKey();
            MenuItem item = entry.getValue();
            this.inventory.setItem(slot, item.buildItem());
            this.itemMap.put(slot, item);
        }
    }

    public void clear() {
        this.inventory.clear();
        this.itemMap.clear();
    }

    public void open(Player player) {
        player.openInventory(this.getInventory());
        this.handler.onOpen(this, player);
        player.updateInventory();
    }

    public void setItem(int slot, MenuItem item) {
        itemMap.put(slot, item);
        this.inventory.setItem(slot, item.buildItem());
    }

    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public Map<Integer, MenuItem> getItemMap() {
        return itemMap;
    }

    public Menu getMenu() {
        return this;
    }
}