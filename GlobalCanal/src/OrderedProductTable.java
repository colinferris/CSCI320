
import java.sql.*;
import java.util.ArrayList;


/**
 * 
 * Class to make and manipulate a orderedproduct table
 *
 */
public class OrderedProductTable {

	public static void populateOrderedProductTableFromCSV(Connection conn,
												   String fileName) throws SQLException{
		
		/*
		 * 
		 * Fill this out when a CSV format is known
		 * 
		 * 
		 * 
		 */
		
		
	}
	
	public static void createOrderedProductTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS orderedproduct("
					+ "ORDER_ID INT,"
					+ "PRODUCT_ID INT,"
					+ "PRIMARY KEY (ORDER_ID, PRODUCT_ID),"
					+ "FOREIGN KEY (ORDER_ID) REFERENCES previousorder(ORDER_ID),"
					+ "FOREIGN KEY (PRODUCT_ID) REFERENCES product(PRODUCT_ID),"
					+ ");";
			
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addOrderedProduct(Connection conn,
								  int order_ID,
								  int product_ID){
		
		String query = String.format("INSERT INTO orderedproduct "
								   + "VALUES (%d,\'%d\');",
									 order_ID, product_ID);
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
										
	}
	
	
	public static ResultSet queryOrderedProductTable(Connection conn,
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
		
		sb.append("FROM orderedproduct ");
		
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
	
	
	public static void printOrderedProductTable(Connection conn){
		String query = "SELECT * FROM orderedproduct;";
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			
			while(result.next()){
				System.out.printf("OrderedProduct %d: %d\n",
								  result.getInt(1),
								  result.getInt(2));
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
		
}