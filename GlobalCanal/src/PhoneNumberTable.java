
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
		 * 
		 * Fill this out when a CSV format is known
		 * 
		 * 
		 * 
		 */
		
		
	}
	
	public static void createPhoneNumberTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS phonenumber("
					+ "U_ID INT,"
					+ "PNUMBER int,"
					+ "PRIMARY KEY (U_ID, PNUMBER),"
					+ "FOREIGN KEY (U_ID) REFERENCES useraccount(U_ID)"
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
				System.out.printf("PhoneNumber %d: %d",
								  result.getInt(1),
								  result.getInt(2));
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
		
}
