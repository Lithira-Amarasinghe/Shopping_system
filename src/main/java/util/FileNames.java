package util;

public enum FileNames {
    PRODUCTS_FILE("product.ser"),
    TEST_PRODUCTS_FILE("test_product.ser"),
    CART_PRODUCTS_FILE("cart_items.ser"),
    USERS_FILE("users.ser");

    private final String value;

    FileNames( String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
