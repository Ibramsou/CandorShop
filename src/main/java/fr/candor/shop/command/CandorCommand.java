package fr.candor.shop.command;

import fr.candor.shop.ShopPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class CandorCommand implements CommandExecutor {

    private final Map<String, CandorArgument> arguments = new HashMap<>();
    protected final ShopPlugin plugin;

    public CandorCommand(ShopPlugin plugin) {
        this.plugin = plugin;

        setup();
    }

    public abstract void setup();

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
            sender.sendMessage(ChatColor.RED + "You don't have permission.");
            return false;
        }

        if (args.length == 0) {
            this.sendHelp(sender);
            return false;
        }

        CandorArgument argument = this.arguments.get(args[0].toLowerCase());
        if (argument == null) {
            this.sendHelp(sender);
            return false;
        }

        argument.execute(this.plugin, sender, Arrays.copyOfRange(args, 1, args.length));
        return false;
    }

    protected void sendHelp(CommandSender sender) {
        CandorArgument argument = arguments.get("help");
        if (argument == null) return;
        argument.execute(this.plugin, sender, null);
    }

    protected final void addArgument(CandorArgument argument) {
        this.arguments.put(argument.argument(), argument);
    }
}
