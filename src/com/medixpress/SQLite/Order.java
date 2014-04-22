package com.medixpress.SQLite;

import android.database.Cursor;

public class Order {
	long orderId;
	Product product;
	Vendor vendor;
	long consumerId;
	float amount;
	long time;
	
	// constructors
	public Order(Product product, Vendor vendor, long consumerId, 
			float amount, long time) {
		this.product = product;
		this.vendor = vendor;
		this.consumerId = consumerId;
		this.amount = amount;
		this.orderId = 0;
		this.time = time;
	}
	
	public Order() {
	}
	
	public Order(Cursor c) {
		this.fromCursor(c);
	}
	
	// setters
	public void setProduct(Product product) { this.product = product; }
	public void setVendor(Vendor vendor) { this.vendor = vendor; }
	public void setConsumerId(long consumerId) { this.consumerId = consumerId; }
	public void setOrderId(long orderId) { this.orderId = orderId; }
	public void setAmount(float amount) { this.amount = amount; }
	public void setTime(long time) { this.time = time; }
	public void fromCursor(Cursor c) {
		
	}
	
	// getters
	public Product getProduct() { return this.product; }
	public Vendor getVendor() { return this.vendor; }
	public long getConsumerId() { return this.consumerId; }
	public long getOrderId() { return this.orderId; }
	public float getAmount() { return this.amount; }
	public long getTime() { return this.time; }

}
