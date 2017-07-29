package com.globalcanal;

/**
 * 
 * 
 * 
 *
 */
public class Product {

	int id;
	String name;
	String dimensions; //Form of "#x#x# UNIT" where # is a number and UNIT is a unit of measurement
	String weight; //Form of "# UNIT" follows same logic as above
	String cOrigin; //Country of origin
	Double price;
	
	public Product(int id,
				   String name,
				   String dimensions,
				   String weight,
				   String cOrigin,
				   double price
				   ){
		this.id = id;
		this.name = name;
		this.dimensions = dimensions;
		this.weight = weight;
		this.cOrigin = cOrigin;
		this.price = price;		
	}
	
	public int getID(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDimensions(){
		return dimensions;
	}
	
	public String getWeight(){
		return weight;
	}
	
	public String getCountry(){
		return cOrigin;
	}
	
	public double getPrice(){
		return price;
	}
	
}
