/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import model.*;
import repository.ShopData;
import util.ComponentRef;
import util.FileNames;
import util.ShopUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Lithira
 */
public class HomeUI extends JFrame {
    private static String[] columns = {
            "Product ID",
            "Name",
            "Category",
            "Price($)",
            "Info"
    };
    private List<Product> productList = new ArrayList();
    private List<Product> sortedProductList = new ArrayList();
    private List<CartItem> cartItems = new ArrayList<>();

    private JPanel mainPanel = new JPanel();

    private JPanel topPanel = new JPanel();
    private JPanel panel1 = new JPanel();
    private JPanel panel2 = new JPanel();
    private JPanel panel3 = new JPanel();
    private JPanel panel4 = new JPanel();
    private JPanel sortPanel = new JPanel();
    private JLabel lblProductId = new JLabel();
    private JLabel lblProductName = new JLabel();
    private JLabel lblProductCategory = new JLabel();
    private JLabel lblProductSize = new JLabel();
    private JLabel lblProductColour = new JLabel();
    private JLabel lblProductNoOfItemsAvailable = new JLabel();
    private JLabel sizeLabel = new JLabel("Size");
    private JLabel colorLabel = new JLabel("Red");
    private DefaultTableModel model;
    private JTable table = new JTable();
    private JCheckBox sortCheckBox;

    private Component ref;

    private Object[][] data;

    /**
     * Creates new form HomeUI
     */
    public HomeUI() {
        ComponentRef.setHomeRef(this);
        cartItems = null;
        initComponents();
        setVisible(true);
        setTitle("Westminster Shopping Center");
        setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        setSize(1000, 500);


        productList = ShopData.getProducts();
        cartItems = ShopData.getCartItems();
        data = new Object[productList.size()][];

        createTopSection();
        createFirstSection();
        createSortSection();
        createProductListSection();
        createProductDetailsSection();
        createAddToCartSection();

        getContentPane().add(mainPanel);
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
            java.util.logging.Logger.getLogger(HomeUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeUI().setVisible(true);
            }
        });
    }

    public static void repaintHome() {
        repaintHome();
    }

    private void createAddToCartSection() {
        JButton btnAddToCart = new JButton("Add to Shopping Cart");
        panel4.add(btnAddToCart, BorderLayout.NORTH);
        add(panel4);

        // Adding the action listener event to the add to cart button
        btnAddToCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = addProductsToCart();// Add product to the cart. If the item already exists it only change the quantity
                if (i==1 || i == 2) return;
                boolean isDataSaved = ShopData.saveToAFile(cartItems, FileNames.CART_PRODUCTS_FILE);
                if (isDataSaved) { // Check whether the product added to the cart
                    JOptionPane.showMessageDialog(ref,"Product added to cart successfully");
                }
            }
        });
    }

    private int getAddQuantity() {
        int quantity;
        try {
            quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter the quantity"));/* Get the
                quantity that want to add to the cart*/
            if (String.valueOf(quantity) == null) {
                return -1;
            } else if (quantity <= 0) {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
        return quantity;
    }

    private void createTopSection(){
        topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            // This check if there is a user logged in to the system logout
            if(ShopData.getCurrentUser()==null){
                JOptionPane.showMessageDialog(this,"No one logged into the system to logout");
                return;
            }
            // This window ask whether user wants to log out?
            int option = JOptionPane.showConfirmDialog(this,"Do you want logout? ");
            if(option == 0) {
                ShopData.logout();
                JOptionPane.showMessageDialog(this,"Logout success");
            }
        });

        JButton btnShoppingCart = new JButton("Shopping Cart");
        btnShoppingCart.addActionListener(e -> {
            User currentUser = ShopData.getCurrentUser();
            if (currentUser!=null) {
                new ShoppingCart();
            }else{
                new LoginUI();
            }
        });

        topPanel.add(btnLogout);
        topPanel.setPreferredSize(new Dimension(1000, 50));
        topPanel.add(btnShoppingCart);
        add(topPanel);
    }

    private void createFirstSection() {
        panel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("Select Product Category");
        label.setSize(20,10);
        JPanel tempPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Set the preferred size of the panel (width in this case)
        tempPanel.setPreferredSize(new Dimension(200, 30));
        tempPanel.add(label);

        panel1.add(tempPanel);

        String[] labels = {"All", "Electronics", "Clothing"};
        JComboBox<String> comboBox = new JComboBox<>(labels);

        comboBox.addActionListener(e -> {
            productList = ShopData.getProducts();
            switch ((String) comboBox.getSelectedItem()) {
                case "Electronics" -> {
                    productList = productList.stream()
                            .filter(product -> product instanceof Electronics)
                            .collect(Collectors.toList());
                }
                case "Clothing" -> {
                    productList = productList.stream()
                            .filter(product -> product instanceof Clothing)
                            .collect(Collectors.toList());
                }
            }
            modifyTableData();
        });
        comboBox.setSize(50, 50);
        panel1.add(comboBox);
        add(panel1);
    }

    private void createSortSection() {
        sortPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        sortCheckBox = new JCheckBox("Sort");
        sortPanel.add(sortCheckBox);
        sortCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    sortCheckBox.setText("Sorted");
                    modifyTableData();
                } else {
                    sortCheckBox.setText("Sort");
                    modifyTableData();
                }
            }
        });
        add(sortPanel);
    }

    /* This method arrage the data that used to add to the products table*/
    public void arrangeTableData(List<Product> list) {
        data = new Object[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            Product product = list.get(i);
            if (product instanceof Electronics electronics) {
                data[i] = new Object[]{
                        electronics.getProductId(),
                        electronics.getProductName(),
                        "Electronics",
                        electronics.getPrice(),
                        (electronics.getBrand() + ", " + electronics.getWarrantyPeriod() + " weeks warranty")
                };
            } else if (product instanceof Clothing clothing) {
                data[i] = new Object[]{
                        clothing.getProductId(),
                        clothing.getProductName(),
                        "Clothing",
                        clothing.getPrice(),
                        (clothing.getSize() + ", " + clothing.getColor()),
                };
            }
        }
    }

    public void modifyTableData() {
        if (sortCheckBox.isSelected()) {
            sortedProductList = productList.stream()
                    .sorted(Comparator.comparing(product -> product.getProductName()))
                    .collect(Collectors.toList());
            arrangeTableData(sortedProductList);
        } else {
            arrangeTableData(productList);
        }
        model.setDataVector(data, columns);
    }

    private void createProductListSection() {
        panel2.setLayout(new GridLayout(1, 2));
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column){
                return false; // Prevent editing in all cells in the table but it is clickable
            }
        };
        table.setModel(model);

        table = new JTable(model);
        modifyTableData();

        // Change the background color to red of the items less than 3 in the products table
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = null;
                try {
                    component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    String productId = (String) table.getValueAt(row, 0);
                    var product = ShopUtil.getProduct(productId, productList);

                    // Change the background color of the row based on a condition (e.g., even row number)
                    if (product.getNoOfItemsAvailable() < 3) {
                        component.setBackground(Color.RED);
                    } else {
                        component.setBackground(table.getBackground());
                    }
                } catch (Exception e) {
                    e.getMessage();
                } finally {
                    return component;
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow selecting only one row at a time
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Retrieve data from the selected row
                    Object productId = table.getValueAt(selectedRow, 0);
                    showSelectedItem((String) productId);
                }
            }
        });
        panel2.add(scrollPane);
        add(panel2);

    }

    private void createProductDetailsSection() {
        panel3.setLayout(new GridLayout(7, 1));
        panel3.add(new JLabel("Selected Product -"));
        panel3.add(new JLabel("Details"));
        panel3.add(new JLabel("Product ID"));
        panel3.add(lblProductId);
        panel3.add(new JLabel("Product Category"));
        panel3.add(lblProductCategory);
        panel3.add(new JLabel("Product Name"));
        panel3.add(lblProductName);
        panel3.add(sizeLabel);
        panel3.add(lblProductSize);
        panel3.add(colorLabel);
        panel3.add(lblProductColour);
        panel3.add(new JLabel("Items available"));
        panel3.add(lblProductNoOfItemsAvailable);
        add(panel3);
    }

    private void showSelectedItem(String productId) {
        Product product = productList.stream()
                .filter(x -> x.getProductId().equals(productId))
                .findFirst()
                .get();
        lblProductId.setText(product.getProductId());
        lblProductName.setText(product.getProductName());
        lblProductCategory.setText(product.getClass().getSimpleName());
        lblProductNoOfItemsAvailable.setText(String.valueOf(product.getNoOfItemsAvailable()));
        // Check the Class of the instance and set the value in the UI according to the instance
        if (product instanceof Electronics) {
            sizeLabel.setText("Brand");
            colorLabel.setText("Warranty period (week)");
            lblProductSize.setText(((Electronics) product).getBrand());
            lblProductColour.setText(((Electronics) product).getWarrantyPeriod());

        } else if (product instanceof Clothing) {
            sizeLabel.setText("Size");
            colorLabel.setText("Color");
            lblProductColour.setText(((Clothing) product).getColor());
            lblProductSize.setText(((Clothing) product).getSize() + "");
        }
    }

    private int addProductsToCart() {
        cartItems = ShopData.getCartItems();
        String productId = lblProductId.getText();

        int quantity = getAddQuantity();
        if (quantity == -1) {
            JOptionPane.showMessageDialog(this, "Quantity is invalid !!!");
            return 1;
        }
        Product product = productList.stream()
                .filter(x -> x.getProductId().equals(productId))
                .findFirst()
                .get();

        Optional<CartItem> first = cartItems.stream()
                .filter(c -> c.getProductId().equals(productId))
                .findFirst();
        if(first.isPresent()){
            CartItem cartItem = first.get();
            if(product.getNoOfItemsAvailable() < (quantity + cartItem.getQuantity())) {
                JOptionPane.showMessageDialog(this, "Maximum quantity available is " + product.getNoOfItemsAvailable());
                return 2;
            }
        }
        if (product.getNoOfItemsAvailable() < quantity) {
            JOptionPane.showMessageDialog(this, "Maximum quantity available is " + product.getNoOfItemsAvailable());
            return 2;
        }

        try {
            CartItem cartItems = this.cartItems.stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst()
                    .get();
            if (cartItems != null) {
                //Check to see whether the product already exists in the cart
                cartItems.addQuantity(quantity, product.getPrice());
                return 3;
            }
        } catch (Exception e) {
        }
        // Add the product as a new item to cart because the product isn't in the cart
        CartItem cartItem = new CartItem(
                product.getProductId(),
                product.getProductName(),
                quantity,
                quantity * product.getPrice()
        );
        cartItems.add(cartItem);
        return 0;
    }

    public TableModel getTableModel(){
        return model;
    }

    public String[] getColumns(){
        return columns;
    }

    public Object[][] getData(){
        return data;
    }

    public Component getHomeRef(){
        return ref;
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
