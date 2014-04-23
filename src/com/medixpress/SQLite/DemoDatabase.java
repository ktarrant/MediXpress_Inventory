package com.medixpress.SQLite;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DemoDatabase {
	// Creates a demonstration database

	private final static String TAG = "DemoDatabase";
	// Creates a fake database for a vendor and returns the vendorId
	public static long createDemoDatabase(DatabaseHelper helper) {

		Vendor v1 = helper.addVendor(new Vendor("Danktown Express", "Boulder, Colorado", "8am - 7pm"));
		// Create fake products
		// Flowers
		Product v1_p1 = helper.addProduct(new Product(v1, "Sloppy Kush", 7.0f, 60.0f, 0));
		Product v1_p2 = helper.addProduct(new Product(v1, "OG Banana", 3.5f, 75.0f, 0));
		Product v1_p3 = helper.addProduct(new Product(v1, "Buddha Blaster", 70.0f, 60.0f, 0));
		Product v1_p4 = helper.addProduct(new Product(v1, "Diamond Sack", 60.f, 75.0f, 0));
		Product v1_p5 = helper.addProduct(new Product(v1, "Saggy Skunk", 44.0f, 56.0f, 0));
		// Edibles
		Product v1_p6 = helper.addProduct(new Product(v1, "Pot Brownies", 1.0f, 10.0f, 1));
		Product v1_p7 = helper.addProduct(new Product(v1, "Pot-Pop", 1.0f, 3.0f, 1));
		Product v1_p8 = helper.addProduct(new Product(v1, "Smack n' Cheese", 1.0f, 13.0f, 1));
		// Create fake orders
		Date now = new Date();
		Order v1_o1 = helper.addOrder(new Order(v1_p1, v1, 0, 1.0f, 
				now.getTime()-100000));
		Order v1_o2 = helper.addOrder(new Order(v1_p2, v1, 1, 0.5f, 
				now.getTime()-1000000));
		Order v1_o3 = helper.addOrder(new Order(v1_p3, v1, 2, 10.0f, 
				now.getTime()-3000000));
		Order v1_o4 = helper.addOrder(new Order(v1_p5, v1, 3, 7.3f, 
				now.getTime()-10000000));
		// return vendorId
		return v1.getVendorId();
	}
	
	private static HashMap<Integer,ArrayList<Long>> tracker = new HashMap<Integer, ArrayList<Long>>();
	
	public static Bitmap createDemoBitmap(Context context, Product product) {
	    AssetManager assetManager = context.getAssets();
	    
	    long productId = product.getProductId();
	    int typeId = product.getTypeId();
//
	    InputStream istr;
	    Bitmap bitmap = null;
    	if (tracker.get(typeId) == null) {
    		tracker.put(typeId, new ArrayList<Long>());
    	}
    	List<Long> prd = tracker.get(typeId);
    	if (!prd.contains(productId)) {
    		String fn = String.format("demo%d_%d.jpg", typeId, prd.size());
    		//Log.i(TAG, fn);
    		prd.add(productId);
		    try {
		        istr = assetManager.open(fn);
		        bitmap = BitmapFactory.decodeStream(istr);
		    } catch (IOException e) {
		        return null;
		    }
    	}
	    return bitmap;
	}

	public static Bitmap getDemoReport(Context context, Long id) {
	    AssetManager assetManager = context.getAssets();
	    
	    InputStream istr;
	    Bitmap bitmap = null;
		String fn = String.format("report_%d.jpg", id);
		Log.i(TAG, fn);
		//Log.i(TAG, fn);
		/*prd.add(productId);
	    try {
	        istr = assetManager.open(fn);
	        bitmap = BitmapFactory.decodeStream(istr);
	    } catch (IOException e) {
	        return null;
	    }
	    return bitmap;*/
		return null;
	}
}
