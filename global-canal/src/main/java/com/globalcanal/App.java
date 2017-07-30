package com.globalcanal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class App {

    //The connection to the database
    private Connection conn;

    /**
     * Create a database connection with the given params
     * @param location: path of where to place the database
     * @param user: user name for the owner of the database
     * @param password: password of the database owner
     */
    public void createConnection(String location,
                                 String user,
                                 String password){
        try {

            //This needs to be on the front of your location
            String url = "jdbc:h2:" + location;

            //This tells it to use the h2 driver
            Class.forName("org.h2.Driver");

            //creates the connection
            conn = DriverManager.getConnection(url,
                    user,
                    password);
        } catch (SQLException e) {
            //You should handle this better
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * just returns the connection
     * @return: returns class level connection
     */
    public Connection getConnection(){
        return conn;
    }

    /**
     * When your database program exits
     * you should close the connection
     */
    public void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) {
        Menu mainMenu = new Menu();
        GlobalCanal gc = new GlobalCanal();

        com.globalcanal.App globalCanal = new com.globalcanal.App();

        //Hard drive location of the database
        String location = "~/globalcanal/globalcanal";
        String user = "scj";
        String password = "password";

        //Create the database connections, basically makes the database
        globalCanal.createConnection(location, user, password);
        System.out.println("here");

        try {

            /**
             * Creates a sample UserAccount table
             * and populates it from a csv file
             */
            UserAccountTable.createUserAccountTable(globalCanal.getConnection());
            UserAccountTable.populateUserAccountTableFromCSV(
                    globalCanal.getConnection(),
                    "C:/Users/TIMBERWOLVES/Documents/GitHub/CSCI320/global-canal/src/main/csv/User.csv"
            );

            ProductTable.createProductTable(globalCanal.getConnection());
            ProductTable.populateProductTableFromCSV(
                    globalCanal.getConnection(),
                    "C:/Users/TIMBERWOLVES/Documents/GitHub/CSCI320/global-canal/src/main/csv/Product.csv"
            );

            ShoppingCartTable.createShoppingCartTable(globalCanal.getConnection());
            ShoppingCartTable.populateShoppingCartTableFromCSV(
                    globalCanal.getConnection(),
                    "C:/Users/TIMBERWOLVES/Documents/GitHub/CSCI320/global-canal/src/main/csv/Shopping_Cart.csv"
            );

            ShippingAddressTable.createShippingAddressTable(globalCanal.getConnection());
            ShippingAddressTable.populateShippingAddressTableFromCSV(
                    globalCanal.getConnection(),
                    "C:/Users/TIMBERWOLVES/Documents/GitHub/CSCI320/global-canal/src/main/csv/Shipping_Address.csv"
            );

            PreviousOrderTable.createPreviousOrderTable(globalCanal.getConnection());
            PreviousOrderTable.populatePreviousOrderTableFromCSV(
                    globalCanal.getConnection(),
                    "C:/Users/TIMBERWOLVES/Documents/GitHub/CSCI320/global-canal/src/main/csv/Previous Order.csv"
            );

            OrderedProductTable.createOrderedProductTable(globalCanal.getConnection());
            OrderedProductTable.populateOrderedProductTableFromCSV(
                    globalCanal.getConnection(),
                    "C:/Users/TIMBERWOLVES/Documents/GitHub/CSCI320/global-canal/src/main/csv/Ordered_Product.csv"
            );

            PaymentMethodTable.createPaymentMethodTable(globalCanal.getConnection());
            PaymentMethodTable.populatePaymentMethodTableFromCSV(
                    globalCanal.getConnection(),
                    "C:/Users/TIMBERWOLVES/Documents/GitHub/CSCI320/global-canal/src/main/csv/Payment_Method.csv"
            );

            PhoneNumberTable.createPhoneNumberTable(globalCanal.getConnection());
            PhoneNumberTable.populatePhoneNumberTableFromCSV(
                    globalCanal.getConnection(),
                    "C:/Users/TIMBERWOLVES/Documents/GitHub/CSCI320/global-canal/src/main/csv/Phone_Numbers.csv"
            );

            ProductCategoryTable.createProductCategoryTable(globalCanal.getConnection());
            ProductCategoryTable.populateProductCategoryTableFromCSV(
                    globalCanal.getConnection(),
                    "C:/Users/TIMBERWOLVES/Documents/GitHub/CSCI320/global-canal/src/main/csv/Product_Category.csv"
            );

            ProductInCartTable.createProductInCartTable(globalCanal.getConnection());
            ProductInCartTable.populateProductInCartTableFromCSV(
                    globalCanal.getConnection(),
                    "C:/Users/TIMBERWOLVES/Documents/GitHub/CSCI320/global-canal/src/main/csv/Product_In_Cart.csv"
            );



            /**
             * Just displays the table
             */
            UserAccountTable.printUserAccountTable(globalCanal.getConnection());
            ProductTable.printProductTable(globalCanal.getConnection());

        } catch (SQLException e) {
            System.out.println("Data already added");
        }

        mainMenu.addMenuItem("1", "Product Search", new Runnable() {
                    @Override
                    public void run(){
                        System.out.println("This is the second menu item");
                        System.out.println("This menu item features a Runnable to define the function this menu item calls");
                    }
        });

        mainMenu.start();
    }

}
