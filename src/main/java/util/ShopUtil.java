package util;

import model.Product;

import model.User;
import repository.ShopData;

import java.util.List;
import java.util.Optional;

public class ShopUtil {
    public static Product getProduct(String productId, List<Product> products) {
        Optional<Product> first = products.stream()
                .filter(product -> product.getProductId().equals(productId))
                .findFirst();
        if(first.isPresent()) return first.get();
        return null;
    }

    public static User getCurrentUser(List<User> users) {
        User currentUser = ShopData.getCurrentUser();
        Optional<User> first = users.stream()
                .filter(user -> user.getUsername().equals(currentUser.getUsername()))
                .findFirst();
        return first.get();
    }
}
