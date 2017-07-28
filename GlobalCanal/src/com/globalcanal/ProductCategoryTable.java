package com.globalcanal;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


/**
 * 
 * Class to make and manipulate a productcategory table
 *
 */
public class ProductCategoryTable {

	public static void populateProductCategoryTableFromCSV(Connection conn,
												   String fileName) throws SQLException{
		
		/*
		 * Similar to the demo, creates a list of ordered product and sends it
		 * to a helper function to build the bulk insert.
		 */

		ArrayList<ProductCategory> productCategorys = new ArrayList<ProductCategory>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = br.readLine()) != null){
				String[] split = line.split(",");
				productCategorys.add(new ProductCategory(
						Integer.parseInt(split[0]),
						split[1]
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
		String sql = createProductCategorySQLInsert(productCategorys);

		/**
		 * Create and execute an SQL statement
		 *
		 * execute only returns if it was successful
		 */
		Statement stmt = conn.createStatement();
		stmt.execute(sql);




	}
	
	public static void createProductCategoryTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS productcategory("
					+ "P_ID INT,"
					+ "NAME VARCHAR (255),"
					+ "PRIMARY KEY (P_ID, NAME),"
					+ "FOREIGN KEY (P_ID) REFERENCES product(P_ID)"
					+ ");";
			
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addProductCategory(Connection conn,
								  int p_id,
								  String name){
		
		String query = String.format("INSERT INTO product "
								   + "VALUES (%d,\'%s\');",
									 p_id, name);
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
										
	}
	
	
	public static ResultSet queryProductCategoryTable(Connection conn,
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
		
		sb.append("FROM productcategory ");
		
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
	
	
	public static void printProductCategoryTable(Connection conn){
		String query = "SELECT * FROM productcategory;";
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			
			while(result.next()){
				System.out.printf("ProductCategory %d: %s\n",
								  result.getInt(1),
								  result.getString(2));
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static String createProductCategorySQLInsert(ArrayList<ProductCategory> productCategorys)
	{
		StringBuilder sb = new StringBuilder();

		/**
		 * The start of the statement,
		 * tells it the table to add it to
		 * the order of the data in reference
		 * to the columns to ad dit to
		 */
		sb.append("INSERT INTO productcategory (P_ID, NAME) VALUES");

		/**
		 * For each person append a (id, first_name, last_name, MI) tuple
		 *
		 * If it is not the last person add a comma to seperate
		 *
		 * If it is the last person add a semi-colon to end the statement
		 */
		for(int i = 0; i < productCategorys.size(); i++){
			ProductCategory pc = productCategorys.get(i);
			sb.append(String.format("(%d,\'%s\')", pc.getPID(), pc.getName()));
			if( i != productCategorys.size()-1){
				sb.append(",");
			}
			else{
				sb.append(";");
			}
		}

		return sb.toString();
	}
		
}
