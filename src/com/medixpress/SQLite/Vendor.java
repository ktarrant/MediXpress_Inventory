package com.medixpress.SQLite;

public class Vendor {
	
	long vendorId;
	String name;
	String location;
	String hours;
	
	// constructors
	public Vendor(String name, String location, String hours) {
		this.vendorId = 0;
		this.name = name;
		this.location = location;
		this.hours = hours;
	}
	public Vendor(String name) {
		this(name, null, null);
	}
	public Vendor() {
	}
	
	// setters
	public void setVendorId(long vendorId) { this.vendorId = vendorId; }
	public void setName(String name) { this.name = name; }
	public void setLocation(String location) { this.location = location; }
	public void setHours(String hours) { this.hours = hours; }
	
	// getters
	public long getVendorId() { return this.vendorId; }
	public String getName() { return this.name; }
	public String getLocation() { return this.location; }
	public String getHours() { return this.hours; }
}
