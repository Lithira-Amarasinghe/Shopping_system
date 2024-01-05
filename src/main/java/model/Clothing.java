/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * @author Lithira
 */
public class Clothing extends Product {
    private float size;
    private String color;



    public Clothing(
            String productId,
            String productName,
            int noOfItemsAvailable,
            float price,
            float size,
            String color) {
        super(productId, productName, noOfItemsAvailable, price);
        this.size = size;
        this.color = color;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Clothing{" +
                "size=" + size +
                ", color='" + color + '\'' +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", noOfItemsAvailable=" + noOfItemsAvailable +
                ", price=" + price +
                '}';
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public void printDetails() {
        System.out.println("Product id : " + productId);
        System.out.println("Product name : " + productName);
        System.out.println("No of available items : " + noOfItemsAvailable);
        System.out.println("Price : " + price);
        System.out.println("Size : " + size);
        System.out.println("Color  : " + color);
    }
}
