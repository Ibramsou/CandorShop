package fr.candor.shop.command.manage.argument;

import fr.candor.shop.ShopPlugin;
import fr.candor.shop.command.manage.ManageValueArgument;
import fr.candor.shop.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class SetArgument implements ManageValueArgument {
    @Override
    public String argument() {
        return "set";
    }

    @Override
    public String usage() {
        return ChatColor.RED + "Usage: /manage set <player> <amount>";
    }

    @Override
    public void manage(ShopPlugin plugin, CommandSender sender, OfflinePlayer player, PlayerData data, double amount) {
        data.setBalance(amount);
        sender.sendMessage(ChatColor.LIGHT_PURPLE + String.format("Changed %s's to %s", player.getName(), amount));
        if (player.getPlayer() != null && player.isOnline()) {
            player.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + String.format("Your balance has been changed to %s", amount));
        }
    }


}
