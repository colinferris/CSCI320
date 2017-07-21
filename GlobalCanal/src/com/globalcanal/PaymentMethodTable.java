
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
		 * 
		 * Fill this out when a CSV format is known
		 * 
		 * 
		 * 
		 */
		
		
	}
	
	public static void createPaymentMethodTable(Connection conn){
		try{
			String query = "CREATE TABLE IF NOT EXISTS paymentmethod("
					+ "U_ID INT,"
					+ "P_ID INT,"
					+ "CNAME VARCHAR (255),"
					+ "CNUM INT,"
					+ "EXPYEAR INT,"
					+ "EXPMONTH INT,"
					+ "SCODE INT"
					+ "PRIMARY KEY (U_ID, P_ID)"
					+ "FOREIGN KEY (U_ID) REFERENCES useraccount(U_ID)"
					+ ");";
			
			Statement stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addPaymentMethod(Connection conn,
								  int U_id,
								  int p_id,
								  String cName,
								  int cNum,
								  int expYear,
								  int expMonth,
								  int sCode){
		
		String query = String.format("INSERT INTO paymentmethod "
								   + "VALUES (%d,\'%d\',\'%s\',\'%d\',\'%d\',\'%d\',\'%d\');",
									 U_id, p_id, cName, cNum, expYear, expMonth, sCode);
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
				System.out.printf("PaymentMethod %d %d: %s %d %d %d %d\n",
								  result.getInt(1),
								  result.getInt(2),
								  result.getString(3),
								  result.getInt(4),
								  result.getInt(5),
								  result.getInt(6),
								  result.getInt(7));
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
		
}
