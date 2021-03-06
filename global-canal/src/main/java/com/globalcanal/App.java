package com.globalcanal;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class App {
    private static Scanner scan;
    private static GlobalCanal context;
    private static Connection conn;
    private static String location = "~/globalcanal/globalcanal";

    public static void main( String[] args ) {
        scan = new Scanner(System.in);
        context = new GlobalCanal();
        login();

        int choice;
        do {
            System.out.println("\n*** Menu Options ***\n");
            System.out.println("1 - List Product");

            System.out.print("> ");
            choice = scan.nextInt();

            switch (choice) {
                case 1:
                    break;
                case 2:
                    System.out.println("2");
                    break;
                case 3:
                    System.out.println("3");
                    break;
                case 4:
                    System.out.println("4");
                    break;
                default:
            }
        }
        while (choice != 4);
    }

    public static void login() {
        boolean success;
        do {
            System.out.println("\nAuthentication is required.\n");
            System.out.println("Enter Username:");
            String username = scan.nextLine();
            System.out.println("Enter Password:");
            String password = scan.nextLine();

            success = createConnection(location, username, password);
            if (!success) {
                System.out.println("\nPlease try again.\n");
            }
        } while (success != true);
    }

    public static boolean createConnection(String location,
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
            return false;
        } catch (ClassNotFoundException e){
           return false;
        }
        return true;
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
}
