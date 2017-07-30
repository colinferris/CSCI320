package com.globalcanal;

import java.util.Date;

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
	Date birthdate; //represent as YYYYMMDD
	double credit;
	
	
	/**
	 * Data is assumed to be parsed and split before constructor is called
	 */
	public UserAccount (int id, 
						String fName,
						String lName,
						String mI,
						String pWord,
						Date birthdate,
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
	
	public Date getBirthdate(){
		return birthdate;
	}
	
	public double getCredit(){
		return credit;
	}
	
}
