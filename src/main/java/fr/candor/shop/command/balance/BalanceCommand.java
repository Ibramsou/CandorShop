package fr.candor.shop.command.balance;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BalanceCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) {

            return false;
        }
        if (sender instanceof Player player) {
            return false;
        }

        sender.sendMessage(ChatColor.RED + "You must be a player to run this command");
        return false;
    }
}
