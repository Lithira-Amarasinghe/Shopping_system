package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import service.impl.WestminsterShoppingManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingManagerControllerTest {

    @Mock
    WestminsterShoppingManager westminsterShoppingManager;

    @Spy
    @InjectMocks
    ShoppingManagerController shoppingManagerController;

    @Test
    void getOption() {

    }

    @Test
    void startManagerWork() {
        doNothing().when(shoppingManagerController).getMainOption();
        doNothing().when(shoppingManagerController).loadDataController();
        shoppingManagerController.startManagerWork();

        verify(shoppingManagerController,times(1)).startManagerWork();
        verify(shoppingManagerController).getMainOption();
        verify(shoppingManagerController).loadDataController();
    }

    @Test
    void getProductId() throws IOException {
        try (ByteArrayInputStream input = new ByteArrayInputStream("10\n20".getBytes())) {
            System.setIn(input);
            Scanner scanner = new Scanner(System.in);
            String productId = shoppingManagerController.getProductId();
            assertEquals("001", productId, "Product Id should be 001");
        }
    }

    @Test
    void getMainOption() {
    }

    @Test
    void getNewProductDetails() {
    }

    @Test
    void getStockChangeData() {
    }

    @Test
    void loadDataController() {
    }
}
