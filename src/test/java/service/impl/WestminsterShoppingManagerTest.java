package service.impl;

import model.Clothing;
import model.Electronics;
import model.Product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ShopData;
import service.ShoppingManager;
import util.FileNames;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

//@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class WestminsterShoppingManagerTest {

//    @Mock
//    ShopData shopData;

    @InjectMocks
    ShoppingManager shoppingManager = new WestminsterShoppingManager();


    @Test
    public void addANewProduct() {
    }

    @Test
    public void deleteAProduct() {
    }

    @Test
    void printProductList() {
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

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        try (MockedStatic<ShopData> shopMockedStatic = mockStatic(ShopData.class)) {
            shopMockedStatic.when(() -> ShopData.getProducts())
                    .thenReturn(products);

            List<Product> resultProducts = shoppingManager.printProductList();

            shopMockedStatic.verify(()->ShopData.getProducts(),times(1));

            assertEquals(resultProducts.size(), products.size(),"Product list should contain 2 items");

        }
    }

    @Test
    void saveProductsCallsTest() {
        var shoppingManager = mock(WestminsterShoppingManager.class);

        doNothing().when(shoppingManager).saveProducts();
        shoppingManager.saveProducts();

        verify(shoppingManager, times(1)).saveProducts();

    }

    @Test
    void saveProductsTest() {

        var shoppingManager = new WestminsterShoppingManager();

        var product1 = new Electronics("001",
                                       "Keyboard",
                                       10,
                                       3000,
                                       "Dell",
                                       "50");
        var product2 = new Electronics("001",
                                      "Keyboard",
                                      12,
                                      3000,
                                      "Dell",
                                      "50");
        var products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        var shopProducts = ShopData.getProducts();

        try (MockedStatic<ShopData> shopDataMockedStatic = Mockito.mockStatic(ShopData.class)) {
            shopDataMockedStatic.when(() -> ShopData.saveToAFile(any(List.class) , any(FileNames.class)))
                    .thenReturn(true);
            shopDataMockedStatic.verifyNoInteractions();

            shoppingManager.saveProducts();
            ShopData mock = mock(ShopData.class);
            shopDataMockedStatic.verify(
                    ()->ShopData.saveToAFile(any(List.class), any(FileNames.class))
                    ,times(1));

//            ShopData.saveToAFile(products, FileNames.TEST_PRODUCTS_FILE);
        }

    }

    @Test
    void loadData() {
        //Mocking the ShopData
        try(MockedStatic<ShopData> mockedShopData = Mockito.mockStatic(ShopData.class)){
            //Arrange
            List<Product> products = new ArrayList<>();
            // Stubbing the getProducts method of ShopData
            mockedShopData.when(()->ShopData.getProducts()).thenReturn(products);

            //Act
            shoppingManager.loadData();

            mockedShopData.verify(()->ShopData.getProducts(), times(1));
        }
    }

    @Test
    void addToStock() {
        try(MockedStatic<ShopData> mockedShopData = Mockito.mockStatic(ShopData.class)){
            mockedShopData.when(
                    ()->ShopData.saveToAFile(any(List.class),any(FileNames.class)))
                    .thenReturn(true);

            var product1 = new Electronics("001","Keyboard",
                                           10,3000,"Dell","50");
            var product2 = new Electronics("001","Keyboard",
                                           12,3000,"Dell","50");
            List<Product> products = new ArrayList<>();
            products.add(product1);
            products.add(product2);

            Product resultProduct = shoppingManager.addToStock(products, product2, 5);

            mockedShopData.verify(()-> ShopData.saveToAFile(any(List.class),any(FileNames.class)),
                                  times(1));

            assertEquals(17,resultProduct.getNoOfItemsAvailable(), "Product should have 17 items");

        }
    }
}
