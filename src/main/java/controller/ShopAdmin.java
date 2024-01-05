package controller;

import service.impl.WestminsterShoppingManager;
import ui.HomeUI;

public class ShopAdmin {
    public static void main(String[] args) {
        HomeUI homeUI = new HomeUI();
        ShoppingManagerController shoppingManagerController = new ShoppingManagerController(new WestminsterShoppingManager());
        shoppingManagerController.loadDataController();
        shoppingManagerController.startManagerWork();
    }
}
