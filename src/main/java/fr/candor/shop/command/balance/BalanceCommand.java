package fr.candor.shop.command.balance;

import fr.candor.shop.ShopPlugin;
import fr.candor.shop.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BalanceCommand implements CommandExecutor {

    private final ShopPlugin plugin;

    public BalanceCommand(ShopPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            String playerName = args[0];
            this.plugin.getPlayerManager().getByName(playerName,
                    (player, data) -> sender.sendMessage(ChatColor.GRAY + String.format("%s's balance: %s", player.getName(), data.getBalance())),
                    player -> sender.sendMessage(ChatColor.RED + "This player never joined the server"),
                    () -> sender.sendMessage(ChatColor.RED + "Data not found")
            );
            return true;
        }

        if (sender instanceof Player player) {
            PlayerData data = this.plugin.getPlayerManager().getPlayerCache().get(player.getUniqueId());
            if (data == null) {
                sender.sendMessage(ChatColor.RED + "Data not loaded, please reconnect");
                return false;
            }
            sender.sendMessage(ChatColor.GRAY + "Balance: " + data.getBalance());
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Usage: /balance <player>");
        return false;
    }
}
