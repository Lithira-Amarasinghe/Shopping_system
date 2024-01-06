package util;

import model.Clothing;
import model.Electronics;
import model.Product;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ShopData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@ExtendWith(MockitoExtension.class)
public class ShopUtilTest {
    List<Product> products = new ArrayList<>();
    @BeforeEach
    void setProducts(){
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
        products.add(product1);
        products.add(product2);
        products.add(product3);
    }

    @Test
    public void getProduct() {

        Product product = ShopUtil.getProduct("003", products);

        assertEquals("003", product.getProductId());
        assertNotEquals("000", product.getProductId());
    }

    @Test
    public void getCurrentUser() {
        // Mocking data for testing
        User user1 = new User("jhon", false);
        User user2 = new User("peter", false);
        User user3 = new User("james", false);

        List<User> userList = Arrays.asList(user1, user2, user3);

        try (MockedStatic<ShopData> shopMockedStatic = Mockito.mockStatic(ShopData.class)) {
            shopMockedStatic.when(() -> ShopData.getCurrentUser())
                    .thenReturn(user2);
            User user = ShopUtil.getCurrentUser(userList);

            assertEquals( user.getUsername(), "peter","User name should be peter");
        }
    }
}
