package fr.candor.shop.command.shop;

import fr.candor.shop.ShopPlugin;
import fr.candor.shop.menu.type.PaginatedMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShopCommand implements CommandExecutor {

    private final ShopPlugin plugin;

    public ShopCommand(ShopPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            new PaginatedMenu(this.plugin.getShopManager()).open(player);
            return false;
        }

        sender.sendMessage(ChatColor.RED + "You must be a player to perform this command");
        return false;
    }
}
