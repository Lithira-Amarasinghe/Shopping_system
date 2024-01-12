package model;

import java.io.Serializable;
// Implements the Serializable to use serialization to save users data
/* Setter for username if not included because after signup user cannot change the username */
public class User implements Serializable {
    private String username;
    private String password;
    // Used to check if a user purchased an item from shop or not previously
    private boolean isPurchased;

    public String getPassword() {
        return password;
    }

    public User() { }

    public User(String username, boolean isPurchased) {
        this.username = username;
        this.isPurchased = isPurchased;
    }

    public User(String username, String password,boolean isPurchased) {
        this.username = username;
        this.password = password;
        this.isPurchased = isPurchased;
    }

    public String getUsername() {
        return username;
    }

   public boolean isPurchased() {
        return isPurchased;
    }

    public void purchased() {
        isPurchased = true;
    }

    /* Used to check equality of two users based on the username*/
    @Override
    public boolean equals(Object user){
        return this.username.equals(((User)user).username);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", isPurchased=" + isPurchased +
                '}';
    }
}
