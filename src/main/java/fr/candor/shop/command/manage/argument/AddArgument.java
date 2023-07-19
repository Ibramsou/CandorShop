package fr.candor.shop.command.manage.argument;

import fr.candor.shop.ShopPlugin;
import fr.candor.shop.command.manage.ManageValueArgument;
import fr.candor.shop.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class AddArgument implements ManageValueArgument {
    @Override
    public String argument() {
        return "add";
    }

    @Override
    public String usage() {
        return ChatColor.RED + "Usage: /manage add <player> <amount>";
    }

    @Override
    public void manage(ShopPlugin plugin, CommandSender sender, OfflinePlayer player, PlayerData data, double amount) {

    }
}
