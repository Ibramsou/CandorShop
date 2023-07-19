package fr.candor.shop.command;

import fr.candor.shop.ShopPlugin;
import org.bukkit.command.CommandSender;

public interface CandorArgument {

    String argument();

    void execute(ShopPlugin plugin, CommandSender sender, String[] args);
}
