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
		 * 
		 * Fill this out when a CSV format is known
		 * 
		 * 
		 * 
		 */
		
		
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
		
}
