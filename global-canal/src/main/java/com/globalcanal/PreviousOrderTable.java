package com.globalcanal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


/**
 * 
 * Class to make and manipulate a previousorder table
 *
 */
public class PreviousOrderTable {

	public static void populatePreviousOrderTableFromCSV(Connection conn,
												   String fileName) throws SQLException{
		/*
		 * Similar to the demo, creates a list of ordered product and sends it
		 * to a helper function to build the bulk insert.
		 */

		ArrayList<PreviousOrder> previousOrders = new ArrayList<PreviousOrder>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = br.readLine()) != null){
				String[] split = line.split(",");
				previousOrders.add(new PreviousOrder(
						Integer.parseInt(split[0]),
						Integer.parseInt(split[1]),
						Integer.parseInt(split[2]),
						Double.parseDouble(split[3]),
						Integer.parseInt(split[4]),
						Integer.parseInt(split[5]),
						Integer.parseInt(split[6])

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
		String sql = createPreviousOrderSQLInsert(previousOrders);

		/**
		 * Create and execute an SQL statement
		 *
		 * execute only returns if it was successful
		 */
		Statement stmt = conn.createStatement();
		stmt.execute(sql);
	}
	
	public static void createPreviousOrderTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS previousorder("
					+ "ID INT,"
					+ "DATEOFPURCHASE INT,"
					+ "DATEOFSHIPMENT INT,"
					+ "TOTALCOST NUMERIC(8,2),"
					+ "P_ID INT,"
					+ "S_ID INT,"
					+ "U_ID INT,"
					+ "PRIMARY KEY (ID),"
					+ "FOREIGN KEY (U_ID) REFERENCES useraccount(U_ID)"
					+ ");";
			
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addPreviousOrder(Connection conn,
								  int id,
								  int dateOfPurchase,
								  int dateOfShipment,
								  double totalCost,
								  int p_ID,
								  int s_ID,
								  int u_ID){
		
		String query = String.format("INSERT INTO previousorder "
								   + "VALUES (%d,\'%d\',\'%d\',\'%f\',\'%d\',\'%d\',\'%d\');",
									 id, dateOfPurchase, dateOfShipment, totalCost, p_ID, s_ID, u_ID);
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
										
	}
	
	
	public static ResultSet queryPreviousOrderTable(Connection conn,
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
		
		sb.append("FROM previousorder ");
		
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
	
	
	public static void printPreviousOrderTable(Connection conn){
		String query = "SELECT * FROM previousorder;";
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			
			while(result.next()){
				System.out.printf("PreviousOrder %d: %d %d %f %d %d %d\n",
								  result.getInt(1),
								  result.getInt(2),
								  result.getInt(3),
								  result.getDouble(4),
								  result.getInt(5),
								  result.getInt(6),
								  result.getInt(7));
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static String createPreviousOrderSQLInsert(ArrayList<PreviousOrder> previousOrders)
	{
		StringBuilder sb = new StringBuilder();

		/**
		 * The start of the statement,
		 * tells it the table to add it to
		 * the order of the data in reference
		 * to the columns to ad dit to
		 */
		sb.append("INSERT INTO shoppingcart (ID, DATEOFPURCHASE, DATEOFSHIPMENT, TOTALCOST, P_ID, S_ID, U_ID) VALUES");

		for(int i = 0; i < previousOrders.size(); i++){
			PreviousOrder po = previousOrders.get(i);
			sb.append(String.format("(%d,%d,%.2f)",
					po.getID(), po.getPurchaseDate(), po.getShipmentDate(), po.getCost(), po.getPaymentID(), po.getShippingID(), po.getUserID()
			));
			if( i != previousOrders.size()-1){
				sb.append(",");
			}
			else{
				sb.append(";");
			}
		}

		return sb.toString();
	}
		
}
