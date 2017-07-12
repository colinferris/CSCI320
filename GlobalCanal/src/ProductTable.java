
import java.sql.*;
import java.util.ArrayList;


/**
 * 
 * Class to make and manipulate a product table
 *
 */
public class ProductTable {

	public static void populateProductTableFromCSV(Connection conn,
												   String fileName) throws SQLException{
		
		/*
		 * 
		 * Fill this out when a CSV format is known
		 * 
		 * 
		 * 
		 */
		
		
	}
	
	public static void createProductTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS product("
					+ "ID INT PRIMARY KEY,"
					+ "NAME VARCHAR (255),"
					+ "DIMENSIONS VARCHAR (255),"
					+ "WEIGHT VARCHAR (255),"
					+ "C_ORIGIN VARCHAR (255),"
					+ "PRICE NUMERIC (8,2),"
					+ ");";
			
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addProduct(Connection conn,
								  int id,
								  String name,
								  String dimensions,
								  String weight,
								  String cOrigin,
								  double price){
		
		String query = String.format("INSERT INTO product "
								   + "VALUES (%d,\'%s\',\'%s\',\'%s\',\'%s\',\'%f\');",
									 id, name, dimensions, weight, cOrigin, price);
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
										
	}
	
	
	public static ResultSet queryProductTable(Connection conn,
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
		
		sb.append("FROM product ");
		
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
	
	
	public static void printProductTable(Connection conn){
		String query = "SELECT * FROM product;";
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			
			while(result.next()){
				System.out.printf("Product %d: %s %s %s %s %f",
								  result.getInt(1),
								  result.getString(2),
								  result.getString(3),
								  result.getString(4),
								  result.getString(5),
								  result.getDouble(6));
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
}
