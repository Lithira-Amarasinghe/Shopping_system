package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ui.HomeUI;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopAdminTest {
    @Mock
    ShoppingManagerController shoppingManagerController;

    @InjectMocks
    ShopAdmin shopAdmin;

    @Test
    void main() {
        // Mocking dependencies
        HomeUI mockedHomeUI = Mockito.mock(HomeUI.class);

        //Stub the startManagerWork() of the ShoppingManagerController
        doNothing().when(shoppingManagerController).startManagerWork();

        //Stub the startManagerWork() of the loadDataController
        doNothing().when(shoppingManagerController).loadDataController();

        // Creating ShopAdmin instance

        // Testing the behavior of the ShopAdmin class
        shopAdmin.main(new String[]{"arg-1", "arg-2"});

        // Verifying that loadDataController and startManagerWork methods are called once each
        verify(shoppingManagerController, times(1)).loadDataController();
        verify(shoppingManagerController, times(1)).startManagerWork();
    }
}
