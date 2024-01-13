/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import model.*;
import repository.ShopData;
import util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;

/**
 * @author Lithira
 */
public class ShoppingCart extends JFrame {
    private List<Product> productList = ShopData.getProducts();
    private List<CartItem> cartItems;
    private List<User> users = ShopData.getUsers();

    private JPanel panel1 = new JPanel();
    private JPanel panel2 = new JPanel(new GridLayout(1, 2));
    private JPanel panel3 = new JPanel(new GridLayout(1, 2));

    private JPanel summaryLeftPanel = new JPanel(new GridLayout(4, 1));
    private JPanel summaryRightPanel = new JPanel(new GridLayout(4, 1));

    private JLabel lblTotalPrice = new JLabel();
    private JLabel lblDiscountTenPercent = new JLabel();
    private JLabel lblDiscountTwentyPercent = new JLabel();
    private JLabel lblGrandTotal = new JLabel();
    private HomeUI homeUI;

    private Object[][] data = null;

    private int noOfElectronics;
    private int noOfCloths;

    public ShoppingCart() {
        // Holds the reference of the HomeUI that used to instantiate this cartUI
        this.homeUI = (HomeUI) ComponentRef.getHomeRef();
        initComponents();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ShoppingCart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ShoppingCart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ShoppingCart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShoppingCart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ShoppingCart().setVisible(true);
            }
        });
    }

    private void arrangeTableData() {
        noOfCloths = 0;
        noOfElectronics=0;
        data = new Object[cartItems.size()][];
        for (int i = 0; i < cartItems.size(); i++) {
            Product product = ShopUtil.getProduct(cartItems.get(i).getProductId(), productList);
            CartItem cartItem = cartItems.get(i);
            if (product instanceof Electronics) {
                Electronics electronics = (Electronics) product;
                noOfElectronics += cartItem.getQuantity();
                data[i] = new Object[]{
                        (electronics.getProductId() + " \n" +
                                electronics.getProductName() + " \n" +
                                electronics.getBrand() + ", " +
                                electronics.getWarrantyPeriod()
                        ),
                        cartItem.getQuantity(),
                        cartItem.getTotalPrice()
                };
            } else if (product instanceof Clothing) {
                Clothing clothing = (Clothing) product;
                noOfCloths += cartItem.getQuantity();

                data[i] = new Object[]{
                        (clothing.getProductId() + " \n" +
                                clothing.getProductName() + " \n" +
                                clothing.getSize() + ", " +
                                clothing.getColor()
                        ),
                        cartItem.getQuantity(),
                        cartItem.getTotalPrice()
                };
            }
        }
    }

    private void createCartItemsTable() {
        // Retrieve the products from saved file
        String[] columns = {
                "Product",
                "Quantity",
                "Price($)",
                "Remove",

        };

        arrangeTableData();

        panel1.setLayout(new GridLayout(1, 2));
        DefaultTableModel model = new DefaultTableModel(data, columns);

        JTable table = new JTable();
        table.setModel(model);
        table.setEnabled(false);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());
                if(data.length==0) dispose();
                if (column == 3) {
                    model.getValueAt(row, 0);
                    // Removes the selected item from the cartItems list
                    cartItems.remove(row);
                    ShopData.saveToAFile(cartItems, FileNames.CART_PRODUCTS_FILE);
                    arrangeTableData();
                    model.setDataVector(data, columns);
                } else {
                    // Prevent actions or selection on other columns
                    table.getSelectionModel().clearSelection();
                }
                // Calculate the bill again
                calculateBill();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(500, 150);
        panel1.add(scrollPane);

        table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor());

        add(panel1);
    }

    private void calculateBill() {
        Optional cartOptional = cartItems.stream()
                .map(product -> product.getTotalPrice())
                .reduce((x, y) -> x + y);
        if (!cartOptional.isPresent()) return;
        float totalPrice = (float) cartOptional.get();
        float tenPercentDiscount = 0;
        if (!ShopUtil.getCurrentUser(users).isPurchased()) {
            tenPercentDiscount = (float) (totalPrice * 0.1);
        }
        float twentyPercentDiscount = noOfCloths >= 3 || noOfElectronics >= 3 ? (float) (totalPrice * 0.2) : 0;

        lblTotalPrice.setText(String.valueOf(totalPrice));
        lblDiscountTenPercent.setText(String.valueOf(tenPercentDiscount));
        lblDiscountTwentyPercent.setText(String.valueOf(twentyPercentDiscount));
        lblGrandTotal.setText(String.valueOf(totalPrice - tenPercentDiscount - twentyPercentDiscount));
    }

    private void createCartSummary() {
        panel2.setLayout(new GridLayout(1, 2));

        summaryLeftPanel.add(new JLabel("Total"));
        summaryLeftPanel.add(new JLabel("First purchase discount (10%)"));
        summaryLeftPanel.add(new JLabel("Three items in the same Category Discount (20%)"));
        summaryLeftPanel.add(new JLabel("Final Total"));

        panel2.add(summaryLeftPanel);

        summaryRightPanel.add(lblTotalPrice);
        summaryRightPanel.add(lblDiscountTenPercent);
        summaryRightPanel.add(lblDiscountTwentyPercent);
        summaryRightPanel.add(lblGrandTotal);

        panel2.add(summaryRightPanel);

        calculateBill();

    }

    private void createLastSection() {
        JButton btnPurchase = new JButton("Purchase");
        btnPurchase.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "Do you want to finish checkout?");
            if (result == 0) {
                cartItems.stream()
                        .forEach(cartItem -> {
                            Product product = ShopUtil.getProduct(cartItem.getProductId(), productList);
                            product.purchase(cartItem.getQuantity());
                        });
                cartItems.clear();
                ShopData.saveToAFile(cartItems, FileNames.CART_PRODUCTS_FILE);
                ShopData.saveToAFile(productList, FileNames.PRODUCTS_FILE);
                ShopUtil.getCurrentUser(users).purchased();
                ShopData.saveToAFile(users, FileNames.USERS_FILE);

                JOptionPane.showMessageDialog(this, "Checkout completed");

                // Recreate the HomeUI to update product quantities after purchase
                homeUI.dispose();
                new HomeUI();

                // Close the CartUI after successful purchase
                this.dispose();
            }
        });
        panel3.add(BorderLayout.CENTER, btnPurchase);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        this.homeUI = homeUI;
        // Load the cart items. If the cart is empty, Cart window is not open to the user
        cartItems = ShopData.getCartItems();
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!!!");
            return;
        }

        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));  // Set the layout to GridLayout
        setTitle("Shopping cart");  // Set the title of the UI
        setSize(800, 300);  // Set the dimensions of the UI
        createCartItemsTable();  // Call the method to the add the cart items table to UI
        createCartSummary();  // Create the cart summary
        createLastSection(); // Create the purchase items section

        add(panel1);     // add the JPanels to the UI
        add(panel2);
        add(panel3);
    }
}
