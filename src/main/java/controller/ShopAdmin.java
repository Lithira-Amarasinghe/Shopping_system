package controller;

import service.impl.WestminsterShoppingManager;
import ui.HomeUI;

public class ShopAdmin {
    static ShoppingManagerController shoppingManagerController;

    static{
        shoppingManagerController = new ShoppingManagerController(new WestminsterShoppingManager());
    }

    ShopAdmin(){
        shoppingManagerController = new ShoppingManagerController(new WestminsterShoppingManager());
    }

    ShopAdmin(ShoppingManagerController shoppingManagerController){
        ShopAdmin.shoppingManagerController = shoppingManagerController;
    }

    public static void main(String[] args) {
        HomeUI homeUI = new HomeUI();
        shoppingManagerController.loadDataController();
        shoppingManagerController.startManagerWork();
    }
}
