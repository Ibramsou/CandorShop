package fr.candor.shop.command.manage.argument;

import fr.candor.shop.ShopPlugin;
import fr.candor.shop.command.manage.ManageDataArgument;
import fr.candor.shop.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

public class AddArgument implements ManageDataArgument {
    @Override
    public String argument() {
        return "add";
    }

    @Override
    public void manage(ShopPlugin plugin, OfflinePlayer player, PlayerData data, String[] args) {

    }

    @Override
    public String usage() {
        return ChatColor.RED + "Usage: /manage add <player> <amount>";
    }
}
