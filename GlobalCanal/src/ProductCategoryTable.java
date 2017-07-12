import java.sql.*;
import java.util.ArrayList;


/**
 * 
 * Class to make and manipulate a productcategory table
 *
 */
public class ProductCategoryTable {

	public static void populateProductCategoryTableFromCSV(Connection conn,
												   String fileName) throws SQLException{
		
		/*
		 * 
		 * Fill this out when a CSV format is known
		 * 
		 * 
		 * 
		 */
		
		
	}
	
	public static void createProductCategoryTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS productcategory("
					+ "P_ID INT,"
					+ "NAME VARCHAR (255),"
					+ "PRIMARY KEY (P_ID, NAME),"
					+ "FOREIGN KEY (P_ID) REFERENCES product(P_ID)"
					+ ");";
			
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addProductCategory(Connection conn,
								  int p_id,
								  String name){
		
		String query = String.format("INSERT INTO product "
								   + "VALUES (%d,\'%s\');",
									 p_id, name);
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
										
	}
	
	
	public static ResultSet queryProductCategoryTable(Connection conn,
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
		
		sb.append("FROM productcategory ");
		
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
	
	
	public static void printProductCategoryTable(Connection conn){
		String query = "SELECT * FROM productcategory;";
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			
			while(result.next()){
				System.out.printf("ProductCategory %d: %s\n",
								  result.getInt(1),
								  result.getString(2));
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
		
}
