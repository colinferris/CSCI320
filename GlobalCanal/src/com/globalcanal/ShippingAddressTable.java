
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
		 * 
		 * Fill this out when a CSV format is known
		 * 
		 * 
		 * 
		 */
		
		
	}
	
	public static void createShippingAddressTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS shippingaddress("
					+ "U_ID INT,"
					+ "S_ID INT,"
					+ "STREETNUM VARCHAR (255),"
					+ "STREETNAME VARCHAR (255),"
					+ "APTNUM VARCHAR (255),"
					+ "CITY VARCHAR (255),"
					+ "STATE VARCHAR(255),"
					+ "ZIPCODE INT,"
					+ "PRIMARY KEY (U_ID, S_ID),"
					+ "FOREIGN KEY (U_ID) REFERENCES useraccount(U_ID)"
					+ ");";
			
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addShippingAddress(Connection conn,
								  int U_id,
								  int S_id,
								  String streetNum,
								  String streetName,
								  String aptNum,
								  String city,
								  String state,
								  int zipCode){
		
		String query = String.format("INSERT INTO shippingaddress "
								   + "VALUES (%d,\'%d\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%d\');",
									 U_id, S_id, streetNum, streetName, aptNum, city, state, zipCode);
		try {
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
		
}
