package fr.candor.shop.command.manage.argument;

import fr.candor.shop.ShopPlugin;
import fr.candor.shop.command.manage.ManageDataArgument;
import fr.candor.shop.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class SetArgument implements ManageDataArgument {
    @Override
    public String argument() {
        return "set";
    }

    @Override
    public String usage() {
        return ChatColor.RED + "Usage: /manage set <player> <amount>";
    }

    @Override
    public void manage(ShopPlugin plugin, CommandSender sender, OfflinePlayer player, PlayerData data, String[] args) {

    }


}
