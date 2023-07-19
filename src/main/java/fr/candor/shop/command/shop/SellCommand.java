package fr.candor.shop.command.shop;

import fr.candor.shop.ShopPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SellCommand implements CommandExecutor {

    private final ShopPlugin plugin;

    public SellCommand(ShopPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /sell <price>");
                return false;
            }
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType() == Material.AIR) {
                sender.sendMessage(ChatColor.RED + "Please select an item in your hand");
                return false;
            }
            double price;
            try {
                price = Double.parseDouble(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Please type a valid number !");
                return false;
            }
            this.plugin.getShopManager().sellITem(player, itemStack, price);
            return false;
        }

        sender.sendMessage(ChatColor.RED + "You must be a player to perform this command");
        return false;
    }
}
