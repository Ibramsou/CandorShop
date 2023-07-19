package fr.candor.shop.item;

import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

public record ShopItem(UUID id, UUID seller, ItemStack item, double price) {

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
