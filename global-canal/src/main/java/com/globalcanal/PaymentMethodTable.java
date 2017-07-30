package com.globalcanal;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * 
 * Class to make and manipulate a paymentmethod table
 *
 */
public class PaymentMethodTable {

	public static void populatePaymentMethodTableFromCSV(Connection conn,
												   String fileName) throws SQLException{

				/*
		 * Similar to the demo, creates a list of ordered product and sends it
		 * to a helper function to build the bulk insert.
		 */

		ArrayList<PaymentMethod> paymentMethods = new ArrayList<PaymentMethod>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = br.readLine()) != null){
				String[] split = line.split(",");
				paymentMethods.add(new PaymentMethod(
						Integer.parseInt(split[0]),
						Integer.parseInt(split[1]),
						split[2],
						split[3],
						Integer.parseInt(split[5]),
						Integer.parseInt(split[4]),
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
		String sql = createPaymentMethodSQLInsert(paymentMethods);

		/**
		 * Create and execute an SQL statement
		 *
		 * execute only returns if it was successful
		 */
		Statement stmt = conn.createStatement();
		stmt.execute(sql);
		
	}
	
	public static void createPaymentMethodTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS paymentmethod("
					+ "U_ID INT,"
					+ "P_ID INT AUTO_INCREMENT,"
					+ "CNAME VARCHAR (255),"
					+ "CNUM VARCHAR (255),"
					+ "EXPYEAR INT,"
					+ "EXPMONTH INT,"
					+ "SCODE INT,"
					+ "PRIMARY KEY (U_ID, P_ID),"
					+ "FOREIGN KEY (U_ID) REFERENCES useraccount(ID)"
					+ ");";
			
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addPaymentMethod(Connection conn,
								  int U_id,
								  String cName,
								  long cNum,
								  int expYear,
								  int expMonth,
								  int sCode){

		String query = String.format("INSERT INTO paymentmethod (U_ID, CNAME, CNUM, EXPYEAR, EXPMONTH, SCODE) "
						+ "VALUES (%d,\'%s\',%d,%d,%d,%d);",
				U_id, cName, cNum, expYear, expMonth, sCode);
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
										
	}
	
	
	public static ResultSet queryPaymentMethodTable(Connection conn,
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
		
		sb.append("FROM paymentmethod ");
		
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
	
	
	public static void printPaymentMethodTable(Connection conn){
		String query = "SELECT * FROM paymentmethod;";
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			
			while(result.next()){
				System.out.printf("PaymentMethod %d: %s %d %d %d %d\n",
								  result.getInt(2),
								  result.getString(3),
								  result.getString(4),
								  result.getInt(5),
								  result.getInt(6),
								  result.getInt(7));
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static void printPaymentMethodByUID(Connection conn, int UID){
		String query = String.format("SELECT * FROM paymentmethod WHERE U_ID = %d;", UID);
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);

			while(result.next()){
				System.out.printf("PaymentMethod %d: %s %s %d %d %d\n",
						result.getInt(2),
						result.getString(3),
						result.getString(4),
						result.getInt(5),
						result.getInt(6),
						result.getInt(7));
			}

		} catch (SQLException e){
			e.printStackTrace();
		}
	}



	public static String createPaymentMethodSQLInsert(ArrayList<PaymentMethod> paymentMethods)
	{
		StringBuilder sb = new StringBuilder();

		/**
		 * The start of the statement,
		 * tells it the table to add it to
		 * the order of the data in reference
		 * to the columns to ad dit to
		 */
		sb.append("INSERT INTO paymentmethod (U_ID, P_ID, CNAME, CNUM, " +
				"EXPMONTH, EXPYEAR, SCODE) VALUES");

		/**
		 * For each person append a (id, first_name, last_name, MI) tuple
		 *
		 * If it is not the last person add a comma to seperate
		 *
		 * If it is the last person add a semi-colon to end the statement
		 */
		for(int i = 0; i < paymentMethods.size(); i++){
			PaymentMethod pm = paymentMethods.get(i);
			sb.append(String.format("(%d,%d,\'%s\',\'%s\',%d,%d,%d)",
					pm.getUID(),
					pm.getPID(),
					pm.getCName(),
					pm.getCNum(),
					pm.getExpMonth(),
					pm.getExpYear(),
					pm.getSCode()
			));
			if( i != paymentMethods.size()-1){
				sb.append(",");
			}
			else{
				sb.append(";");
			}
		}

		return sb.toString();
	}
		
}
