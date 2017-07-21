/**
 * 
 * 
 * 
 *
 */
public class ShippingAddress {
	
	int U_id;
	int S_id;
	
	String streetNum;
	String streetName;
	String aptNum;
	String city;
	String state;
	int zipCode;
	
	public ShippingAddress(int U_id,
						   int S_id,
						   String streetNum,
						   String streetName,
						   String aptNum,
						   String City,
						   String State,
						   int zipcode){
		
		this.U_id = U_id;
		this.S_id = S_id;
		this.streetNum = streetNum;
		this.streetName = streetName;
		this.aptNum = aptNum;
		this.city = City;
		this.state = State;
		this.zipCode = zipcode;
		
		
	}
	
	public int getUID(){
		return U_id;
	}
	
	public int getSID(){
		return S_id;
	}
	
	public String getStreetNum(){
		return streetNum;
	}
	
	public String getStreetName(){
		return streetName;
	}
	
	public String getAptNum(){
		return aptNum;
	}
	
	public String getCity(){
		return city;
	}
	
	public String getState(){
		return state;
	}
	
	public int getZip(){
		return zipCode;
	}
	
}
