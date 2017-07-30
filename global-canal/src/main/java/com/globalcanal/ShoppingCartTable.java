
package com.globalcanal;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


/**
 * 
 * Class to make and manipulate a shoppingcart table
 *
 */
public class ShoppingCartTable {

	public static void populateShoppingCartTableFromCSV(Connection conn,
												   String fileName) throws SQLException{
		
		/*
		 * Similar to the demo, creates a list of ordered product and sends it
		 * to a helper function to build the bulk insert.
		 */

		ArrayList<ShoppingCart> shoppingCarts = new ArrayList<ShoppingCart>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = br.readLine()) != null){
				String[] split = line.split(",");
				shoppingCarts.add(new ShoppingCart(
						Integer.parseInt(split[0]),
						Integer.parseInt(split[1]),
						Double.parseDouble(split[2])
				));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/**
		 * Creates the SQL query to do a bulk add of all people
		 * that were read in. This is more efficent then adding one
		 * at a time
		 */
		String sql = createShoppingCartSQLInsert(shoppingCarts);

		/**
		 * Create and execute an SQL statement
		 *
		 * execute only returns if it was successful
		 */
		Statement stmt = conn.createStatement();
		stmt.execute(sql);



	}
	
	public static void createShoppingCartTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS shoppingcart("
					+ "U_ID INT,"
					+ "SC_ID INT AUTO_INCREMENT,"
					+ "TOTALCOST NUMERIC (8,2),"
					+ "PRIMARY KEY (U_ID, SC_ID),"
					+ "FOREIGN KEY (U_ID) REFERENCES useraccount(ID)"
					+ ");";
			
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addShoppingCart(Connection conn,
								  int U_ID,
								  int SC_ID,
								  double totalCost){
		
		String query = String.format("INSERT INTO shoppingcart "
								   + "VALUES (%d,\'%d\',\'%f\');",
									 U_ID, SC_ID, totalCost);
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
										
	}
	
	
	public static ResultSet queryShoppingCartTable(Connection conn,
											  ArrayList<String> columns,
											  ArrayList<String> whereClauses){
		
		StringBuilder sb = new StringBuilder();
		
		
		sb.append("SELECT ");
		
		if(columns.isEmpty()){
			sb.append("* ");
		}
		else {
			for(int i = 0; i < columns.size(); i++){
				if(i != columns.size() - 1){
					sb.append(columns.get(i) + ", ");
				}
				else{
					sb.append(columns.get(i) + " ");
				}
			}
		}
		
		sb.append("FROM shoppingcart ");
		
		if(!whereClauses.isEmpty()){
			sb.append("WHERE ");
			for(int i = 0; i < whereClauses.size(); i++){
				if(i != whereClauses.size() - 1){
					sb.append(whereClauses.get(i) + " AND ");
				}
				else{
					sb.append(whereClauses.get(i));
				}
			}
		}
		
		sb.append(";");
		System.out.println("Query: " + sb.toString());
		try{ 
			Statement stmt = conn.createStatement();
			return stmt.executeQuery(sb.toString());
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static void printShoppingCartTable(Connection conn){
		String query = "SELECT * FROM shoppingcart;";
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			
			while(result.next()){
				System.out.printf("ShoppingCart %d: %d %f\n",
								  result.getInt(1),
								  result.getInt(2),
								  result.getDouble(3));
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static String createShoppingCartSQLInsert(ArrayList<ShoppingCart> shoppingCarts)
	{
		StringBuilder sb = new StringBuilder();

		/**
		 * The start of the statement,
		 * tells it the table to add it to
		 * the order of the data in reference
		 * to the columns to ad dit to
		 */
		sb.append("INSERT INTO shoppingcart (U_ID, SC_ID, TOTALCOST) VALUES");

		for(int i = 0; i < shoppingCarts.size(); i++){
			ShoppingCart sc = shoppingCarts.get(i);
			sb.append(String.format("(%d,%d,%.2f)",
					sc.getUID(), sc.getSCID(), sc.totalCost()
			));
			if( i != shoppingCarts.size()-1){
				sb.append(",");
			}
			else{
				sb.append(";");
			}
		}

		return sb.toString();
	}


}
