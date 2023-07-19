package fr.candor.shop.menu.holder;

import fr.candor.shop.menu.MenuHolder;
import fr.candor.shop.menu.page.MenuPage;

public interface PageHolder extends MenuHolder {

    MenuPage getPage();
}