package com.globalcanal;
import java.util.Scanner;

public class App {

    public static void main( String[] args ) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Enter \"1\", \"2\", \"3\" or \"4\"");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("1");
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
}
