package repository;

import model.CartItem;
import model.Product;
import model.User;
import util.FileNames;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ShopData {
    private static List<Product> products;
    private static List<CartItem> cartItems;
    private static List<User> users;
    private static User currentUser;

    static{
        getProducts();
        getCartItems();
        getUsers();
    }

    public static User getCurrentUser(){
        return currentUser;
    }

    public static<T> boolean testCall(T num)  {
        System.out.println("TestCall method runniing");
        if((int)num==10) try {
            throw new Exception();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void setCurrentUser(User currentUser){
        ShopData.currentUser = currentUser;
        try {
            boolean userExists = users.stream()
                    .anyMatch(user -> user.getUsername().equals(currentUser.getUsername()));
            if (!userExists) {
                users.add(currentUser);
                saveToAFile(users, FileNames.USERS_FILE);
            }
        }catch (Exception e){
            System.out.println(e);
            System.out.println("Error in saving user!!!");
        }
    }
    public static List<Product> getProducts(){
        products = loadData(FileNames.PRODUCTS_FILE, ArrayList.class);
        return products;
    }
    public static List<CartItem> getCartItems(){
        cartItems = loadData(FileNames.CART_PRODUCTS_FILE, ArrayList.class);
        return cartItems;
    }

    public static List<User> getUsers(){
        users = loadData(FileNames.USERS_FILE, ArrayList.class);
        return users;
    }

    public static <T> T loadData(FileNames fileNames, Class<T> expectedType) {
        T data = null;
        try {
            ObjectInputStream inputStream;
            inputStream = new ObjectInputStream(
                    new FileInputStream(fileNames.getValue()));
            data = (T)inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (data == null) {
                try {
                    data = expectedType.getDeclaredConstructor().newInstance();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            return (T)data;
        }
    }

    public static <T> boolean saveToAFile(T ref, FileNames fileNames) {
        System.out.println("Produce saving...");
        try (ObjectOutputStream outputStream =
                     new ObjectOutputStream(
                             new FileOutputStream(fileNames.getValue()))) {
            outputStream.writeObject(ref);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
