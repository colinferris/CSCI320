package com.globalcanal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


/**
 * 
 * Class to make and manipulate a shippingaddress table
 *
 */
public class ShippingAddressTable {

	public static void populateShippingAddressTableFromCSV(Connection conn,
												   String fileName) throws SQLException{
		
		/*
		 * Similar to the demo, creates a list of ordered product and sends it
		 * to a helper function to build the bulk insert.
		 */

		ArrayList<ShippingAddress> shippingAddress = new ArrayList<ShippingAddress>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = br.readLine()) != null){
				String[] split = line.split(",");
				shippingAddress.add(new ShippingAddress(
						Integer.parseInt(split[0]),
						Integer.parseInt(split[1]),
						split[2],
						split[3],
						split[4],
						split[5],
						split[6],
						Integer.valueOf(split[7])
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
		String sql = createShippingAddressSQLInsert(shippingAddress);

		/**
		 * Create and execute an SQL statement
		 *
		 * execute only returns if it was successful
		 */
		Statement stmt = conn.createStatement();
		stmt.execute(sql);
		
	}
	
	public static void createShippingAddressTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS shippingaddress("
					+ "U_ID INT,"
					+ "S_ID INT AUTO_INCREMENT,"
					+ "STREETNUM VARCHAR (255),"
					+ "STREETNAME VARCHAR (255),"
					+ "APTNUM VARCHAR (255),"
					+ "CITY VARCHAR (255),"
					+ "STATE VARCHAR(255),"
					+ "ZIPCODE INT,"
					+ "PRIMARY KEY (U_ID, S_ID),"
					+ "FOREIGN KEY (U_ID) REFERENCES useraccount(ID)"
					+ ");";
			
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addShippingAddress(Connection conn, ShippingAddress address){
		String query = String.format("INSERT INTO shippingaddress (U_ID, STREETNUM, STREETNAME, APTNUM, CITY, STATE, ZIPCODE) "
								   + "VALUES (%d,\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',%d);",
									 address.getUID(), address.getStreetNum(), address.getStreetName(), address.getAptNum(), address.getCity(), address.getState(), address.getZip());
		try {
			System.out.println(query);
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
										
	}
	
	
	public static ResultSet queryShippingAddressTable(Connection conn,
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
		
		sb.append("FROM shippingaddress ");
		
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
	
	
	public static void printShippingAddressTable(Connection conn){
		String query = "SELECT * FROM shippingaddress;";
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			
			while(result.next()){
				System.out.printf("ShippingAddress %d %d: %s %s %s %s %s %d\n",
								  result.getInt(1),
								  result.getInt(2),
								  result.getString(3),
								  result.getString(4),
								  result.getString(5),
								  result.getString(6),
								  result.getString(7),
								  result.getInt(8));
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static String createShippingAddressSQLInsert(ArrayList<ShippingAddress> shippingAddress)
	{
		StringBuilder sb = new StringBuilder();

		/**
		 * The start of the statement,
		 * tells it the table to add it to
		 * the order of the data in reference
		 * to the columns to ad dit to
		 */
		sb.append("INSERT INTO shippingaddress (U_ID, S_ID, STREETNUM, STREETNAME, APTNUM, CITY, STATE, ZIPCODE) VALUES");
		
		for(int i = 0; i < shippingAddress.size(); i++){
			ShippingAddress sa = shippingAddress.get(i);
			sb.append(String.format("(%d,%d,\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',%d)",
					sa.getUID(), sa.getSID(), sa.getStreetNum(), sa.getStreetName(), sa.getAptNum(), sa.getCity(), sa.getState(), sa.getZip()
			));
			if( i != shippingAddress.size()-1){
				sb.append(",");
			}
			else{
				sb.append(";");
			}
		}

		return sb.toString();
	}
		
}
