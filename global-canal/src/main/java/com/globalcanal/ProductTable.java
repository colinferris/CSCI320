package com.globalcanal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


/**
 * 
 * Class to make and manipulate a product table
 *
 */
public class ProductTable {

	public static void populateProductTableFromCSV(Connection conn,
												   String fileName) throws SQLException{
		/*
		 * Similar to the demo, creates a list of ordered product and sends it
		 * to a helper function to build the bulk insert.
		 */

		ArrayList<Product> products = new ArrayList<Product>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = br.readLine()) != null){
				String[] split = line.split(",");
				products.add(new Product(
						Integer.parseInt(split[0]),
						split[1],
						split[2],
						split[3],
						split[4],
						Double.valueOf(split[5])
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
		String sql = createProductSQLInsert(products);

		/**
		 * Create and execute an SQL statement
		 *
		 * execute only returns if it was successful
		 */
		Statement stmt = conn.createStatement();
		stmt.execute(sql);

		
	}
	
	public static void createProductTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS product("
					+ "ID INT AUTO_INCREMENT,"
					+ "NAME VARCHAR (255),"
					+ "DIMENSIONS VARCHAR (255),"
					+ "WEIGHT VARCHAR (255),"
					+ "C_ORIGIN VARCHAR (255),"
					+ "PRICE NUMERIC (8,2),"
					+ "PRIMARY KEY (ID)"
					+ ");";
			
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addProduct(Connection conn,
								  int id,
								  String name,
								  String dimensions,
								  String weight,
								  String cOrigin,
								  double price){
		
		String query = String.format("INSERT INTO product "
								   + "VALUES (%d,\'%s\',\'%s\',\'%s\',\'%s\',\'%f\');",
									 id, name, dimensions, weight, cOrigin, price);
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
										
	}
	
	
	public static ResultSet queryProductTable(Connection conn,
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
		
		sb.append("FROM product ");
		
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
	
	
	public static void printProductTable(Connection conn){
		String query = "SELECT * FROM product;";
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			
			while(result.next()){
				System.out.printf("Product %d: %s %s %s %s %f\n",
								  result.getInt(1),
								  result.getString(2),
								  result.getString(3),
								  result.getString(4),
								  result.getString(5),
								  result.getDouble(6));
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static String createProductSQLInsert(ArrayList<Product> products)
	{
		StringBuilder sb = new StringBuilder();

		/**
		 * The start of the statement,
		 * tells it the table to add it to
		 * the order of the data in reference
		 * to the columns to ad dit to
		 */
		sb.append("INSERT INTO product (ID, NAME, DIMENSIONS, WEIGHT, C_ORIGIN, PRICE) VALUES");

		/**
		 * For each person append a (id, first_name, last_name, MI) tuple
		 *
		 * If it is not the last person add a comma to seperate
		 *
		 * If it is the last person add a semi-colon to end the statement
		 */
		for(int i = 0; i < products.size(); i++){
			Product pr = products.get(i);
			sb.append(String.format("(%d,\'%s\',\'%s\',\'%s\',\'%s\',%.2f)",
					pr.getID(), pr.getName(), pr.getDimensions(), pr.getWeight(), pr.getCountry(), pr.getPrice()
			));
			if( i != products.size()-1){
				sb.append(",");
			}
			else{
				sb.append(";");
			}
		}

		return sb.toString();
	}
		
}
