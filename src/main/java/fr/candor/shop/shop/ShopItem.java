package fr.candor.shop.shop;

import fr.candor.shop.ShopPlugin;
import fr.candor.shop.item.ItemBuilder;
import fr.candor.shop.menu.Menu;
import fr.candor.shop.menu.MenuHolder;
import fr.candor.shop.menu.MenuItem;
import fr.candor.shop.menu.type.PaginatedMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record ShopItem(UUID id, UUID seller, ItemStack item, double price) implements MenuItem {

    @Override
    public void click(Menu menu, MenuHolder holder, InventoryClickEvent event, Player player, ClickType type) {
        if (type == ClickType.RIGHT || type == ClickType.LEFT) {
            ShopPlugin.getInstance().getShopManager().buyItem(player, this);
            player.closeInventory();
        }
    }

    @Override
    public ItemStack buildItem() {
        return new ItemBuilder(this.item)
                .addLore(List.of(
                        "",
                        ChatColor.LIGHT_PURPLE + "Amount: " + ChatColor.GRAY + this.item.getAmount(),
                        ChatColor.LIGHT_PURPLE + "Price: " + ChatColor.GRAY + this.price
                )).buildItem();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopItem item = (ShopItem) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
