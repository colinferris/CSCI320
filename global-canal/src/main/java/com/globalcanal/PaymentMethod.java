package com.globalcanal;

/**
 * 
 * Represents a credit or debit card
 *
 */
public class PaymentMethod {

	int U_id;
	int P_id;
	String cName;
	String cNum;
	int expMonth;
	int expYear;
	int sCode; //security code
	
	public PaymentMethod(int U_id,
						 int P_id,
						 String cName,
						 String cNum,
						 int expMonth,
						 int expYear,
						 int sCode){
		
		this.U_id = U_id;
		this.P_id = P_id;
		this.cName = cName;
		this.cNum = cNum;
		this.expMonth = expMonth;
		this.expYear = expYear;
		this.sCode = sCode;
	}
	
	public int getUID(){
		return U_id;
	}
	
	public int getPID(){
		return P_id;
	}
	
	public String getCName(){
		return cName;
	}
	
	public String getCNum(){
		return cNum;
	}
	
	public int getExpMonth(){
		return expMonth;
	}
	
	public int getExpYear(){
		return expYear;
	}
	
	public int getSCode(){
		return sCode;
	}
	
}
