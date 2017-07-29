
import java.sql.*;
import java.util.ArrayList;


/**
 * 
 * Class to make and manipulate a shoppingcart table
 *
 */
public class ShoppingCartTable {

	public static void populateShoppingCartTableFromCSV(Connection conn,
												   String fileName) throws SQLException{
		
		/*
		 * 
		 * Fill this out when a CSV format is known
		 * 
		 * 
		 * 
		 */
		
		
	}
	
	public static void createShoppingCartTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS shoppingcart("
					+ "U_ID INT,"
					+ "SC_ID INT,"
					+ "TOTALCOST NUMERIC (8,2),"
					+ "PRIMARY KEY (U_ID, SC_ID)"
					+ "FOREIGN KEY (U_ID) REFERENCES useraccount(U_ID)"
					+ ");";
			
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addShoppingCart(Connection conn,
								  int U_ID,
								  int SC_ID,
								  double totalCost){
		
		String query = String.format("INSERT INTO shoppingcart "
								   + "VALUES (%d,\'%d\',\'%f\');",
									 U_ID, SC_ID, totalCost);
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
										
	}
	
	
	public static ResultSet queryShoppingCartTable(Connection conn,
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
		
		sb.append("FROM shoppingcart ");
		
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
	
	
	public static void printShoppingCartTable(Connection conn){
		String query = "SELECT * FROM shoppingcart;";
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			
			while(result.next()){
				System.out.printf("ShoppingCart %d: %d %f\n",
								  result.getInt(1),
								  result.getInt(2),
								  result.getDouble(3));
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
		
}
