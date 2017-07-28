package com.globalcanal;
/**
 * 
 * 
 *
 */
public class ProductInCart {

	int SC_id;
	int P_id;
	
	public ProductInCart(int SC_id,
						 int P_id){
		
		this.SC_id = SC_id;
		this.P_id = P_id;
		
	}
	
	public int getSCID(){
		return SC_id;
	}
	
	public int getPID(){
		return P_id;
	}
}
