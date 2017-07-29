package com.globalcanal;

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
		 * 
		 * Fill this out when a CSV format is known
		 * 
		 * 
		 * 
		 */
		
		
	}
	
	public static void createProductInCartTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS productincart("
					+ "SC_ID INT,"
					+ "P_ID INT,"
					+ "TOTALCOST NUMERIC(8,2),"
					+ "FOREIGN KEY (SC_ID) REFERENCES shoppingcart(SC_ID),"
					+ "FOREIGN KEY (P_ID) REFERENCES product(P_ID),"
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
		
}
