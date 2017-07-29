package com.globalcanal;

import java.sql.*;
import java.util.ArrayList;


/**
 * 
 * Class to make and manipulate a useraccount table
 *
 */
public class UserAccountTable {

	public static void populateUserAccountTableFromCSV(Connection conn,
												   String fileName) throws SQLException{
		
		/*
		 * 
		 * Fill this out when a CSV format is known
		 * 
		 * 
		 * 
		 */
		
		
	}
	
	public static void createUserAccountTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS useraccount("
					+ "ID INT,"
					+ "FIRST_NAME VARCHAR (255),"
					+ "LAST_NAME VARCHAR (255),"
					+ "MI CHAR (5),"
					+ "BIRTHDATE INT,"
					+ "PASSWORD VARCHAR(255),"
					+ "CREDIT NUMERIC (8,2),"
					+ "PRIMARY KEY(ID)"
					+ ");";
			
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addUserAccount(Connection conn,
								  int id,
								  String fName,
								  String lName,
								  String MI,
								  int birthdate,
								  String password,
								  double credit){
		
		String query = String.format("INSERT INTO useraccount "
								   + "VALUES (%d,\'%s\',\'%s\',\'%s\',\'%d\',\'%s\',\'%f\');",
									 id, fName, lName, MI, birthdate, password, credit);
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
										
	}
	
	
	public static ResultSet queryUserAccountTable(Connection conn,
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
		
		sb.append("FROM useraccount ");
		
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
	
	
	/*
	 * 
	 */
	public static void printUserAccountTable(Connection conn){
		String query = "SELECT * FROM useraccount;";
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			
			while(result.next()){
				System.out.printf("UserAccount %d: %s %s %s %d %s %f\n",
								  result.getInt(1),
								  result.getString(2),
								  result.getString(3),
								  result.getString(4),
								  result.getInt(5),
								  result.getString(6),
								  result.getDouble(7));
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
}
