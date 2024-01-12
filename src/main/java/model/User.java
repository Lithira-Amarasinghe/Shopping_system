package model;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
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
