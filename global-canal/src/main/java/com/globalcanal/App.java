package com.globalcanal;

public class App {

    public static void main( String[] args ) {
        Menu mainMenu = new Menu();
        GlobalCanal gc = new GlobalCanal();

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
