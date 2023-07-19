package fr.candor.shop.menu.handler;

import fr.candor.shop.item.ItemBuilder;
import fr.candor.shop.menu.MenuItem;
import fr.candor.shop.menu.item.FunctionalItem;
import fr.candor.shop.menu.page.MenuPage;
import fr.candor.shop.menu.page.item.NextPageItem;
import fr.candor.shop.menu.page.item.PreviousPageItem;
import fr.candor.shop.menu.util.PagePatternUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public interface PaginatedMenuHandler {

    String title();

    default Map<Integer, MenuItem> itemsPerPage() {
        Map<Integer, MenuItem> itemMap = new HashMap<>();
        Sound success = Sound.BLOCK_NOTE_BLOCK_PLING;
        FunctionalItem empty = new FunctionalItem(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).displayName(" ").buildItem());
        PreviousPageItem previous = new PreviousPageItem(Material.REDSTONE).displayName(ChatColor.AQUA + "Previous Page").successSound(success);
        NextPageItem next = new NextPageItem(Material.SLIME_BALL).displayName(ChatColor.GREEN + "Next Page").successSound(success);
        for (int i = 0; i < 9; i++) {
            itemMap.put(i, empty);
        }
        int start = 9 * (this.linesPerPage() - 1);
        int end = start + 9;
        for (int i = start; i < end; i++) {
            itemMap.put(i, empty);
        }
        itemMap.put(2, previous);
        itemMap.put(6, next);
        return itemMap;
    }

     Collection<? extends MenuItem> allItems();

    default void onPageOpened(Player player, MenuPage page) {}

    default int[] itemsPattern() {
        return PagePatternUtil.squarePattern(0, 9, 1, 5);
    }

    default int linesPerPage() {
        return 6;
    }

}