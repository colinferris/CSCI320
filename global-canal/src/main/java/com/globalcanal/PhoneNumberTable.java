package com.globalcanal;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


/**
 * 
 * Class to make and manipulate a phonenumber table
 *
 */
public class PhoneNumberTable {

	public static void populatePhoneNumberTableFromCSV(Connection conn,
												   String fileName) throws SQLException{

		/*
		 * Similar to the demo, creates a list of ordered product and sends it
		 * to a helper function to build the bulk insert.
		 */

		ArrayList<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = br.readLine()) != null){
				String[] split = line.split(",");
				phoneNumbers.add(new PhoneNumber(
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
		String sql = createPhoneNumberSQLInsert(phoneNumbers);

		/**
		 * Create and execute an SQL statement
		 *
		 * execute only returns if it was successful
		 */
		Statement stmt = conn.createStatement();
		stmt.execute(sql);



	}
	
	public static void createPhoneNumberTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS phonenumber("
					+ "U_ID INT,"
					+ "PNUMBER VARCHAR (255),"
					+ "PRIMARY KEY (U_ID, PNUMBER),"
					+ "FOREIGN KEY (U_ID) REFERENCES useraccount(ID)"
					+ ");";
			
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addPhoneNumber(Connection conn,
								  int id,
								  int pNumber){
		
		String query = String.format("INSERT INTO phonenumber "
								   + "VALUES (%d,\'%d\');",
									 id, pNumber);
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
										
	}
	
	
	public static ResultSet queryPhoneNumberTable(Connection conn,
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
		
		sb.append("FROM phonenumber ");
		
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
	
	
	public static void printPhoneNumberTable(Connection conn){
		String query = "SELECT * FROM phonenumber;";
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			
			while(result.next()){
				System.out.printf("PhoneNumber %d: %d\n",
								  result.getInt(1),
								  result.getInt(2));
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static String createPhoneNumberSQLInsert(ArrayList<PhoneNumber> phoneNumbers)
	{
		StringBuilder sb = new StringBuilder();

		/**
		 * The start of the statement,
		 * tells it the table to add it to
		 * the order of the data in reference
		 * to the columns to ad dit to
		 */
		sb.append("INSERT INTO phonenumber (U_ID, PNUMBER) VALUES");

		/**
		 * For each person append a (id, first_name, last_name, MI) tuple
		 *
		 * If it is not the last person add a comma to seperate
		 *
		 * If it is the last person add a semi-colon to end the statement
		 */
		for(int i = 0; i < phoneNumbers.size(); i++){
			PhoneNumber pn = phoneNumbers.get(i);
			sb.append(String.format("(%d,\'%s\')",
					pn.getUID(),
					pn.getPNumber()
			));
			if( i != phoneNumbers.size()-1){
				sb.append(",");
			}
			else{
				sb.append(";");
			}
		}

		return sb.toString();
	}
		
}
