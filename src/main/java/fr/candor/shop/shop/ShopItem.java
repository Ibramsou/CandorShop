package fr.candor.shop.shop;

import fr.candor.shop.menu.Menu;
import fr.candor.shop.menu.MenuHolder;
import fr.candor.shop.menu.MenuItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

public record ShopItem(UUID id, UUID seller, ItemStack item, double price) implements MenuItem {

    @Override
    public void click(Menu menu, MenuHolder holder, InventoryClickEvent event, Player player, ClickType type) {

    }

    @Override
    public ItemStack buildItem() {
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopItem shopItem = (ShopItem) o;
        return Objects.equals(id, shopItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
