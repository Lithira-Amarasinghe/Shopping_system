package service.impl;

import model.Clothing;
import model.Electronics;
import model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ShopData;
import service.ShoppingManager;
import util.FileNames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WestminsterShoppingManagerTest {

    @InjectMocks
    ShoppingManager shoppingManager = new WestminsterShoppingManager();

    @Spy
    WestminsterShoppingManager spyShoppingManager;

    List<Product> products = new ArrayList<>();
    Product product;

    @BeforeEach
    public void createProductsList(){
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
                                    "earphone",
                                    9,
                                    6000,
                                    "JBL",
                                    "40");

        products.add(product1);
        products.add(product2);
        products.add(product3);
    }

    @BeforeEach
    void setProduct(){
        product = new Electronics("001",
                                      "Keyboard",
                                      12,
                                      2500,
                                      "ROG",
                                      "50");
    }

    @Test
    void addANewProduct() {
        try(MockedStatic<ShopData> mockedShopData = Mockito.mockStatic(ShopData.class)){
            mockedShopData.when(()-> ShopData.saveToAFile(any(List.class), any(FileNames.class)))
                    .thenReturn(true);
            System.out.println();
            shoppingManager.addANewProduct(product);
            mockedShopData.verify(() -> ShopData.saveToAFile(any(List.class), any(FileNames.class)), times(1));
        }
    }

    @Test
    void deleteAProduct() {
        try(MockedStatic<ShopData> mockedShopData = Mockito.mockStatic(ShopData.class)){
            mockedShopData.when(()->ShopData.getProducts()).thenReturn(products);
            doNothing().when(spyShoppingManager).saveProducts();
            Product deletedProduct = spyShoppingManager.deleteProduct("001");
            mockedShopData.verify(()-> ShopData.getProducts(), times(1));
            verify(spyShoppingManager, times(1)).saveProducts();
            assertEquals("001", deletedProduct.getProductId(), "Deleted product ID should be 001");
        }
    }

    @Test
    void printProductList() {
        try (MockedStatic<ShopData> mockedShopData = mockStatic(ShopData.class)) {
            mockedShopData.when(() -> ShopData.getProducts())
                    .thenReturn(products);
            List<Product> resultProducts = shoppingManager.printProductList();

            mockedShopData.verify(()->ShopData.getProducts(),times(1));

            assertEquals(resultProducts.size(), products.size(),"Product list should contain 3 items");

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
        var shopProducts = ShopData.getProducts();
        Map<Integer,String> map = new HashMap<>();
        map.put(1,"001");
        map.put(2,"002");
        map.put(3,"003");
        try (MockedStatic<ShopData> mockedShopData = Mockito.mockStatic(ShopData.class)) {
            List<Product> mockProducts = new ArrayList<>();
            mockedShopData.when(() -> ShopData.saveToAFile(anyList() , any(FileNames.class)))
                    .thenReturn(true);
            mockedShopData.verifyNoInteractions();

            shoppingManager.saveProducts();

            mockedShopData.verify(
                    ()->ShopData.saveToAFile(anyList(), any(FileNames.class))
                    ,times(1));

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

            Product resultProduct = shoppingManager.addToStock(products, product, 5);

            mockedShopData.verify(()-> ShopData.saveToAFile(any(List.class),any(FileNames.class)),
                                  times(1));

            assertEquals(17,resultProduct.getNoOfItemsAvailable(), "Product should have 17 items");

        }
    }
}
