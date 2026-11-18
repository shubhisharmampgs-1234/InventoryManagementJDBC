import java.sql.*;
import java.util.Scanner;

public class InventoryManagement {

    static final String URL = "jdbc:mysql://localhost:3306/retail_db";
    static final String USER = "root";
    static final String PASSWORD = "XXXXXX";

    static Connection con;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to retail_db successfully!");

            // Menu will be added here
            int choice;

            do {
                System.out.println("\n===== INVENTORY MANAGEMENT SYSTEM =====");
                System.out.println("1. Insert Products");
                System.out.println("2. Display Products by Category");
                System.out.println("3. Update Stock");
                System.out.println("4. Update Product Price");
                System.out.println("5. Display Inventory Value and Delete Product");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        insertProducts();
                        break;
                    case 2:
                        displayByCategory();
                        break;
                    case 3:
                        updateStock();
                        break;
                    case 4:
                        updatePrice();
                        break;
                    case 5:
                        calculateAndDelete();
                        break;
                    case 6:
                        System.out.println("Exiting Inventory Management System.");
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }

            } while (choice != 6);

            con.close();

        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    static void insertProducts() {

        String sql = "INSERT INTO Product VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 1; i <= 3; i++) {

                System.out.println("\nEnter details of Product " + i);

                System.out.print("Product ID: ");
                int id = sc.nextInt();
                sc.nextLine();

                System.out.print("Product Name: ");
                String name = sc.nextLine();

                System.out.print("Category: ");
                String category = sc.nextLine();

                System.out.print("Quantity: ");
                int quantity = sc.nextInt();

                System.out.print("Price: ");
                double price = sc.nextDouble();

                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, category);
                ps.setInt(4, quantity);
                ps.setDouble(5, price);

                try {
                    ps.executeUpdate();
                    System.out.println("Product inserted successfully!");
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println("Duplicate Product ID! Record not inserted.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void displayByCategory() {

        sc.nextLine(); // Clear input buffer

        System.out.print("Enter category: ");
        String category = sc.nextLine();

        String sql = "SELECT * FROM Product WHERE category = ? AND quantity < 20 ORDER BY price DESC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, category);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- Products in " + category + " ---");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("Product ID: " + rs.getInt("product_id"));
                System.out.println("Name: " + rs.getString("product_name"));
                System.out.println("Category: " + rs.getString("category"));
                System.out.println("Quantity: " + rs.getInt("quantity"));
                System.out.println("Price: " + rs.getDouble("price"));
                System.out.println("-------------------------");
            }

            if (!found) {
                System.out.println("No matching products found.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void updateStock() {

        System.out.print("Enter Product ID: ");
        int id = sc.nextInt();

        System.out.print("Enter quantity to add: ");
        int quantity = sc.nextInt();

        String sql = "UPDATE Product SET quantity = quantity + ? WHERE product_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Stock updated successfully!");
            } else {
                System.out.println("Product does not exist.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void updatePrice() {

        System.out.print("Enter Product ID: ");
        int id = sc.nextInt();

        System.out.print("Enter new price: ");
        double newPrice = sc.nextDouble();

        String selectSQL = "SELECT * FROM Product WHERE product_id = ?";
        String updateSQL = "UPDATE Product SET price = ? WHERE product_id = ?";

        try (PreparedStatement selectPS = con.prepareStatement(selectSQL)) {

            selectPS.setInt(1, id);
            ResultSet rs = selectPS.executeQuery();

            if (rs.next()) {

                if (newPrice > 0) {

                    try (PreparedStatement updatePS = con.prepareStatement(updateSQL)) {

                        updatePS.setDouble(1, newPrice);
                        updatePS.setInt(2, id);
                        updatePS.executeUpdate();

                        System.out.println("Price updated successfully!");
                        System.out.println("Product ID: " + id);
                        System.out.println("Product Name: " + rs.getString("product_name"));
                        System.out.println("New Price: " + newPrice);
                    }

                } else {
                    System.out.println("Price must be greater than zero.");
                }

            } else {
                System.out.println("Product does not exist.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void calculateAndDelete() {

        String sumSQL = "SELECT SUM(quantity * price) AS total FROM Product";

        try (PreparedStatement ps = con.prepareStatement(sumSQL);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println("Total Inventory Value: " + rs.getDouble("total"));
            }

        } catch (SQLException e) {
            System.out.println("Error calculating inventory value: " + e.getMessage());
        }

        System.out.print("\nEnter Product ID to delete: ");
        int id = sc.nextInt();

        String selectSQL = "SELECT quantity FROM Product WHERE product_id = ?";
        String deleteSQL = "DELETE FROM Product WHERE product_id = ? AND quantity = 0";

        try (PreparedStatement selectPS = con.prepareStatement(selectSQL)) {

            selectPS.setInt(1, id);
            ResultSet rs = selectPS.executeQuery();

            if (rs.next()) {

                int quantity = rs.getInt("quantity");

                if (quantity == 0) {

                    try (PreparedStatement deletePS = con.prepareStatement(deleteSQL)) {
                        deletePS.setInt(1, id);
                        deletePS.executeUpdate();
                        System.out.println("Product deleted successfully!");
                    }

                } else {
                    System.out.println("Product cannot be deleted because quantity is not zero.");
                }

            } else {
                System.out.println("Product does not exist.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }






}