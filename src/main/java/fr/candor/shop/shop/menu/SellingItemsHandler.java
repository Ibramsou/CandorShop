package fr.candor.shop.shop.menu;

import fr.candor.shop.ShopPlugin;
import fr.candor.shop.menu.MenuItem;
import fr.candor.shop.menu.handler.SimpleMenuHandler;
import fr.candor.shop.shop.ShopItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SellingItemsHandler implements SimpleMenuHandler {

    private final Collection<ShopItem> sellingItems;

    public SellingItemsHandler(ShopPlugin plugin, Player player) {
        this.sellingItems = plugin.getShopManager().getSellingItems(player.getUniqueId());
    }

    @Override
    public String title() {
        return ChatColor.AQUA + "Your Selling Items";
    }

    @Override
    public Map<Integer, MenuItem> itemMap() {
        Map<Integer, MenuItem> map = new HashMap<>();
        int index = 0;
        for (ShopItem sellingItem : this.sellingItems) {
            map.put(index, sellingItem);
            index++;
        }
        return map;
    }

    @Override
    public int lines() {
        return Math.max(1, (int) Math.ceil((double) this.sellingItems.size() / 9.0));
    }
}
