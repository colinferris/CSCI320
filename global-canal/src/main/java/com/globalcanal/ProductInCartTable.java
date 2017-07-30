package com.globalcanal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


/**
 * 
 * Class to make and manipulate a productincart table
 *
 */
public class ProductInCartTable {

	public static void populateProductInCartTableFromCSV(Connection conn,
												   String fileName) throws SQLException{
		/*
		 * Similar to the demo, creates a list of ordered product and sends it
		 * to a helper function to build the bulk insert.
		 */

		ArrayList<ProductInCart> productInCarts = new ArrayList<ProductInCart>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = br.readLine()) != null){
				String[] split = line.split(",");
				productInCarts.add(new ProductInCart(
						Integer.parseInt(split[0]),
						Integer.parseInt(split[1])
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
		String sql = createProductInCartSQLInsert(productInCarts);

		/**
		 * Create and execute an SQL statement
		 *
		 * execute only returns if it was successful
		 */
		Statement stmt = conn.createStatement();
		stmt.execute(sql);



	}
	
	public static void createProductInCartTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS productincart("
					+ "SC_ID INT,"
					+ "P_ID INT,"
					+ "TOTALCOST NUMERIC(8,2),"
					+ "FOREIGN KEY (SC_ID) REFERENCES shoppingcart(SC_ID),"
					+ "FOREIGN KEY (P_ID) REFERENCES product(ID),"
					+ "PRIMARY KEY (SC_ID, P_ID)"
					+ ");";
			
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addProductInCart(Connection conn,
								  int SC_ID,
								  int P_ID,
								  double totalCost){
		
		String query = String.format("INSERT INTO productincart "
								   + "VALUES (%d,\'%d\',\'%f\');",
									 SC_ID, P_ID, totalCost);
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
										
	}
	
	
	public static ResultSet queryProductInCartTable(Connection conn,
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
		
		sb.append("FROM productincart ");
		
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
	
	
	public static void printProductInCartTable(Connection conn){
		String query = "SELECT * FROM productincart;";
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			
			while(result.next()){
				System.out.printf("ProductInCart %d: %d %f\n",
								  result.getInt(1),
								  result.getInt(2),
								  result.getDouble(3));
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	public static String createProductInCartSQLInsert(ArrayList<ProductInCart> productInCarts)
	{
		StringBuilder sb = new StringBuilder();

		/**
		 * The start of the statement,
		 * tells it the table to add it to
		 * the order of the data in reference
		 * to the columns to ad dit to
		 */
		sb.append("INSERT INTO productincart (P_ID, SC_ID) VALUES");

		for(int i = 0; i < productInCarts.size(); i++){
			ProductInCart pc = productInCarts.get(i);
			sb.append(String.format("(%d,%d)",
					pc.getPID(), pc.getSCID()
			));
			if( i != productInCarts.size()-1){
				sb.append(",");
			}
			else{
				sb.append(";");
			}
		}

		return sb.toString();
	}
		
}
