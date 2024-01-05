/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author Lithira
 */

public abstract class Product implements Serializable {
    protected String productId;
    protected String productName;
    protected int noOfItemsAvailable;
    protected float price;

    Product(){

    }

    Product(
            String productId,
            String productName,
            int noOfItemsAvailable,
            float price){
        this.productId = productId;
        this.productName = productName;
        this.noOfItemsAvailable = noOfItemsAvailable;
        this.price = price;
    }

    public abstract void printDetails();

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getNoOfItemsAvailable() {
        return noOfItemsAvailable;
    }

    public void setNoOfItemsAvailable(int noOfItemsAvailable) {
        this.noOfItemsAvailable = noOfItemsAvailable;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }


    public void purchase(int quantity) {
        noOfItemsAvailable -= quantity;
    }
    public void addToStock(int quantity){
        noOfItemsAvailable = noOfItemsAvailable + quantity;

    }
}
