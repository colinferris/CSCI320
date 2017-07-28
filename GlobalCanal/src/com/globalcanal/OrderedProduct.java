/**
 * 
 * 
 *
 */
package com.globalcanal;

public class OrderedProduct {

	int order_ID;
	int product_ID;

	public OrderedProduct(int order_ID,
						  int product_ID){
		this.order_ID = order_ID;
		this.product_ID = product_ID;
	}
	
	public int orderID(){
		return order_ID;
	}
	
	public int productID(){
		return product_ID;
	}
	
}

