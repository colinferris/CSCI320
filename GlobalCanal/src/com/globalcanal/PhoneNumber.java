/**
 * 
 * 
 * 
 *
 */
package com.globalcanal;
public class PhoneNumber {

	int U_id;
	int pNumber;
	
	public PhoneNumber(int U_id,
					   int pNumber){
		this.pNumber = pNumber;
		this.U_id = U_id;
	}
	
	public int getUID(){
		return U_id;
	}
	
	public int getPNumber(){
		return pNumber;
	}
}
