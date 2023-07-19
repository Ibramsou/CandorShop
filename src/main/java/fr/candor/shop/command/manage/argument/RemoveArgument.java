package fr.candor.shop.command.manage.argument;

import fr.candor.shop.ShopPlugin;
import fr.candor.shop.command.manage.ManageDataArgument;
import fr.candor.shop.command.manage.ManageValueArgument;
import fr.candor.shop.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class RemoveArgument implements ManageValueArgument {
    @Override
    public String argument() {
        return "remove";
    }

    @Override
    public String usage() {
        return ChatColor.RED + "Usage: /manage remove <player> <amount>";
    }

    @Override
    public void manage(ShopPlugin plugin, CommandSender sender, OfflinePlayer player, PlayerData data, double amount) {

    }
}
