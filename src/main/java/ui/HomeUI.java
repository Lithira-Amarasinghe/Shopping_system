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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
    List<Product> productList = new ArrayList();
    List<Product> sortedProductList = new ArrayList();
    List<CartItem> cartItems = new ArrayList<>();

    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();
    JPanel panel3 = new JPanel();
    JPanel panel4 = new JPanel();
    JPanel sortPanel = new JPanel();
    JLabel lblProductId = new JLabel();
    JLabel lblProductName = new JLabel();
    JLabel lblProductCategory = new JLabel();
    JLabel lblProductSize = new JLabel();
    JLabel lblProductColour = new JLabel();
    JLabel lblProductNoOfItemsAvailable = new JLabel();
    JLabel sizeLabel = new JLabel("Size");
    JLabel colorLabel = new JLabel("Red");
    DefaultTableModel model;
    JTable table = new JTable();
    JCheckBox sortCheckBox;

    Object[][] data;

    /**
     * Creates new form HomeUI
     */
    public HomeUI() {
//        cartItems = (List<CartItem>) ShopData.loadData(FileNames.CART_PRODUCTS_FILE,Object.class);
        cartItems = null;
        initComponents();
        setVisible(true);
        setTitle("Westminster Shopping Center");
        setLayout(new GridLayout(5, 1));
        setSize(1000, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        productList = ShopData.getProducts();
        cartItems = ShopData.getCartItems();
        data = new Object[productList.size()][];
        createFirstSection();
        createSortSection();
        createProductListSection();
        createProductDetailsSection();
        createAddToCartSection();
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
        btnAddToCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProductsToCart(); // Add product to the cart. If the item already exists it only change the quantity
                boolean isDataSaved = ShopData.saveToAFile(cartItems, FileNames.CART_PRODUCTS_FILE);
                if (isDataSaved) { // Check whether the product added to the cart
                    System.out.println("Product added to cart successfully");
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

    private void createFirstSection() {
        panel1.setLayout(new FlowLayout());
        panel1.add(new JLabel("Select Product Category"));

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

        JButton btnShoppingCart = new JButton("Shopping Cart");
        btnShoppingCart.addActionListener(e -> {
            User currentUser = ShopData.getCurrentUser();
            if (currentUser!=null) {
                new CartUI(this);
            }else{
                new LoginUI();
            }
        });
        panel1.add(btnShoppingCart);
        add(panel1);
    }

    private void createSortSection() {
        sortPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        sortCheckBox = new JCheckBox("Sort");
        sortPanel.add(sortCheckBox);
        sortPanel.setSize(1000, 20);

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

    private void arrangeTableData(List<Product> list) {
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

    private void modifyTableData() {
        if (sortCheckBox.isSelected()) {
            sortedProductList = productList.stream()
                    .sorted(Comparator.comparing(Product::getProductName))
                    .collect(Collectors.toList());
            arrangeTableData(sortedProductList);
        } else {
            arrangeTableData(productList);
        }
        model.setDataVector(data, columns);
    }

    private void createProductListSection() {
        panel2.setLayout(new GridLayout(1, 2));
        model = new DefaultTableModel(data, columns);
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
        scrollPane.setSize(500, 300);

// Insert an emoji into a specific cell (e.g., row 0, column 0)
//        table.getModel().setValueAt("<html>😊</html>", 0, 5);

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

    private void addProductsToCart() {
        cartItems = ShopData.getCartItems();
        String productId = lblProductId.getText();

        int quantity = getAddQuantity();
        if (quantity == -1) {
            JOptionPane.showMessageDialog(this, "Quantity is invalid !!!");
            return;
        }
        Product product = productList.stream()
                .filter(x -> x.getProductId() == productId)
                .findFirst()
                .get();
        if (product.getNoOfItemsAvailable() < quantity) {
            JOptionPane.showMessageDialog(this, "Maximum quantity available is " + product.getNoOfItemsAvailable());
            return;
        }

        try {
            CartItem cartItems = this.cartItems.stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst()
                    .get();
            if (cartItems != null) {
                //Check to see whether the product already exists in the cart
                cartItems.addQuantity(quantity, product.getPrice());
                return;
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
