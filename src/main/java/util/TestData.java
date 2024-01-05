package util;

import model.Clothing;
import model.Electronics;
import model.Product;

import java.util.ArrayList;
import java.util.List;

public class TestData {
    public static List<Product> getProducts(){
        var product1 = new Electronics("001",
                                       "Keyboard",
                                       12,
                                       2500,
                                       "ROG",
                                       "50");
        var product2 = new Clothing("002",
                                    "T-shirt",
                                    5,
                                    3500,
                                    15,
                                    "RED");
        var product3 = new Electronics("003",
                                       "Mouse",
                                       12,
                                       2000,
                                       "DELL",
                                       "60");
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        return products;
    }
}
