package fr.candor.shop.command.manage;

import fr.candor.shop.ShopPlugin;
import fr.candor.shop.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public interface ManageValueArgument extends ManageDataArgument {

    default void manage(ShopPlugin plugin, CommandSender sender, OfflinePlayer player, PlayerData data, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(usage());
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Please type a valid number");
            return;
        }

        manage(plugin, sender, player, data, amount);
    }

    void manage(ShopPlugin plugin, CommandSender sender, OfflinePlayer player, PlayerData data, double amount);
}
