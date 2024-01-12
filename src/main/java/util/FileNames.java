package util;

/* This enum is created to store the file names that are used to save/retrieve data */
public enum FileNames {
    PRODUCTS_FILE("product.ser"),
    TEST_PRODUCTS_FILE("test_product.ser"),
    CART_PRODUCTS_FILE("cart_items.ser"),
    USERS_FILE("users.ser");

    private final String value;

    FileNames( String value) {
        this.value = value;
    }

    // This method used to get the actual file name
    public String getValue() {
        return value;
    }

}
