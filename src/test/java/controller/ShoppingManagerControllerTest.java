package controller;

import model.Clothing;
import model.Electronics;
import model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ShopData;
import service.ShoppingManager;
import service.impl.WestminsterShoppingManager;
import util.ShopUtil;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingManagerControllerTest {

    @Mock
    ShoppingManager shoppingManager;

    @Mock
    Scanner mockedScanner;

    @Mock
    private PrintStream mockOut;

    @Captor
    ArgumentCaptor<String> stringCaptor;

    @Captor
    ArgumentCaptor<List> listCaptor;

    @Spy
    @InjectMocks
    ShoppingManagerController shoppingManagerController;

    Product product1;

    static int callCount = 2;

    private List<Product> products=new ArrayList<>();

    @BeforeEach
    void setUpData() {
        //Setting up the data
        product1 = new Electronics("001",
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

    @Test
    void startManagerWork() {
        doNothing().when(shoppingManagerController).getMainOption();
        doNothing().when(shoppingManagerController).loadDataController();
        shoppingManagerController.startManagerWork();

        verify(shoppingManagerController, times(1)).startManagerWork();
        verify(shoppingManagerController).getMainOption();
        verify(shoppingManagerController).loadDataController();
    }

    @Test
    void getOptionTest() {
        // Create a mock input reader
        Mockito.when(mockedScanner.nextInt()).thenReturn(5);
        // Call the method that reads user input
        int result = shoppingManagerController.getOption();
        // Assert the result
        Assertions.assertEquals(5, result, "Result should be 5");
    }

    @Test
    void getOptionExceptionTest() {
        Mockito.when(mockedScanner.nextInt()).thenThrow(new InputMismatchException());

        var result = shoppingManagerController.getOption();
        assertEquals(0, result, "Result should be 0");
    }

    @Test
    void getProductId() {
        when(mockedScanner.nextLine()).thenReturn("002");
        String productId = shoppingManagerController.getProductId();
        assertEquals("002", productId, "Product id should be 002");
    }

    @Test
    void getProductIdExceptionTest() {
        Mockito.when(mockedScanner.nextLine()).thenThrow(new InputMismatchException());
        var productId = shoppingManagerController.getProductId();
        assertNull(productId, "Product id should be null");
    }

    @Test
    void getProductConsolePrintTest() {
        try {
//            new PrintStream(new ByteArrayOutputStream());
//            PrintStream mockOut = Mockito.mock(PrintStream.class);

            System.setOut(mockOut);

            //Call actual method
            shoppingManagerController.getProductId();

            Mockito.verify(mockOut,times(1)).println(stringCaptor.capture());
            assertEquals("Enter the product id to delete : ",stringCaptor.getValue());

        } catch (Exception e) {
        }
    }

    @Test
    void getMainOptionTest01(){
        when(mockedScanner.nextInt()).thenReturn(1,-1);
        when(mockedScanner.nextLine()).thenReturn("null");

//        when(shoppingManagerController.getNewProductDetails()).thenReturn(product1);
        doReturn(product1).when(shoppingManagerController).getNewProductDetails();
        doNothing().when(shoppingManager).addANewProduct(any());

        shoppingManagerController.getMainOption();

        verify(shoppingManager,times(1)).addANewProduct(any(Product.class));
        verify(shoppingManagerController,times(1)).getNewProductDetails();

    }


    @Test
    void getMainOptionInput3Test() {
        callCount=2;
        try(PrintStream printStream = Mockito.mock(PrintStream.class)) {
            //stubbing
            doNothing().when(shoppingManager).displayMenu();

            when(mockedScanner.nextInt()).thenAnswer(invocation -> {
                // Track calls using a counter
                callCount++;
                // Return values based on call count
                if (callCount == 3) {
                    return callCount;
                }
                return -1;
            });
            when(mockedScanner.nextLine()).thenReturn("1");
            when(shoppingManager.printProductList()).thenReturn(products);

            System.setOut(printStream);

            //Call actual method
            shoppingManagerController.getMainOption();

            verify(shoppingManager,times(1)).printProductList();

        } catch (Exception e) {
        }
    }



    @Test
    void getStockChangeDataTest(){
        try(
                MockedStatic mockedShopData = Mockito.mockStatic(ShopData.class);
                MockedStatic mockedShopUtil = Mockito.mockStatic(ShopUtil.class);

        ) {
            doReturn("001").when(mockedScanner).nextLine();
            doReturn(5).when(mockedScanner).nextInt();

            System.setOut(mockOut);

            mockedShopData.when(()-> ShopData.getProducts()).thenReturn(products);
            mockedShopData.when(()-> ShopUtil.getProduct(stringCaptor.capture(),listCaptor.capture())).thenReturn(product1);

            shoppingManagerController.getStockChangeData();

            verify(shoppingManager, times(1)).addToStock(anyList(), any(Product.class), anyInt());

            assertEquals("001", stringCaptor.getValue(),"Product ID should be 001");
            assertEquals(products, listCaptor.getValue());

        }
    }
}
