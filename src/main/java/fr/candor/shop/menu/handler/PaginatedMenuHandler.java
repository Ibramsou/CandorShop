package fr.candor.shop.menu.handler;

import fr.candor.shop.menu.MenuItem;
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
        PreviousPageItem previous = new PreviousPageItem(Material.ARROW).displayName(ChatColor.AQUA + "Previous Page").successSound(success);
        NextPageItem next = new NextPageItem(Material.ARROW).displayName(ChatColor.GREEN + "Next Page").successSound(success);
        itemMap.put(0, previous);
        itemMap.put(8, next);
        itemMap.put(45, previous);
        itemMap.put(53, next);
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