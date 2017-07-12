
import java.sql.*;


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
	
}
