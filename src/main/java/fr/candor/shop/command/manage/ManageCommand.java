package fr.candor.shop.command.manage;

import fr.candor.shop.ShopPlugin;
import fr.candor.shop.command.CandorCommand;
import fr.candor.shop.command.manage.argument.*;

public class ManageCommand extends CandorCommand {

    public ManageCommand(ShopPlugin plugin) {
        super(plugin);
    }

    @Override
    public void setup() {
        this.addArgument(new HelpArgument());
        this.addArgument(new LookArgument());
        this.addArgument(new RemoveArgument());
        this.addArgument(new SetArgument());
        this.addArgument(new AddArgument());
    }
}
