package fr.candor.shop.menu.page.item;

import fr.candor.shop.menu.Menu;
import fr.candor.shop.menu.MenuHolder;
import fr.candor.shop.menu.MenuItem;
import fr.candor.shop.menu.holder.PageHolder;
import fr.candor.shop.menu.page.MenuPage;
import fr.candor.shop.menu.type.PaginatedMenu;
import fr.candor.shop.util.ItemCreator;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PreviousPageItem extends ItemCreator<PreviousPageItem> implements MenuItem {

    private Sound errorSound;
    private Sound successSound;

    public PreviousPageItem(Material material) {
        super(new ItemStack(material));
    }

    public PreviousPageItem(ItemStack itemStack) {
        super(itemStack);
    }

    public PreviousPageItem errorSound(Sound sound) {
        this.errorSound = sound;
        return this;
    }

    public PreviousPageItem successSound(Sound sound) {
        this.successSound = sound;
        return this;
    }

    @Override
    protected PreviousPageItem returnInstance() {
        return this;
    }

    @Override
    public void click(Menu menu, MenuHolder holder, InventoryClickEvent event, Player player, ClickType type) {
        if (menu instanceof PaginatedMenu paginated) {
            MenuPage page = ((PageHolder) holder).getPage();
            MenuPage previousPage = paginated.open(player, page.getPageIndex() - 1);
            if (previousPage == null && this.errorSound != null) {
                player.playSound(player.getLocation(), this.errorSound, 1F, 1F);
            } else if (previousPage != null && this.successSound != null) {
                player.playSound(player.getLocation(), this.successSound, 1F, 1F);
            }
        }
    }
}