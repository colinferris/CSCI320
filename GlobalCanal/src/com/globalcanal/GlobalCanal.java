import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


/**
 * 
 * Main program of the GlobalCanal database project
 * 
 * 
 *
 */
public class GlobalCanal {

	
	private static Connection conn;
	
	
	/************************************
	 * 
	 * CART FUCTIONALITY
	 * 
	 ************************************/
	
	/**
	 * Adds a product to a user's cart
	 * 
	 * U_Id is the id of the user, P_id is the product id
	 * 
	 */
	public static void addProductToCart(int U_id, int SC_id, int P_id){
		double totalCost;
		String query = String.format("SELECT PRICE FROM PRODUCT WHERE P_ID = %d;", P_id);
		ResultSet rs;
		
		try {
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		totalCost = rs.getDouble(1);
		
		ProductInCartTable.addProductInCart(conn, SC_id, P_id, totalCost);
		updateCartPrice(U_id, SC_id);
	}
	
	/**
	 * Removes a product from  a user's cart
	 * 
	 * @param U_id
	 * @param SC_id
	 * @param P_id
	 */
	public static void removeProductFromCart(int U_id, int SC_id, int P_id){
		String query = String.format("DELETE FROM productincart "
				                   + "WHERE SC_ID = %d AND P_ID = %d", SC_id, P_id);
		
		try {
			Statement stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		updateCartPrice(U_id, SC_id);
	}
	
	/**
	 * Updates the price in a cart to reflect the items in it
	 * 
	 * @param U_id
	 * @param SC_id
	 */
	public static void updateCartPrice(int U_id, int SC_id){
		 
		double totalPrice = 0;
		ResultSet rs;
		ResultSet result2;
		String query = String.format("SELECT TOTALCOST FROM productincart "
					 + "WHERE SC_ID = %d;", SC_id);
		
		try {
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		while(rs.next()){
			totalPrice += rs.getDouble(1);
		}
		
		query = String.format("UPDATE shopping_cart "
				            + "SET TOTALCOST = %f "
				            + "WHERE U_ID = %d AND SC_ID = %d;", totalPrice, U_id, SC_id);
	}
	
	/************************************
	 * 
	 * END BLOCK
	 * 
	 ************************************/
	
	/************************************
	 * 
	 * ORDER FUCTIONALITY
	 * 
	 ************************************/
	
	/**
	 * Creates a table entry for previousOrder based on what was in the user's cart
	 * removes the items from the user's cart, and credits their account
	 * 
	 * provides check to make sure users have the money to buy a product
	 * 
	 * @param U_id
	 * @param SC_id
	 */
	public static void makeOrder(int U_id, int SC_id){
		
		double credit;
		double totalCost;
		ResultSet rs;
		
		String query = String.format("SELECT CREDIT FROM useraccount "
				                   + "WHERE ID = %d;", U_id);
		try {
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		credit = rs.getDouble(1);
		
		
		query = String.format("SELECT TOTALCOST FROM shoppingcart "
                            + "WHERE U_ID = %d AND SC_ID = %d;", U_id, SC_id);
		try {
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
				
		totalCost = rs.getDouble(1);
		
		if(totalCost > credit){
			System.out.println("Insufficent Funds for Purchase.");
			return;
		}
		else{
			//Random number for dates
			int rand = ThreadLocalRandom.current().nextInt(20170701, 202001230);
			
			
			//Find shipping address
			query = String.format("SELECT S_ID FROM shippingaddress "
					            + "WHERE U_ID = %d;", U_id);
			try {
				Statement stmt = conn.createStatement();
				rs = stmt.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			int Shipment_id = rs.getInt(1);
			
			//find payment method
			query = String.format("SELECT P_ID FROM paymentmethod "
		                        + "WHERE U_ID = %d;", U_id);
			
			try {
				Statement stmt = conn.createStatement();
				rs = stmt.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			int Payment_id = rs.getInt(1);
			
			//generate order
			PreviousOrderTable.addPreviousOrder(conn, rand - 1000000, rand, rand+3, totalCost, Payment_id, Shipment_id, U_id);
			
			//get id of the order
			query=String.format("SELECT ID FROM previousorder "
					          + "WHERE DATEOFPURCHASE = %d AND DATEOFSHIPTMENT = %d "
					          + "AND P_ID = %d AND S_ID = %d AND U_id = %d;", 
					          rand, rand+3, Payment_id, Shipment_id, U_id);
			
			try {
				Statement stmt = conn.createStatement();
				rs = stmt.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			int order_id = rs.getInt(1);
			
			//create orderedproducts
			query = String.format("SELECT P_ID FROM productincart "
					            + "WHERE SC_ID = %d;", SC_id);
			try {
				Statement stmt = conn.createStatement();
				rs = stmt.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			int product_id;
			
			while(rs.next()){
				product_id = rs.getInt(1);
				OrderedProductTable.addOrderedProduct(conn, order_id, product_id);
				removeProductFromCart(U_id, SC_id, product_id);
			}
			
			//credit the useraccount 
			double newcredit = credit - totalCost;
			
			query = String.format("UPDATE useraccount"
					            + "SET credit = %f "
					            + "WHERE U_ID = %d;", newcredit, U_id);
		}
			
	}
	
	/************************************
	 * 
	 * END BLOCK
	 * 
	 ************************************/
	
	/************************************
	 * 
	 * ITEM SEARCH
	 * 
	 ************************************/
	
	
	/**
	 * Uses a category name to find any product 
	 * that has a category that contains the given string
	 * as a substring.
	 * 
	 * @param category
	 * @return
	 */
	public static ResultSet searchProductByCategory(String category){
		
		ResultSet rs;
		
		String query = String.format("SELECT * FROM PRODUCT "
				                   + "WHERE P_ID = "
				                   + "(SELECT P_ID FROM productcategory"
				                   + "WHERE NAME = %"
				                   + category
				                   + "%);");
		
		try {
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rs;
	}
	
	
	
}

















