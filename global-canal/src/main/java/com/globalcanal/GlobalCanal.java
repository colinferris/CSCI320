package com.globalcanal;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


/**
 *
 * Main program of the GlobalCanal database project
 *
 *
 *
 */
public class GlobalCanal {


    private static Connection conn;

    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive

        return rand.nextInt((max - min) + 1) + min;
    }


    /************************************
     *
     * CART FUCTIONALITY
     *
     ************************************/

    /**
     * Adds a product to a user's cart
     *
     * U_Id is the id of the user, P_id is the product id
     *
     */
    public static void addProductToCart(int U_id, int SC_id, int P_id){
        double totalCost = 0.0;
        String query = String.format("SELECT PRICE FROM PRODUCT WHERE P_ID = %d;", P_id);
        ResultSet rs;

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            totalCost = rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        ProductInCartTable.addProductInCart(conn, SC_id, P_id, totalCost);
        updateCartPrice(U_id, SC_id);
    }

    /**
     * Removes a product from  a user's cart
     *
     * @param U_id
     * @param SC_id
     * @param P_id
     */
    public static void removeProductFromCart(int U_id, int SC_id, int P_id){
        String query = String.format("DELETE FROM productincart "
                + "WHERE SC_ID = %d AND P_ID = %d", SC_id, P_id);

        try {
            Statement stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateCartPrice(U_id, SC_id);
    }

    /**
     * Updates the price in a cart to reflect the items in it
     *
     * @param U_id
     * @param SC_id
     */
    public static void updateCartPrice(int U_id, int SC_id){

        double totalPrice = 0.0;
        ResultSet rs;
        ResultSet result2;
        String query = String.format("SELECT TOTALCOST FROM productincart "
                + "WHERE SC_ID = %d;", SC_id);

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next()){
                totalPrice += rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



        query = String.format("UPDATE shopping_cart "
                + "SET TOTALCOST = %f "
                + "WHERE U_ID = %d AND SC_ID = %d;", totalPrice, U_id, SC_id);
    }

    /************************************
     *
     * END BLOCK
     *
     ************************************/

    /************************************
     *
     * ORDER FUCTIONALITY
     *
     ************************************/

    /**
     * Creates a table entry for previousOrder based on what was in the user's cart
     * removes the items from the user's cart, and credits their account
     *
     * provides check to make sure users have the money to buy a product
     *
     * @param U_id
     * @param SC_id
     */
    public static void makeOrder(int U_id, int SC_id){

        double credit = 0.0;
        double totalCost = 0.0;
        int Shipment_id = 0;
        int Payment_id = 0;
        int order_id = 0;
        int product_id;
        ResultSet rs;

        String query = String.format("SELECT CREDIT FROM useraccount "
                + "WHERE ID = %d;", U_id);
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            credit = rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }




        query = String.format("SELECT TOTALCOST FROM shoppingcart "
                + "WHERE U_ID = %d AND SC_ID = %d;", U_id, SC_id);
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            totalCost = rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }




        if(totalCost > credit){
            System.out.println("Insufficent Funds for Purchase.");
        }
        else{
            //Random number for dates
            int rand = randInt(20170701, 202001230);

            //Find shipping address
            query = String.format("SELECT S_ID FROM shippingaddress "
                    + "WHERE U_ID = %d;", U_id);
            try {
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery(query);
                Shipment_id = rs.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }



            //find payment method
            query = String.format("SELECT P_ID FROM paymentmethod "
                    + "WHERE U_ID = %d;", U_id);

            try {
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery(query);
                Payment_id = rs.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }


            Random  rnd;
            Date    dt;
            Date    s_dt;
            long    ms;

            // Get a new random instance, seeded from the clock
            rnd = new Random();

            // Get an Epoch value roughly between 1940 and 2010
            // -946771200000L = January 1, 1940
            // Add up to 70 years to it (using modulus on the next long)
            ms = -946771200000L + (Math.abs(rnd.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));

            // Construct a date
            dt = new Date(ms);
            s_dt = new Date(ms + 259200000);

            //generate order
            PreviousOrderTable.addPreviousOrder(conn, rand - 1000000, dt, s_dt, totalCost, Payment_id, Shipment_id, U_id);

            //get id of the order
            query=String.format("SELECT ID FROM previousorder "
                            + "WHERE DATEOFPURCHASE = %d AND DATEOFSHIPTMENT = %d "
                            + "AND P_ID = %d AND S_ID = %d AND U_id = %d;",
                    rand, rand+3, Payment_id, Shipment_id, U_id);

            try {
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery(query);
                order_id = rs.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }



            //create orderedproducts
            query = String.format("SELECT P_ID FROM productincart "
                    + "WHERE SC_ID = %d;", SC_id);
            try {
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery(query);
                while(rs.next()){
                    product_id = rs.getInt(1);
                    OrderedProductTable.addOrderedProduct(conn, order_id, product_id);
                    removeProductFromCart(U_id, SC_id, product_id);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }





            //credit the useraccount
            double newcredit = credit - totalCost;

            query = String.format("UPDATE useraccount"
                    + "SET credit = %f "
                    + "WHERE U_ID = %d;", newcredit, U_id);
        }

    }

    /************************************
     *
     * END BLOCK
     *
     ************************************/



    /************************************
     *
     * ITEM SEARCH
     *
     ************************************/


    /**
     * Uses a category name to find any product
     * that has a category that contains the given string
     * as a substring.
     *
     * @param category
     * @return
     */
    public static ResultSet searchProductByCategory(String category){

        ResultSet rs = null;

        String query = String.format("SELECT * FROM product "
                + "WHERE P_ID = "
                + "(SELECT P_ID FROM productcategory"
                + "WHERE NAME = %"
                + category
                + "%);");

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    /************************************
     *
     * User Functionality
     *
     ************************************/
    public static ResultSet userLogin(Connection conn, String firstName, String lastName, String password) {
        ResultSet rs = null;
        String query = String.format("SELECT * FROM useraccount " +
                "WHERE FIRST_NAME = \'%s\' AND LAST_NAME = \'%s\' AND PASSWORD = \'%s\';",
                firstName, lastName, password);
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            System.out.println("Query Worked");
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return rs;

    }

    public static void createUser(Connection conn, String firstName, String lastName, String mI, java.util.Date birthdate, String password){
        UserAccountTable.addUserAccount(conn, firstName, lastName, mI, birthdate, password, 10000.00);
        UserAccountTable.printUserAccountTable(conn);
    }

    public static void addCredit(Connection conn, UserAccount account, double creditToAdd){
        String query = String.format("UPDATE useraccount SET CREDIT = %.2f WHERE ID = %d;", account.getCredit() + creditToAdd, account.getId());
        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        account.setCredit(account.getCredit() + creditToAdd);
    }

    public static void listUserInfomation(Connection conn, UserAccount account){
        String query = String.format("SELECT * from useraccount WHERE ID = %d;", account.getId());
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while(result.next()){
                System.out.printf("UserAccount %d: %s %s %s %tF %s %f\n",
                        result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getDate(5),
                        result.getString(6),
                        result.getDouble(7));
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /************************************
     *
     * Payment Method Functionality
     *
     ************************************/

    public static void createPaymentMethod(Connection conn, int U_ID, String cName, long cNum, int expYear, int expMonth, int sCode){
        PaymentMethodTable.addPaymentMethod(conn, U_ID, cName, cNum, expYear, expMonth, sCode);
    }

    public static void changeUserName(Connection conn, UserAccount user){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter User First Name:");
        String firstName = sc.nextLine();
        System.out.println("Enter User Last Name:");
        String lastName = sc.nextLine();
        System.out.println("Enter User Middle Initial:");
        String middleInitial = sc.nextLine();
        user.setFName(!firstName.isEmpty() ? firstName : user.getFName());
        user.setLName(!lastName.isEmpty() ? lastName : user.getLName());
        user.setMI(!middleInitial.isEmpty() ? middleInitial : user.getMI());

        UserAccountTable.updateUserName(conn, user);
    }

    public static void deletePaymentMethod(Connection conn, int userID, int pMethodID){
        String query = String.format("DELETE FROM paymentmethod WHERE U_ID = %d AND P_ID = %d;", userID, pMethodID);
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void listShippingInformation(Connection conn, int userID){
        String query = String.format("SELECT * FROM shippingaddress WHERE U_ID = %d;", userID);
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while(result.next()){
                System.out.printf("Shipping Address %d: %s %s %s %s %s %d\n",
                        result.getInt(2),
                        result.getString(3),
                        result.getString(4),
                        result.getString(5),
                        result.getString(6),
                        result.getString(7),
                        result.getInt(8));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void deleteShipmentMethod(Connection conn, int userID, int sAddressID){
        String query = String.format("DELETE FROM shippingaddress WHERE U_ID = %d AND S_ID = %d;", userID, sAddressID);
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void listPhoneNumber(Connection conn, int userID){
        String query = String.format("SELECT * FROM phonenumber WHERE U_ID = %d;", userID);
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while(result.next()){
                System.out.printf("Shipping Address %d: %s\n",
                        result.getInt(1),
                        result.getString(2));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void deletePhoneNumber(Connection conn, int userID, String pNumber){
        String query = String.format("DELETE FROM phonenumber WHERE U_ID = %d AND PNUMBER = \'%s\';", userID, pNumber);
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }




}
