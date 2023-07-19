package fr.candor.shop.command.manage;

import fr.candor.shop.ShopPlugin;
import fr.candor.shop.command.CandorArgument;
import fr.candor.shop.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.regex.Pattern;

public interface ManageDataArgument extends CandorArgument {

    Pattern PLAYER_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{1,16}$");

    default void execute(ShopPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(usage());
            return;
        }

        String playerName = args[0];

        plugin.getPlayerManager().getByName(playerName,
                (player, data) -> manage(plugin, sender, player, data, Arrays.copyOfRange(args, 1, args.length)),
                player -> sender.sendMessage(ChatColor.RED + "This player never joined the server"),
                () -> sender.sendMessage(ChatColor.RED + "Please type a valid player name")
        );
    }

    void manage(ShopPlugin plugin, CommandSender sender, OfflinePlayer player, PlayerData data, String[] args);

    String usage();
}
