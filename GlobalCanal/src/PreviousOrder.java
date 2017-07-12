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
	int payment_ID;
	int shipping_ID;
	
	public PreviousOrder(int id,
						 int dateOfPurchase,
						 int dateOfShipment,
						 double totalCost,
						 int payment_ID,
						 int shipping_ID){
		this.id = id;
		this.dateOfPurchase = dateOfPurchase;
		this.dateOfShipment = dateOfShipment;
		this.totalCost = totalCost;
		this.payment_ID = payment_ID;
		this.shipping_ID = shipping_ID;
		
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
		return payment_ID;
	}
	
	public int getShippingID(){
		return shipping_ID;
	}
	
}
