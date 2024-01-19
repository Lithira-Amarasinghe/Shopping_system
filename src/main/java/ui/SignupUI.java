package ui;

import model.User;
import repository.ShopData;
import util.FileNames;
import util.ShopUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class SignupUI extends JFrame {
    // Create three text fields
    private JTextField txtUsername = new JTextField();
    private JPasswordField txtPassword = new JPasswordField();
    private JPasswordField txtConfirmationPassword = new JPasswordField();

    private JButton btnSignup = new JButton("Signup");
    private JButton btnCancel = new JButton("Cancel");

    SignupUI() {
        setLayout(new GridLayout(4, 2));

        txtUsername.setColumns(20);
        txtPassword.setColumns(20);
        txtConfirmationPassword.setColumns(20);

        add(new JLabel("Username : "));
        add(txtUsername);

        add(new JLabel("Password : "));
        add(txtPassword);

        add(new JLabel("Confirm password : "));
        add(txtConfirmationPassword);

        createControllersSection();

        // Set the frame properties
        setSize(400, 200);
        setTitle("Signup");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void createControllersSection() {
        btnSignup.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            String confirmationPassword = txtConfirmationPassword.getText();
            List<User> users = ShopData.getUsers();

            String existingUsername = null;

            Optional<User> first = users.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findFirst();


            if(first.isPresent()) {
                existingUsername = first.get().getUsername();
            }

            if(username.equals(existingUsername)){
                JOptionPane.showMessageDialog(this,username + " already exists. Try another");
            }else if(!password.equals(confirmationPassword)){
                JOptionPane.showMessageDialog(this,"Password and confirmation password" +
                        " does not match. Please check it again");
            }else{
                users.add(new User(txtUsername.getText(),txtPassword.getText(),false));
                JOptionPane.showMessageDialog(this,"Signup successful");
                ShopData.saveToAFile(users, FileNames.USERS_FILE);
                dispose();
                new LoginUI();
            }
        });

        btnCancel.addActionListener(e -> {
            dispose();
        });

        add(btnCancel);
        add(btnSignup);
    }
}
