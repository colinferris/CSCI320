package com.globalcanal;

/**
 * 
 * 
 * 
 *
 */
public class ProductCategory {

	int P_id;
	String name;
	
	public ProductCategory(int P_id,
						   String name){
		this.P_id = P_id;
		this.name = name;
	}
	
	public int getPID(){
		return P_id;
	}
	
	public String getName(){
		return name;
	}
}
