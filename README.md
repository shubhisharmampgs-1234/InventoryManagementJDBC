# Inventory Management System using Java JDBC and MySQL

## 1. Project Overview
The Inventory Management System is a menu-driven Java application developed using JDBC and MySQL. It helps manage product records and perform essential inventory operations.

## 2. Technologies Used
- Java 21
- JDBC
- MySQL
- MySQL Connector/J
- IntelliJ IDEA

## 3. Database Details
- Database Name: `retail_db`
- Table Name: `Product`

### Table Structure

| Column | Data Type |
|---|---|
| product_id | INT PRIMARY KEY |
| product_name | VARCHAR(100) |
| category | VARCHAR(50) |
| quantity | INT |
| price | DOUBLE |

## 4. Features
1. Insert at least three product records using PreparedStatement.
2. Display products by category where quantity is less than 20, sorted by price in descending order.
3. Update existing product stock.
4. Update product price after checking product existence and validating that the price is greater than zero.
5. Calculate total inventory value using `SUM(quantity * price)`.
6. Delete a product only when its quantity is zero.
7. Handle duplicate product IDs and missing product records.

## 5. Project Structure
- `InventoryManagement.java` – Main Java application.
- `database.sql` – Database and table creation script.
- `Screenshots/` – Project and execution evidence.
- `AI_Prompts/` – AI prompts and reflections.

## 6. How to Run
1. Install JDK 21 and MySQL.
2. Execute `database.sql` in MySQL Workbench.
3. Add MySQL Connector/J to the Java project libraries.
4. Update the database connection credentials locally in the Java source code.
5. Run `InventoryManagement.java` from IntelliJ IDEA.

## 7. Database Connection
- URL: `jdbc:mysql://localhost:3306/retail_db`
- Username: `root`
- Password: Configure your own local MySQL password.

## 8. Author
Student Project – OOPJ MSE-2
