package ui;

import model.User;
import repository.ShopData;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class LoginUI extends JFrame{
    // Create three text fields
    JTextField txtUsername = new JTextField();
    JPasswordField txtPassword = new JPasswordField();

    // Main Controllers in the signup form
    JButton btnLogin = new JButton("login");
    JButton btnSignup = new JButton("signup");
    JButton btnCancel = new JButton("cancel");

    JPanel pane1 = new JPanel(new FlowLayout());
    JPanel pane2 = new JPanel(new FlowLayout());
    JPanel controllerPanel = new JPanel(new FlowLayout());

    public LoginUI() {
        setLayout(new GridLayout(3,1));
        txtUsername.setColumns(20);
        txtPassword.setColumns(20);

        pane1.setLayout(new GridLayout(1, 2));
        pane2.setLayout(new GridLayout(1, 2));
        controllerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        pane1.add(new JLabel("Username"));
        pane1.add(txtUsername);

        pane2.add(new JLabel("Password"));
        pane2.add(txtPassword);

        createControllersSection();

        setSize(400, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Login");
        setVisible(true);

        add(pane1);
        add(pane2);
        add(controllerPanel);
    }

    private void createControllersSection() {
        btnLogin.addActionListener(e -> {
            // Get the text in the text fields
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            // Get the user details from the database
            List<User> users = ShopData.getUsers();
            if(users==null) return;
            Optional<User> first = users.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findFirst();

            if(first.isEmpty()){
                JOptionPane.showMessageDialog(this,"Username doesn't exists. Please try again.");
                return;
            }

            User user = first.get();

            if(!user.getPassword().equals(password)){
                //Show incorrect password message to user
                JOptionPane.showMessageDialog(this,"Incorrect password");
            }else{
                //Show login success message to user
                JOptionPane.showMessageDialog(this,"Login successful");
                ShopData.setCurrentUser(user);
                dispose();
                new ShoppingCart();
            }
        });

        btnCancel.addActionListener(e -> {
            dispose();
        });

        btnSignup.addActionListener(e -> {
            dispose();
            new SignupUI();
        });

        // Adding the buttons to controller panel
        controllerPanel.add(btnSignup);
        controllerPanel.add(btnCancel);
        controllerPanel.add(btnLogin);

    }
}
