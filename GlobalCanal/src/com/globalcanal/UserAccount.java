package com.globalcanal;
/**
 * 
 * Holds data about a UserAccount
 * 
 *
 */
public class UserAccount {

	String fName;
	String lName;
	String mI;
	String pWord;
	
	int id;
	int birthdate; //represent as YYYYMMDD
	double credit;
	
	
	/**
	 * Data is assumed to be parsed and split before constructor is called
	 */
	public UserAccount (int id, 
						String fName,
						String lName,
						String mI,
						String pWord,
						int birthdate,
						double credit){
		
		this.fName = fName;
		this.lName = lName;
		this.mI = mI;
		this.pWord = pWord;
		this.birthdate = birthdate;
		this.id = id;
		this.credit = credit;
	}
	
	public int getId(){
		return id;
	}
	
	public String getFName(){
		return fName;
	}
	
	public String getLName(){
		return lName;
	}
	
	public String getMI(){
		return mI;
	}
	
	public String getPassword(){
		return pWord;
	}
	
	public int getBirthdate(){
		return birthdate;
	}
	
	public double getCredit(){
		return credit;
	}
	
}
