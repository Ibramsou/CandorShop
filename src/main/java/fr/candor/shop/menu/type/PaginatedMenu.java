package fr.candor.shop.menu.type;

import fr.candor.shop.menu.Menu;
import fr.candor.shop.menu.MenuItem;
import fr.candor.shop.menu.handler.PaginatedMenuHandler;
import fr.candor.shop.menu.page.MenuPage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PaginatedMenu implements Menu {


    private final PaginatedMenuHandler handler;
    private final List<MenuPage> pageList = new ArrayList<>();
    private final int[] pattern;
    private int pages;

    public PaginatedMenu(PaginatedMenuHandler handler) {
        this.handler = handler;
        this.pattern = handler.itemsPattern();
        this.refresh();
    }

    public MenuPage open(Player player) {
        return this.open(player, 1);
    }

    public MenuPage open(Player player, int page) {
        int index = page - 1;
        if (this.pages == 0) {
            player.sendMessage(ChatColor.RED + "Gui is empty");
            return null;
        }
        if (index >= this.pages || index < 0) {
            return null;
        }
        MenuPage pearlPage = this.pageList.get(index);
        pearlPage.open(player);
        return pearlPage;
    }

    public void refresh() {
        this.pageList.clear();
        LinkedList<? extends MenuItem> items = new LinkedList<>(this.handler.allItems());
        int itemsPerPage = this.pattern.length;
        this.pages = (int) Math.ceil((double) items.size() / (double) itemsPerPage);

        for (int pageIndex = 0; pageIndex < pages; pageIndex++) {
            MenuPage page = new MenuPage(this.handler, pageIndex + 1, this);
            int itemSize = Math.min(items.size(), itemsPerPage);
            for (int i = 0; i < itemSize; i++) {
                MenuItem item = items.pollFirst();
                if (item == null) break;
                int slot = this.pattern[i];
                page.setItem(slot, item);
            }
            this.pageList.add(page);
        }
    }

    public List<MenuPage> getPageList() {
        return this.pageList;
    }

    public int getPageSize() {
        return this.pages;
    }


}