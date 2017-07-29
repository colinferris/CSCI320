package com.globalcanal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;



/**
 * 
 * Class to make and manipulate a orderedproduct table
 *
 */
public class OrderedProductTable {

	public static void populateOrderedProductTableFromCSV(Connection conn,
												   String fileName) throws SQLException{
		
		/*
		 * Similar to the demo, creates a list of ordered product and sends it
		 * to a helper function to build the bulk insert.
		 */

		ArrayList<OrderedProduct> orderedProducts = new ArrayList<OrderedProduct>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = br.readLine()) != null){
				String[] split = line.split(",");
				orderedProducts.add(new OrderedProduct(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
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
		String sql = createOrderedProductSQLInsert(orderedProducts);

		/**
		 * Create and execute an SQL statement
		 *
		 * execute only returns if it was successful
		 */
		Statement stmt = conn.createStatement();
		stmt.execute(sql);
	}

	
	public static void createOrderedProductTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS orderedproduct("
					+ "ORDER_ID INT,"
					+ "PRODUCT_ID INT,"
					+ "PRIMARY KEY (ORDER_ID, PRODUCT_ID),"
					+ "FOREIGN KEY (ORDER_ID) REFERENCES previousorder(ORDER_ID),"
					+ "FOREIGN KEY (PRODUCT_ID) REFERENCES product(PRODUCT_ID),"
					+ ");";
			
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addOrderedProduct(Connection conn,
								  int order_ID,
								  int product_ID){
		
		String query = String.format("INSERT INTO orderedproduct "
								   + "VALUES (%d,\'%d\');",
									 order_ID, product_ID);
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
										
	}
	
	
	public static ResultSet queryOrderedProductTable(Connection conn,
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
		
		sb.append("FROM orderedproduct ");
		
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
	
	
	public static void printOrderedProductTable(Connection conn){
		String query = "SELECT * FROM orderedproduct;";
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			
			while(result.next()){
				System.out.printf("OrderedProduct %d: %d\n",
								  result.getInt(1),
								  result.getInt(2));
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static String createOrderedProductSQLInsert(ArrayList<OrderedProduct> orderedProducts){
		StringBuilder sb = new StringBuilder();

		/**
		 * The start of the statement,
		 * tells it the table to add it to
		 * the order of the data in reference
		 * to the columns to ad dit to
		 */
		sb.append("INSERT INTO orderedproduct (ORDER_ID, PRODUCT_ID) VALUES");

		/**
		 * For each person append a (id, first_name, last_name, MI) tuple
		 *
		 * If it is not the last person add a comma to seperate
		 *
		 * If it is the last person add a semi-colon to end the statement
		 */
		for(int i = 0; i < orderedProducts.size(); i++){
			OrderedProduct op = orderedProducts.get(i);
			sb.append(String.format("(%d,%d)",
					op.orderID(), op.productID()));
			if( i != orderedProducts.size()-1){
				sb.append(",");
			}
			else{
				sb.append(";");
			}
		}

		return sb.toString();
	}
		
}