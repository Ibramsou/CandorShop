package fr.candor.shop.shop.menu;

import fr.candor.shop.ShopPlugin;
import fr.candor.shop.item.ItemBuilder;
import fr.candor.shop.menu.MenuItem;
import fr.candor.shop.menu.handler.SimpleMenuHandler;
import fr.candor.shop.menu.item.FunctionalItem;
import fr.candor.shop.shop.ShopItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class RemoveSellingItemHandler implements SimpleMenuHandler {

    private final ShopPlugin plugin;
    private final ShopItem item;

    public RemoveSellingItemHandler(ShopPlugin plugin, ShopItem item) {
        this.plugin = plugin;
        this.item = item;
    }

    @Override
    public String title() {
        return ChatColor.RED + "Remove Selling Item ?";
    }

    @Override
    public Map<Integer, MenuItem> itemMap() {
        Map<Integer, MenuItem> map = new HashMap<>();
        FunctionalItem confirm = new FunctionalItem(new ItemBuilder(Material.GREEN_WOOL).displayName(ChatColor.GREEN + "Confirm").buildItem())
                .setConsumer((menu, holder, event, player, type) -> this.plugin.getShopManager().removeItem(player, this.item, true));
        FunctionalItem cancel = new FunctionalItem(new ItemBuilder(Material.RED_WOOL).displayName(ChatColor.RED + "Cancel").buildItem())
                .setConsumer((menu, holder, event, player, type) -> player.closeInventory());
        map.put(12, confirm);
        map.put(14, cancel);
        return map;
    }

    @Override
    public int lines() {
        return 3;
    }
}
