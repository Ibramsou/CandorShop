package fr.candor.shop.command.manage.argument;

import fr.candor.shop.ShopPlugin;
import fr.candor.shop.command.CandorArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpArgument implements CandorArgument {
    @Override
    public String argument() {
        return "help";
    }

    @Override
    public void execute(ShopPlugin plugin, CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.DARK_PURPLE + "Manage Balance Commands:");
        sender.sendMessage(ChatColor.GRAY + " > " + ChatColor.LIGHT_PURPLE + " /manage help");
        sender.sendMessage(ChatColor.GRAY + " > " + ChatColor.LIGHT_PURPLE + " /manage look <player>");
        sender.sendMessage(ChatColor.GRAY + " > " + ChatColor.LIGHT_PURPLE + " /manage set <player> <amount>");
        sender.sendMessage(ChatColor.GRAY + " > " + ChatColor.LIGHT_PURPLE + " /manage add <player> <amount>");
        sender.sendMessage(ChatColor.GRAY + " > " + ChatColor.LIGHT_PURPLE + " /manage remove <player> <amount>");
    }
}
