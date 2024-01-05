package service.impl;

import model.Clothing;
import model.Electronics;
import model.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import repository.ShopData;
import util.FileNames;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WestminsterShoppingManagerTest {

    @Test
    public void addANewProduct() {
    }

    @Test
    public void deleteAProduct() {
    }

    @Test
    public void printProductList() {
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
//        var product3 = new Electronics("003",
//                                       "Mouse",
//                                       12,
//                                       2000,
//                                       "DELL",
//                                       "60");
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
//        products.add(product3);

        try (MockedStatic<ShopData> shopMockedStatic = Mockito.mockStatic(ShopData.class)) {
            shopMockedStatic.when(ShopData::getProducts)
                    .thenReturn(products);

            var shoppingManager = new WestminsterShoppingManager();
            List<Product> resultProducts = shoppingManager.printProductList();

            assertEquals("Product list should contain 2 items",resultProducts.size(), products.size());

        }
    }

    @Test
    public void saveProducts() {
//        var product = new Electronics("001",
//                                       "Keyboard",
//                                       12,
//                                       2500,
//                                       "ROG",
//                                       "50");

        var products = new ArrayList<>();
        var shoppingManager = new WestminsterShoppingManager();
        try(MockedStatic<ShopData> shopDataMockedStatic = Mockito.mockStatic(ShopData.class)){
            shopDataMockedStatic.when(()->ShopData.saveToAFile(products, FileNames.TEST_PRODUCTS_FILE));
        }

        shoppingManager.saveProducts();

        assertEquals(1,1);

    }

    @Test
    public void loadData() {
    }

    @Test
    public void addToStock() {
    }
}
