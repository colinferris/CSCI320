package com.globalcanal;

/**
 * 
 * 
 * 
 *
 */
public class PhoneNumber {

	int U_id;
	String pNumber;
	
	public PhoneNumber(int U_id,
					   String pNumber){
		this.pNumber = pNumber;
		this.U_id = U_id;
	}
	
	public int getUID(){
		return U_id;
	}
	
	public String getPNumber(){
		return pNumber;
	}
}
