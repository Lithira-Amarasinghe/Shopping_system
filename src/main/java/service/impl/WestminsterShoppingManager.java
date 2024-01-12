/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;


import model.Clothing;
import model.Electronics;
import model.Product;
import repository.ShopData;
import service.ShoppingManager;
import ui.HomeUI;
import util.FileNames;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Lithira
 */
public class WestminsterShoppingManager implements ShoppingManager {
    private Scanner scanner = new Scanner(System.in);
    private int option;
    private List<Product> products = new ArrayList<>();

    public WestminsterShoppingManager(){
        products = ShopData.getProducts();
    }

    @Override
    public void displayMenu() {
        System.out.println(" [1]  - Add a new product");
        System.out.println(" [2]  - Delete a product");
        System.out.println(" [3]  - Print products list");
        System.out.println(" [4]  - Save to a file");
        System.out.println(" [5]  - Open shop");
        System.out.println(" [6]  - Add out-of stock items");
        System.out.println(" [-1] - Exit ");
    }

    @Override
    public void addANewProduct(Product product) {
        products.add(product);
        ShopData.saveToAFile(products,FileNames.PRODUCTS_FILE);
//        try {
//            ObjectOutputStream oos = new ObjectOutputStream(
//                    new FileOutputStream(FileNames.PRODUCTS_FILE.getValue()));
//            oos.writeObject(products);
//        } catch (InputMismatchException | FileNotFoundException ex) {     // InputMismatchException thrown when a incompatible data inputs
//            System.out.print("You have entered a wrong data type...\n Do you want to add item again (y/n) ? ");
//            String input = scanner.next();
//            if (input.equals("y") || input.toLowerCase().equals("yes")) {
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public Product deleteAProduct(String productId) {
        products = ShopData.getProducts();
        Optional<Product> productOptional = products.stream()
                .filter(x -> x.getProductId().equals(productId))
                .findFirst();
        if (productOptional.isEmpty()) {
            System.out.println("\nPlease check the product ID again. It doesn't exist...\n");
            return null;
        }
        Product productToDelete = productOptional.get();
        products.remove(productToDelete);
        System.out.println("Product successfully deleted");
        saveProducts();
        return productToDelete;
    }

    @Override
    public List<Product> printProductList() {
        // Assign a sorted product list
        products = ShopData.getProducts().stream()
                .sorted(Comparator.comparing(Product::getProductId))
                .collect(Collectors.toList());

        System.out.println(">>>--- Products in the shop ---<<<");
        System.out.println("No of products : " + products.size());
        int onOfElectronics = 0;
        int onOfCloths = 0;
        for (Product product : products) {
            if (product instanceof Electronics) {
                onOfElectronics++;
            } else if (product instanceof Clothing) {
                onOfCloths++;
            }
        }
        System.out.println("No of Electronics : " + onOfElectronics);
        System.out.println("No of Cloths : " + onOfCloths + "\n");
        for (Product product : products) {
            product.printDetails();
            System.out.println("\n");
        }
        return products;
    }

    @Override
    public void saveProducts() {
        System.out.println(System.identityHashCode(products));
        System.out.println(products);
        ShopData.saveToAFile(products, FileNames.PRODUCTS_FILE);
    }

    @Override
    public <T> boolean testShop(T num) {
        return ShopData.testCall(num);
    }

    @Override
    public void loadData() {
        products = ShopData.getProducts();
    }

    @Override
    public Product addToStock(List<Product> products, Product product, int quantity) {
        if(product==null) {
            System.out.println("No such product found!!!");
            return null;
        }
        product.addToStock(quantity);
        ShopData.saveToAFile(products,FileNames.PRODUCTS_FILE);

        return product;
    }

    @Override
    public void openShop() {
        new HomeUI();
    }


}
