package fr.candor.shop.command.manage;

import fr.candor.shop.ShopPlugin;
import fr.candor.shop.command.CandorArgument;
import fr.candor.shop.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public interface ManageDataArgument extends CandorArgument {

    ExecutorService OFFLINE_POOL = Executors.newCachedThreadPool();
    Pattern PLAYER_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{1,16}$");

    @SuppressWarnings("deprecation")
    default void execute(ShopPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(usage());
            return;
        }

        String playerName = args[0];

        if (!PLAYER_NAME_PATTERN.matcher(playerName).matches()) {
            sender.sendMessage(ChatColor.RED + "Please type a valid player name");
            return;
        }

        OFFLINE_POOL.execute(() -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
            PlayerData data = plugin.getPlayerManager().getPlayerCache().get(player.getUniqueId());
            manage(plugin, player, data, Arrays.copyOfRange(args, args.length + 1, args.length));
        });
    }

    void manage(ShopPlugin plugin, OfflinePlayer player, PlayerData data, String[] args);

    String usage();
}
