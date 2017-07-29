package com.globalcanal;


/**
 * 
 * 
 * 
 *
 */
public class PreviousOrder {

	int id;
	int dateOfPurchase; //YYYYMMDD
	int dateOfShipment; //^^
	double totalCost;
	int p_ID;
	int s_ID;
	int u_ID;
	
	public PreviousOrder(int id,
						 int dateOfPurchase,
						 int dateOfShipment,
						 double totalCost,
						 int payment_ID,
						 int shipping_ID,
						 int user_ID){
		this.id = id;
		this.dateOfPurchase = dateOfPurchase;
		this.dateOfShipment = dateOfShipment;
		this.totalCost = totalCost;
		this.p_ID = payment_ID;
		this.s_ID = shipping_ID;
		this.u_ID = user_ID;
		
	}
	
	public int getID(){
		return id;
	}
	
	public int getPurchaseDate(){
		return dateOfPurchase;
	}
	
	public int getShipmentDate(){
		return dateOfShipment;
	}
	
	public double getCost(){
		return totalCost;
	}
	
	public int getPaymentID(){
		return p_ID;
	}
	
	public int getShippingID(){
		return s_ID;
	}
	
	public int getUserID(){
		return u_ID;
	}
	
}
