package fr.candor.shop.command.manage.argument;

import fr.candor.shop.ShopPlugin;
import fr.candor.shop.command.CandorArgument;
import org.bukkit.command.CommandSender;

public class HelpArgument implements CandorArgument {
    @Override
    public String argument() {
        return "help";
    }

    @Override
    public void execute(ShopPlugin plugin, CommandSender sender, String[] args) {
        
    }
}
