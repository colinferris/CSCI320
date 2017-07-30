package com.globalcanal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App 
{
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
    public static void main(String[] args) {

        App globalCanal = new App();

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

            /**
             * Runs a basic query on the table
             */
            System.out.println("\n\nPrint results of SELECT * FROM person");
            ResultSet results = UserAccountTable.queryUserAccountTable(
                    globalCanal.getConnection(),
                    new ArrayList<String>(),
                    new ArrayList<String>());

            /**
             * Iterates the Result set
             *
             * Result Set is what a query in H2 returns
             *
             * Note the columns are not 0 indexed
             * If you give no columns it will return them in the
             * order you created them. To gaurantee order list the columns
             * you want
             */
            while(results.next()){
                System.out.printf("\tUserAccount %d: %s %s %s\n",
                        results.getInt(1),
                        results.getString(2),
                        results.getString(4),
                        results.getString(3));
            }

            /**
             * A more complex query with columns selected and 
             * addition conditions
             */
            System.out.println("\n\nPrint results of SELECT "
                    + "id, first_name "
                    + "FROM person "
                    + "WHERE first_name = \'Scott\' "
                    + "AND last_name = \'Johnson\'");

            /**
             * This is one way to do this, but not the only
             *
             * Create lists to make the whole thing more generic or
             * you can just construct the whole query here 
             */
            ArrayList<String> columns = new ArrayList<String>();
            columns.add("id");
            columns.add("first_name");
            columns.add("last_name");

            /**
             * Conditionals
             */
            ArrayList<String> whereClauses = new ArrayList<String>();
            whereClauses.add("first_name = \'Scott\'");
            whereClauses.add("last_name = \'Johnson\'");

            /**
             * query and get the result set
             *
             * parse the result set and print it
             *
             * Notice not all of the columns are here because
             * we limited what to show in the query
             */
            ResultSet results2 = UserAccountTable.queryUserAccountTable(
                    globalCanal.getConnection(),
                    columns,
                    whereClauses);
            while(results2.next()){
                System.out.printf("\tUserAccount %d: %s %s\n",
                        results2.getInt(1),
                        results2.getString(2),
                        results2.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
