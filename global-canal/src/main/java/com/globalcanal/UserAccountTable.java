package com.globalcanal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * 
 * Class to make and manipulate a useraccount table
 *
 */
public class UserAccountTable {

	public static void populateUserAccountTableFromCSV(Connection conn,
												   String fileName) throws SQLException{
		/*
		 * Similar to the demo, creates a list of ordered product and sends it
		 * to a helper function to build the bulk insert.
		 */

		ArrayList<UserAccount> userAccounts = new ArrayList<UserAccount>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = br.readLine()) != null){
				String[] split = line.split(",");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				Date date = df.parse(split[4]);
				userAccounts.add(new UserAccount(
						Integer.parseInt(split[0]),
						split[1],
						split[2],
						split[3],
						split[5],
						date,
						Double.parseDouble(split[6])

				));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		/**
		 * Creates the SQL query to do a bulk add of all people
		 * that were read in. This is more efficent then adding one
		 * at a time
		 */
		String sql = createUserAccountSQLInsert(userAccounts);

		/**
		 * Create and execute an SQL statement
		 *
		 * execute only returns if it was successful
		 */
		Statement stmt = conn.createStatement();
		stmt.execute(sql);
	}
	
	public static void createUserAccountTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS useraccount("
					+ "ID INT AUTO_INCREMENT,"
					+ "FIRST_NAME VARCHAR (255),"
					+ "LAST_NAME VARCHAR (255),"
					+ "MI CHAR (5),"
					+ "BIRTHDATE DATE,"
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
								  String fName,
								  String lName,
								  String MI,
								  java.util.Date birthdate,
								  String password,
								  double credit){
		
		String query = String.format("INSERT INTO useraccount (FIRST_NAME, LAST_NAME, MI, BIRTHDATE, PASSWORD, CREDIT) "
								   + "VALUES (\'%s\',\'%s\',\'%s\',\'%tF\',\'%s\',\'%.2f\');",
									 fName, lName, MI, birthdate, password, credit);
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
				System.out.printf("UserAccount %d: %s %s %s %tF %s %f\n",
								  result.getInt(1),
								  result.getString(2),
								  result.getString(3),
								  result.getString(4),
								  result.getDate(5),
								  result.getString(6),
								  result.getDouble(7));
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static void updateUserName(Connection conn, UserAccount user){
		String query = String.format("UPDATE useraccount SET FIRST_NAME = \'%s\', LAST_NAME = \'%s\', MI = \'%s\';",
				user.getFName(), user.getLName(), user.getMI());
		try{
			System.out.println(query);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String createUserAccountSQLInsert(ArrayList<UserAccount> userAccounts)
	{
		StringBuilder sb = new StringBuilder();

		/**
		 * The start of the statement,
		 * tells it the table to add it to
		 * the order of the data in reference
		 * to the columns to ad dit to
		 */
		sb.append("INSERT INTO useraccount (ID, FIRST_NAME, LAST_NAME, MI, BIRTHDATE, PASSWORD, CREDIT) VALUES");

		for(int i = 0; i < userAccounts.size(); i++){
			UserAccount ua = userAccounts.get(i);
			sb.append(String.format("(%d,\'%s\',\'%s\',\'%s\',\'%tF\',\'%s\',%.2f)",
					ua.getId(), ua.getFName(), ua.getLName(), ua.getMI(), ua.getBirthdate(), ua.getPassword(), ua.getCredit()
			));
			if( i != userAccounts.size()-1){
				sb.append(",");
			}
			else{
				sb.append(";");
			}
		}

		return sb.toString();
	}








}
