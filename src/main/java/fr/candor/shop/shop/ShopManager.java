package fr.candor.shop.shop;

import fr.candor.shop.menu.MenuItem;
import fr.candor.shop.menu.handler.PaginatedMenuHandler;
import fr.candor.shop.module.Module;
import fr.candor.shop.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ShopManager extends Module implements PaginatedMenuHandler {

    private final Map<UUID, Set<ShopItem>> sellerItems = new HashMap<>();
    private final Set<ShopItem> menuItems = new HashSet<>();

    public void sellITem(Player player, ItemStack itemStack, double price) {
        ShopItem item = new ShopItem(UUID.randomUUID(), player.getUniqueId(), itemStack, price);
        this.sellerItems.computeIfAbsent(player.getUniqueId(), uuid -> new HashSet<>()).add(item);
        this.menuItems.add(item);
    }

    public void buyItem(Player buyer, ShopItem item) {
        PlayerData data = this.plugin.getPlayerManager().getPlayerCache().get(buyer.getUniqueId());
        if (data == null) return;
        if (data.getBalance() > item.price()) {
            this.sellerItems.get(item.seller()).remove(item);
            this.menuItems.remove(item);
            this.plugin.getPlayerManager().modifyBalance(item.seller(), currentValue -> currentValue + item.price());
            data.modifyBalance(currentValue -> currentValue - item.price());
        } else {
            buyer.sendMessage(ChatColor.RED + "You don't have enough money");
        }
    }

    public Collection<ShopItem> getSellingItems(UUID uuid) {
        Set<ShopItem> selling = this.sellerItems.get(uuid);
        if (selling == null) return List.of();
        return selling;
    }

    @Override
    public String title() {
        return ChatColor.GREEN + "Auction House";
    }

    @Override
    public Collection<? extends MenuItem> allItems() {
        return this.menuItems;
    }
}
