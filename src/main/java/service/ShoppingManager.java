/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import model.Product;

import java.util.List;

/**
 *
 * @author Lithira
 */
public interface ShoppingManager {
    void displayMenu();
    void addANewProduct(Product product);
    Product deleteProduct(String productId);
    List<Product> printProductList();
    void saveProducts();
    void loadData();
    Product addToStock(List<Product> products, Product product, int quantity);
    void openShop();
}
