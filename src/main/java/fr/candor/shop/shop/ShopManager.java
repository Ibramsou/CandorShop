package fr.candor.shop.shop;

import fr.candor.shop.item.ItemBuilder;
import fr.candor.shop.menu.MenuItem;
import fr.candor.shop.menu.handler.PaginatedMenuHandler;
import fr.candor.shop.menu.item.FunctionalItem;
import fr.candor.shop.menu.type.SimpleMenu;
import fr.candor.shop.module.Module;
import fr.candor.shop.player.PlayerData;
import fr.candor.shop.shop.menu.SellingItemsHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ShopManager extends Module implements PaginatedMenuHandler {

    private final Map<UUID, Set<ShopItem>> sellerItems = new HashMap<>();
    private final Set<ShopItem> menuItems = new LinkedHashSet<>();

    public ShopManager() {
        this.plugin.getDatabase().loadItems(sellerItems, menuItems);
    }

    public void sellITem(Player player, ItemStack itemStack, double price) {
        ShopItem item = new ShopItem(UUID.randomUUID(), player.getUniqueId(), itemStack, price);
        this.sellerItems.computeIfAbsent(player.getUniqueId(), uuid -> new HashSet<>()).add(item);
        this.menuItems.add(item);
        this.plugin.getServer().getScheduler().runTaskLaterAsynchronously(this.plugin, () -> this.plugin.getDatabase().sellItem(item), 1L);
        player.sendMessage(ChatColor.GREEN + "Added your item to shop for " + price + "$");
        player.getInventory().setItemInMainHand(null);
    }

    public void buyItem(Player buyer, ShopItem item) {
        if (buyer.getUniqueId().equals(item.seller())) {
            buyer.sendMessage(ChatColor.RED + "You cannot buy your own item !");
            return;
        }
        PlayerData data = this.plugin.getPlayerManager().getPlayerCache().get(buyer.getUniqueId());
        if (data == null) return;
        if (data.getBalance() > item.price()) {
            this.plugin.getPlayerManager().modifyBalance(item.seller(), currentValue -> currentValue + item.price());
            data.modifyBalance(currentValue -> currentValue - item.price());
            this.removeItem(buyer, item, false);
        } else {
            buyer.sendMessage(ChatColor.RED + "You don't have enough money");
        }
    }

    public void removeItem(Player player, ShopItem item, boolean remove) {
        if (player != null) {
            player.getInventory().addItem(item.item());
            player.closeInventory();
            if (remove) {
                player.sendMessage(ChatColor.RED + "You removed a selling item");
            } else {
                player.sendMessage(ChatColor.RED + String.format("You bought an item for %s$", item.price()));
            }
        }
        this.sellerItems.get(item.seller()).remove(item);
        this.menuItems.remove(item);
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, () -> this.plugin.getDatabase().dropItem(item), 1L);
    }

    public Collection<ShopItem> getSellingItems(UUID uuid) {
        Set<ShopItem> selling = this.sellerItems.get(uuid);
        if (selling == null) return List.of();
        return selling;
    }

    @Override
    public String title() {
        return ChatColor.RED + "Auction House";
    }

    @Override
    public Collection<? extends MenuItem> allItems() {
        return this.menuItems;
    }

    @Override
    public Map<Integer, MenuItem> itemsPerPage() {
        Map<Integer, MenuItem> map = PaginatedMenuHandler.super.itemsPerPage();
        FunctionalItem sellingItems = new FunctionalItem(new ItemBuilder(Material.COMPASS).displayName(ChatColor.AQUA + "Your Selling Items").buildItem())
                .setConsumer((menu, holder, event, player, type) -> new SimpleMenu(new SellingItemsHandler(this.plugin, player)).open(player));
        map.put(4, sellingItems);
        return map;
    }
}
