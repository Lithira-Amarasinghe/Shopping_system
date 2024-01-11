package controller;

import lombok.NoArgsConstructor;
import model.Clothing;
import model.Electronics;
import model.Product;
import repository.ShopData;
import service.ShoppingManager;
import util.ShopUtil;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@NoArgsConstructor
public class ShoppingManagerController {
    static Scanner scanner = new Scanner(System.in);
    private static int option;
    ShoppingManager shoppingManager;
    Product product;

//    public ShoppingManagerController(){}

    public ShoppingManagerController(ShoppingManager shoppingManager) {
        this.shoppingManager = shoppingManager;
    }

    public static int getOption() {
        try {
            System.out.print("Enter the option no. : ");
            option = scanner.nextInt();
        } catch (InputMismatchException ex) {
            System.out.println("!!! Invalid input");
            ex.printStackTrace();
        } finally {
            return option;
        }
    }

    public static String getProductId() {
        String productId = null;
        scanner.nextLine();
        try {
            System.out.print("Enter the product id to delete : ");
            productId = scanner.nextLine();
        } catch (InputMismatchException e) {

        }
        return productId;

    }

    public void startManagerWork() {
        getMainOption();
        loadDataController();

    }

    public void getMainOption() {
        while (true) {
            shoppingManager.displayMenu();
            ShoppingManagerController.getOption();
            if (option == -1) {
                System.out.println("Exit");
                break;
            } else if (option == 1) {
                shoppingManager.addANewProduct(getNewProductDetails());
            } else if (option == 2) {
                String productId = ShoppingManagerController.getProductId();
                shoppingManager.deleteAProduct(productId);
            } else if (option == 3) {
                shoppingManager.printProductList();
            } else if (option == 4) {
                shoppingManager.saveProducts();
            } else if (option == 5) {
                shoppingManager.openShop();
            } else if (option == 6) {
                getStockChangeData();
            } else continue;
        }
    }

    public Product getNewProductDetails() {
        try {
            while (true) {
                System.out.println("\t\t---- Add product ----\n");
                System.out.print("Product is Electronic(E/e) item or Clothing(C/c) ? ");
                scanner.nextLine();
                String itemType = scanner.nextLine().trim().toLowerCase();
                System.out.println(itemType);
                System.out.print("Product id    : ");
                String productId = scanner.nextLine();
                System.out.print("Product name  : ");
                String productName = scanner.nextLine();
                System.out.print("No of items   : ");
                int noOfItems = scanner.nextInt();
                System.out.print("Product price : ");
                float price = scanner.nextFloat();
                scanner.nextLine();
                switch (itemType) {
                    case "e" -> {
                        System.out.print("Enter brand : ");
                        String brand = scanner.nextLine();
                        System.out.print("Enter warranty period(weeks) : ");
                        String warrantyPeriod = scanner.next();
                        product = new Electronics(productId,
                                                  productName,
                                                  noOfItems,
                                                  price,
                                                  brand,
                                                  warrantyPeriod);
                    }
                    case "c" -> {
                        System.out.print("Color : ");
                        String color = scanner.nextLine();
                        System.out.print("Size : ");
                        float size = scanner.nextFloat();
                        product = new Clothing(productId,
                                               productName,
                                               noOfItems,
                                               price,
                                               size,
                                               color);
                    }
                    default -> {
                        System.out.println("!!! You have selected a wrong input. Please enter again");
                        continue;
                    }
                }
                System.out.println("Item details entered successfully");
                break;
            }
        } catch (InputMismatchException e) {
            e.printStackTrace();
            System.out.println("!!! Invalid input");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("!!! Error");
        }
        return product;
    }

    public void getStockChangeData() {
        System.out.print("Product id    : ");
        scanner.nextLine();
        String productId = scanner.nextLine();

        List<Product> products = ShopData.getProducts();
        Product product = ShopUtil.getProduct(productId, products);
        if (product == null) {
            System.out.println("No such product found!!!");
            return;
        }

        System.out.print("Quantity add to stock : ");
        int quantity = scanner.nextInt();
        shoppingManager.addToStock(products, product, quantity);
    }

    public void loadDataController() {
        shoppingManager.loadData();
    }
}
