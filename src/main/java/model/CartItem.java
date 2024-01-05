package model;

import java.io.Serializable;

public class CartItem implements Serializable {
    protected String productId;
    protected String productName;
    protected int quantity;
    protected float totalPrice;

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public CartItem(String productId, String productName, int quantity, float totalPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public CartItem() {
    }

    public void printDetails() {

    }

    public void addQuantity(float quantity, float unitPrice) {
        this.quantity+= quantity;
        totalPrice = this.quantity * unitPrice;
    }
}
