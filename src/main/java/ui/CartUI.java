/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import model.*;
import repository.ShopData;
import util.FileNames;
import util.ShopUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

/**
 * @author Lithira
 */
public class CartUI extends JFrame {
    List<Product> productList = ShopData.getProducts();
    List<CartItem> cartItems;
    List<User> users = ShopData.getUsers();;

    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel(new GridLayout(1, 2));
    JPanel panel3 = new JPanel(new GridLayout(1, 2));

    JPanel summaryLeftPanel = new JPanel(new GridLayout(4, 1));
    JPanel summaryRightPanel = new JPanel(new GridLayout(4, 1));

    JLabel lblTotalPrice = new JLabel();
    JLabel lblDiscountTenPercent = new JLabel();
    JLabel lblDiscountTwentyPercent = new JLabel();
    JLabel lblGrandTotal = new JLabel();

    private int noOfElectronics;
    private int noOfCloths;

    private float totalPrice = 0;
    private float firstPurchaseDiscount;
    private float threeItemsDiscount;

    HomeUI homeUI;

    /**
     * Creates new form CatUI
     * @param homeUI
     */
    public CartUI(HomeUI homeUI) {
        this.homeUI = homeUI;
        // Load the cart items. If the cart is empty, Cart window is not open to the user
        cartItems = ShopData.getCartItems();
        if(cartItems.isEmpty()){
            JOptionPane.showMessageDialog(this,"Cart is empty!!!");
            return;
        }

        initComponents();
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 1));  // Set the layout to GridLayout
        setTitle("Shopping cart");  // Set the title of the UI
        setSize(800, 600);  // Set the dimensions of the UI
        createCartItemsTable();  // Call the method to the add the cart items table to UI
        createCartSummary();  // Create the cart summary
        createLastSection(); // Create the purchase items section

        add(panel1);     // add the JPanels to the UI
        add(panel2);
        add(panel3);

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
            java.util.logging.Logger.getLogger(CartUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CartUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CartUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CartUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CartUI(new HomeUI()).setVisible(true);
            }
        });
    }

    private void createLastSection() {
        List<Product> products = ShopData.getProducts();
        JButton btnPurchase = new JButton("Purchase");
        btnPurchase.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "Do you want to finish checkout?");
            if (result == 0) {
                cartItems.stream()
                        .forEach(cartItem -> {
                            Product product = ShopUtil.getProduct(cartItem.getProductId(),products);
                            product.purchase(cartItem.getQuantity());
                        });
                cartItems.clear();
                ShopData.saveToAFile(cartItems, FileNames.CART_PRODUCTS_FILE);
                ShopData.saveToAFile(products, FileNames.PRODUCTS_FILE);
                ShopUtil.getCurrentUser(users).purchased();
                ShopData.saveToAFile(users,FileNames.USERS_FILE);

                JOptionPane.showMessageDialog(this, "Checkout completed");
                homeUI.repaint();
                this.dispose();
            }
        });
        panel3.add(BorderLayout.CENTER, btnPurchase);
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



        Optional cartOptional =  cartItems.stream()
                .map(product -> product.getTotalPrice())
                .reduce((x, y) -> x + y);
        if(!cartOptional.isPresent()) return;
        float totalPrice = (float) cartOptional.get();
        float tenPercentDiscount = 0;
        if(!ShopUtil.getCurrentUser(users).isPurchased()){
             tenPercentDiscount = (float) (totalPrice * 0.1);
        }
        float twentyPercentDiscount = noOfCloths >= 3 || noOfElectronics >= 3 ? (float) (totalPrice * 0.2) : 0;
//                getTwentyPercentDiscount(totalPrice);

        lblTotalPrice.setText(String.valueOf(totalPrice));
        lblDiscountTenPercent.setText(String.valueOf(tenPercentDiscount));
        lblDiscountTwentyPercent.setText(String.valueOf(twentyPercentDiscount));
        lblGrandTotal.setText(String.valueOf(totalPrice - tenPercentDiscount - twentyPercentDiscount));
    }

//    private float getTwentyPercentDiscount(float totalPrice) {
//        int = noOfElectronics
//        cartItems.size();
//        return 0;
//    }

    private void createCartItemsTable() {
        List<Product> products = ShopData.getProducts();

        String[] columns = {
                "Product",
                "Quantity",
                "Price($)",

        };

//        Object[][] data = {
//                {"A001", "TV", "Electronics"},
//                {"A203", "Dishwasher", "Electronics"}
//        };


        Object[][] data = new Object[cartItems.size()][];
        for (int i = 0; i < cartItems.size(); i++) {
            Product product = ShopUtil.getProduct(cartItems.get(i).getProductId(),products);
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
                noOfCloths+= cartItem.getQuantity();

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

        panel1.setLayout(new GridLayout(1, 2));
        DefaultTableModel model = new DefaultTableModel(data, columns);  // Create the table model for the JTable

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(500, 300);
        panel1.add(scrollPane);
        add(panel1);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
