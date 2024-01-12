/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Lithira
 */
public class Electronics extends Product{

    private String brand;
    private String warrantyPeriod;

    public Electronics() {
    }
    @Override
    public void printDetails() {
        System.out.println("Product id : " + productId);
        System.out.println("Product name : " + productName);
        System.out.println("No of available items : " + noOfItemsAvailable);
        System.out.println("Price : " + price);
        System.out.println("Brand : " + brand);
        System.out.println("Warranty period : " + warrantyPeriod);
    }

    @Override
    public String toString() {
        return "Electronics{" +
                "brand='" + brand + '\'' +
                ", warrentyPeriod=" + warrantyPeriod +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", noOfItemsAvailable=" + noOfItemsAvailable +
                ", price=" + price +
                '}';
    }

    public Electronics(
            String productId,
            String productName,
            int noOfItemsAvailable,
            float price,
            String brand,
            String warrentyPeriod) {
        super(productId, productName, noOfItemsAvailable, price);
        this.brand = brand;
        this.warrantyPeriod = warrentyPeriod;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setWarrantyPeriod(String warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public String getWarrantyPeriod() {
        return warrantyPeriod;
    }

}
