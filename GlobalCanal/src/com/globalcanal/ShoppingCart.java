package com.globalcanal;
/**
 * 
 * 
 *
 */
public class ShoppingCart {

	int U_id;
	int SC_id;
	double totalCost;
	
	
	public ShoppingCart(int U_id,
						int SC_id,
						double totalCost){
		
		this.U_id = U_id;
		this.SC_id = SC_id;
		this.totalCost = totalCost;
	}
	
	public int getUID(){
		return U_id;
	}
	
	public int getSCID(){
		return SC_id;
	}
	
	public double totalCost(){
		return totalCost;
	}
}
