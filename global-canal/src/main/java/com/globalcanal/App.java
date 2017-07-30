package com.globalcanal;

import javax.jws.soap.SOAPBinding;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;


public class App {

    //The connection to the database
    private Connection conn;

    private UserAccount userAccount;


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

    public UserAccount getUserAccount(){
        return userAccount;
    }

    public void createUser(Connection conn){
        GlobalCanal gc = new GlobalCanal();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter User First Name:");
        String firstName = sc.nextLine();
        System.out.println("Enter User Last Name:");
        String lastName = sc.nextLine();
        System.out.println("Enter a Middle Initial:");
        String MI = sc.nextLine();
        System.out.println("Enter User Password:");
        String password = sc.nextLine();
        System.out.println("Enter your birthdate (mm/dd/yyyy)");
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
        try{
            java.util.Date birthdate = dateFormat.parse(sc.nextLine());
            gc.createUser(conn, firstName, lastName, MI, birthdate, password);

        }
        catch (ParseException ex) {
            ex.printStackTrace();
        }

    }

    public void getUserLogin(Connection conn){
        GlobalCanal gc = new GlobalCanal();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter User First Name:");
        String firstName = sc.nextLine();
        System.out.println("Enter User Last Name:");
        String lastName = sc.nextLine();
        System.out.println("Enter User Password:");
        String password = sc.nextLine();
        try{
            ResultSet rs = gc.userLogin(conn, firstName, lastName, password);

            if (!rs.next()){
                System.out.println("No such user");
            }
            else {
                userAccount = new UserAccount(
                        rs.getInt("ID"),
                        rs.getString("FIRST_NAME"),
                        rs.getString("LAST_NAME"),
                        rs.getString("MI"),
                        rs.getString("PASSWORD"),
                        rs.getDate("BIRTHDATE"),
                        rs.getDouble("CREDIT"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("No such user");
        }
    }

    public void createPaymentMethod(Connection conn, int u_id){
        GlobalCanal gc = new GlobalCanal();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Card Type:");
        String cardName = sc.nextLine();
        System.out.println("Enter Card Number:");
        long cardNumber = Long.parseLong(sc.nextLine());
        System.out.println("Enter Expiration Year: (xxxx)");
        int expirationYear = Integer.parseInt(sc.nextLine());
        System.out.println("Enter Expiration Month: (xx)");
        int expirationMonth = Integer.parseInt(sc.nextLine());
        System.out.println("Enter Security Code: ");
        int securityCode = Integer.parseInt(sc.nextLine());

        gc.createPaymentMethod(conn, u_id, cardName, cardNumber, expirationYear, expirationMonth, securityCode);
    }
    public void listUserInformation(Connection conn, UserAccount user){
        GlobalCanal gc = new GlobalCanal();
        gc.listUserInfomation(conn, user);

    }

    public void changeUserName(Connection conn, UserAccount ua){
        GlobalCanal gc = new GlobalCanal();
        gc.changeUserName(conn, ua);
    }

    public void deletePaymentMethod(Connection conn, int UserID){
        GlobalCanal gc = new GlobalCanal();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Payment Method ID to Delete: ");
        int idToDelete = Integer.parseInt(sc.nextLine());

        gc.deletePaymentMethod(conn, UserID, idToDelete);
    }

    public void createShippingAddress(Connection conn, UserAccount user){
        GlobalCanal gc = new GlobalCanal();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Street Number:");
        String streetNumber = sc.nextLine();
        System.out.println("Enter Street Name:");
        String streetName = sc.nextLine();
        System.out.println("Enter Apartment Number");
        String aptNum = sc.nextLine();
        System.out.println("Enter City: ");
        String city = sc.nextLine();
        System.out.println("Enter State: ");
        String state = sc.nextLine();
        System.out.println("Enter Zipcode");
        int zipcode = Integer.valueOf(sc.nextLine());
        ShippingAddress newAddress = new ShippingAddress(user.getId(), 0, streetNumber, streetName, aptNum, city, state, zipcode);
        ShippingAddressTable.addShippingAddress(conn, newAddress);
    }

    public void listShippingInformation(Connection conn, UserAccount user){
        GlobalCanal gc = new GlobalCanal();
        gc.listShippingInformation(conn, user.getId());

    }

    public void deleteShippingAddress(Connection conn, int UserID){
        GlobalCanal gc = new GlobalCanal();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Payment Method ID to Delete: ");
        int idToDelete = Integer.parseInt(sc.nextLine());

        gc.deleteShipmentMethod(conn, UserID, idToDelete);
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
        final UserAccount userAccount;

        final GlobalCanal gc = new GlobalCanal();

        final App globalCanal = new App();

        //Hard drive location of the database
        String location = "~/globalcanal/globalcanal";
        final String user = "scj";
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
        mainMenu.addMenuItem("2", "Login", new Runnable() {
            @Override
            public void run() {
                globalCanal.getUserLogin(globalCanal.getConnection());

            }

        });
        mainMenu.addMenuItem("3", "Create User", new Runnable() {
            @Override
            public void run() {
                globalCanal.createUser(globalCanal.getConnection());
            }
        });
        mainMenu.addMenuItem("9", "Update Name", new Runnable() {
            @Override
            public void run() {
                UserAccount ua = globalCanal.getUserAccount();
                Connection conn = globalCanal.getConnection();
                globalCanal.changeUserName(conn, ua);
            }
        });
        mainMenu.addMenuItem("U1", "Display User Info", new Runnable() {
            @Override
            public void run() {
                globalCanal.listUserInformation(globalCanal.getConnection(), globalCanal.getUserAccount());
            }
        });
        mainMenu.addMenuItem("4", "Create Payment Method", new Runnable() {
            @Override
            public void run() {
                UserAccount ua = globalCanal.getUserAccount();
                globalCanal.createPaymentMethod(globalCanal.getConnection(), ua.getId());
            }
        });
        mainMenu.addMenuItem("5", "List My Payment Methods:", new Runnable() {
            @Override
            public void run() {
                UserAccount ua = globalCanal.getUserAccount();
                PaymentMethodTable.printPaymentMethodByUID(globalCanal.getConnection(), ua.getId());
            }
        });
        mainMenu.addMenuItem("6", "Delete My Payment Method: ", new Runnable() {
            @Override
            public void run() {
                UserAccount ua = globalCanal.getUserAccount();
                globalCanal.deletePaymentMethod(globalCanal.getConnection(), ua.getId());
            }
        });
        mainMenu.addMenuItem("7", "Add Credit to account:", new Runnable() {
            @Override
            public void run() {
                UserAccount ua = globalCanal.getUserAccount();
                Connection conn = globalCanal.getConnection();
                Scanner sc = new Scanner(System.in);
                System.out.println("Enter Credit to Add: ");
                double credit = Double.parseDouble(sc.nextLine());
                gc.addCredit(conn, ua, credit);
            }
        });
        mainMenu.addMenuItem("8", "Display account information: ", new Runnable() {
            @Override
            public void run() {
                UserAccount ua = globalCanal.getUserAccount();
                System.out.printf("UserAccount %d: %s %s %s %tF %s %f\n",
                        ua.getId(),
                        ua.getFName(),
                        ua.getLName(),
                        ua.getMI(),
                        ua.getBirthdate(),
                        ua.getPassword(),
                        ua.getCredit());
            }
        });
        mainMenu.addMenuItem("SH1", "Add Shipping Address", new Runnable() {
            @Override
            public void run() {
                UserAccount ua = globalCanal.getUserAccount();
                Connection conn = globalCanal.getConnection();
                globalCanal.createShippingAddress(conn, ua);
            }
        });

        mainMenu.addMenuItem("SH2", "List Shipping Address", new Runnable() {
            @Override
            public void run() {
                UserAccount ua = globalCanal.getUserAccount();
                Connection conn = globalCanal.getConnection();
                globalCanal.listShippingInformation(conn, ua);
            }
        });
        mainMenu.addMenuItem("SH3", "Delete My Shipping Address: ", new Runnable() {
            @Override
            public void run() {
                UserAccount ua = globalCanal.getUserAccount();
                globalCanal.deleteShippingAddress(globalCanal.getConnection(), ua.getId());
            }
        });



        mainMenu.start();
    }

}
