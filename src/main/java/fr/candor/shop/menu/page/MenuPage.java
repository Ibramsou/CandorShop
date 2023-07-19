package fr.candor.shop.menu.page;

import fr.candor.shop.menu.Menu;
import fr.candor.shop.menu.MenuItem;
import fr.candor.shop.menu.handler.PaginatedMenuHandler;
import fr.candor.shop.menu.holder.PageHolder;
import fr.candor.shop.menu.type.PaginatedMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MenuPage implements PageHolder {

    private final PaginatedMenu menu;
    private final PaginatedMenuHandler handler;
    private final Inventory inventory;
    private final int page;
    private final Map<Integer, MenuItem> itemMap = new HashMap<>();

    public MenuPage(PaginatedMenuHandler handler, int page, PaginatedMenu menu) {
        this.inventory = Bukkit.createInventory(this, 9 * handler.linesPerPage(), String.format(handler.title(), page, menu.getPageSize()));
        this.page = page;
        this.handler = handler;
        this.menu = menu;

        this.refresh();
    }

    public void open(Player player) {
        player.openInventory(this.inventory);
        this.handler.onPageOpened(player, this);
        player.updateInventory();
    }

    public void setItem(int slot, MenuItem item) {
        this.inventory.setItem(slot, item.buildItem());
        this.itemMap.put(slot, item);
    }

    public void refresh() {
        this.handler.itemsPerPage().forEach(this::setItem);
    }

    public void clear() {
        this.inventory.clear();
    }

    public MenuPage getPage() {
        return this;
    }

    public int getPageIndex() {
        return this.page;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public Map<Integer, MenuItem> getItemMap() {
        return this.itemMap;
    }

    public Menu getMenu() {
        return this.menu;
    }
}