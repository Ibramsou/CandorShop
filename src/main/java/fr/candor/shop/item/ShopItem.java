package fr.candor.shop.item;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public record ShopItem(ItemStack item, int price, UUID seller) {
}
