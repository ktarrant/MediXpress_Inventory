package com.medixpress.SQLite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {
	
	// Logcat tag
    private static final String TAG = "DatabaseHelper";
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "productManager";
    
    // Table Names
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_VENDORS = "vendors";
    private static final String TABLE_CONSUMERS = "consumers";
    
    // Common column names
    private static final String KEY_CONSUMERID = "consumerId";
    private static final String KEY_VENDORID = "vendorId";
    private static final String KEY_NAME = "name";
 
    // Products table - column names
    private static final String KEY_PRODUCTID = "productId";
    private static final String KEY_STOCK = "stock";
    private static final String KEY_VALUE = "value";
    private static final String KEY_SYMPTOMS = "symptoms";
    private static final String KEY_KEYWORDS = "keywords";
    private static final String KEY_DATE = "date";
    private static final String KEY_TYPEID = "typeId";
    
    // Orders table - column names
    private static final String KEY_ORDERID = "orderId";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_TIME = "time";
 
    // Vendors table - column names
    private static final String KEY_LOCATION = "location";
    private static final String KEY_HOURS = "hours";
    
    // Consumers table - column names
    

    // Table Create Statements
    // Product table create statement
    private static final String CREATE_TABLE_PRODUCTS = 
    		"CREATE TABLE " + TABLE_PRODUCTS + "(" + 
    		KEY_PRODUCTID + " INTEGER PRIMARY KEY," + 
    		KEY_VENDORID + " INTEGER," + 
    		KEY_NAME + " TEXT," + 
    		KEY_STOCK + " REAL," +
    		KEY_VALUE + " REAL," + 
    		KEY_SYMPTOMS + " TEXT," +
            KEY_KEYWORDS + " TEXT," +
    		KEY_TYPEID + " INTEGER" + ")";
    
    // Orders table create statement
    private static final String CREATE_TABLE_ORDERS = 
    		"CREATE TABLE " + TABLE_ORDERS + "(" + 
    		KEY_ORDERID + " INTEGER PRIMARY KEY," + 
    		KEY_CONSUMERID + " INTEGER," + 
    		KEY_VENDORID + " INTEGER," + 
    		KEY_PRODUCTID + " INTEGER," + 
    		KEY_AMOUNT + " REAL, " + 
    		KEY_TIME + " INTEGER)";
 
    // Vendor table create statement
    private static final String CREATE_TABLE_VENDORS = 
    		"CREATE TABLE " + TABLE_VENDORS + "(" + 
    		KEY_VENDORID + " INTEGER PRIMARY KEY," + 
    		KEY_NAME + " TEXT," + 
    		KEY_LOCATION + " TEXT," +
    		KEY_HOURS + " TEXT" + ")";
    
    // Vendor table create statement
    private static final String CREATE_TABLE_CONSUMERS = 
    		"CREATE TABLE " + TABLE_CONSUMERS + "(" + 
    		KEY_CONSUMERID + " INTEGER PRIMARY KEY," + 
    		KEY_NAME + " TEXT)";
 
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_ORDERS);
        db.execSQL(CREATE_TABLE_VENDORS);
        db.execSQL(CREATE_TABLE_CONSUMERS);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSUMERS);
 
        // create new tables
        onCreate(db);
    }
    
    public Product addProduct(Product product) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_VENDORID, product.getVendor().getVendorId());
	    values.put(KEY_NAME, product.getName());
	    values.put(KEY_STOCK, product.getStock());
	    values.put(KEY_VALUE, product.getValue());
	    values.put(KEY_SYMPTOMS, product.getSymptoms());
	    values.put(KEY_KEYWORDS, product.getKeywords());
	    values.put(KEY_TYPEID, product.getTypeId());
	 
	    // insert row
	    product.setProductId(db.insert(TABLE_PRODUCTS, null, values));
	    return product;
    }
    
    public Product getProduct(long productId) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + " WHERE "
                + KEY_PRODUCTID + " = " + productId;
     
        Cursor c = db.rawQuery(selectQuery, null);
     
        if (c != null)
            c.moveToFirst();
     
        Product rval = new Product();
        rval.setProductId(c.getInt(c.getColumnIndex(KEY_PRODUCTID)));
        rval.setVendor(getVendor(c.getInt(c.getColumnIndex(KEY_VENDORID))));
        rval.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        rval.setStock(c.getFloat(c.getColumnIndex(KEY_STOCK)));
        rval.setValue(c.getFloat(c.getColumnIndex(KEY_VALUE)));
        rval.setSymptoms(c.getString(c.getColumnIndex(KEY_SYMPTOMS)));
        rval.setKeywords(c.getString(c.getColumnIndex(KEY_KEYWORDS)));
        //rval.setDate(c.getString(c.getColumnIndex(KEY_DATE)));
        rval.setTypeId(c.getInt(c.getColumnIndex(KEY_TYPEID)));
        
        return rval;
    }
    
    public List<Product> getAllProducts(Vendor vendor) {
        List<Product> products = new ArrayList<Product>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + " WHERE "
        		+ KEY_VENDORID + " = " + vendor.getVendorId();
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Product rval = new Product();
                rval.setProductId(c.getInt(c.getColumnIndex(KEY_PRODUCTID)));
                rval.setVendor(getVendor(c.getInt(c.getColumnIndex(KEY_VENDORID))));
                rval.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                rval.setStock(c.getFloat(c.getColumnIndex(KEY_STOCK)));
                rval.setValue(c.getFloat(c.getColumnIndex(KEY_VALUE)));
                rval.setSymptoms(c.getString(c.getColumnIndex(KEY_SYMPTOMS)));
                rval.setKeywords(c.getString(c.getColumnIndex(KEY_KEYWORDS)));
                rval.setTypeId(c.getInt(c.getColumnIndex(KEY_TYPEID)));
                // adding to todo list
                products.add(rval);
            } while (c.moveToNext());
        }
     
        return products;
    }
    
    public Vendor addVendor(Vendor vendor) {
   	 SQLiteDatabase db = this.getWritableDatabase();
   	 
	    ContentValues values = new ContentValues();
	    //values.put(KEY_VENDORID, 0);//vendor.getVendorId());
	    values.put(KEY_NAME, vendor.getName());
	    values.put(KEY_LOCATION, vendor.getLocation());
	    values.put(KEY_HOURS, vendor.getHours());
	 //
	    // insert row
	    vendor.setVendorId(db.insert(TABLE_VENDORS, null, values));
	    return vendor;
   }
    
   public Vendor getVendor(long vendorId) {
       SQLiteDatabase db = this.getReadableDatabase();
       
       String selectQuery = "SELECT  * FROM " + TABLE_VENDORS + " WHERE "
               + KEY_VENDORID + " = " + vendorId;
       Cursor c = db.rawQuery(selectQuery, null);
    
       if (c != null)
           c.moveToFirst();
    
       Vendor rval = new Vendor();
       rval.setVendorId(c.getInt(c.getColumnIndex(KEY_VENDORID)));
       rval.setName(c.getString(c.getColumnIndex(KEY_NAME)));
       rval.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
       rval.setHours(c.getString(c.getColumnIndex(KEY_HOURS)));
       return rval;
   }
   
   public Order getOrder(long orderId) {
       SQLiteDatabase db = this.getReadableDatabase();
       
       String selectQuery = "SELECT  * FROM " + TABLE_ORDERS + " WHERE "
               + KEY_ORDERID + " = " + orderId;
    
       Cursor c = db.rawQuery(selectQuery, null);
    
       if (c != null)
           c.moveToFirst();
    
       Order rval = new Order();
       rval.setVendor(getVendor(c.getInt(c.getColumnIndex(KEY_VENDORID))));
       rval.setOrderId(c.getInt(c.getColumnIndex(KEY_ORDERID)));
       rval.setProduct(getProduct(c.getInt(c.getColumnIndex(KEY_PRODUCTID))));
       rval.setConsumerId(c.getInt(c.getColumnIndex(KEY_CONSUMERID)));
       rval.setAmount(c.getFloat(c.getColumnIndex(KEY_AMOUNT)));
       //rval.setTime(c.getInt(c.getColumnIndex(KEY_TIME)));
       return rval;
   }
   
   public List<Order> getAllOrders(Vendor vendor) {
       List<Order> orders = new ArrayList<Order>();
       String selectQuery = "SELECT  * FROM " + TABLE_ORDERS + " WHERE "
       		+ KEY_VENDORID + " = " + vendor.getVendorId();
    
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor c = db.rawQuery(selectQuery, null);
    
       // looping through all rows and adding to list
       if (c.moveToFirst()) {
           do {
               Order rval = new Order();
               rval.setVendor(getVendor(c.getInt(c.getColumnIndex(KEY_VENDORID))));
               rval.setOrderId(c.getInt(c.getColumnIndex(KEY_ORDERID)));
               rval.setProduct(getProduct(c.getInt(c.getColumnIndex(KEY_PRODUCTID))));
               rval.setConsumerId(c.getInt(c.getColumnIndex(KEY_CONSUMERID)));
               rval.setAmount(c.getFloat(c.getColumnIndex(KEY_AMOUNT)));
               //rval.setTime(c.getInt(c.getColumnIndex(KEY_TIME)));
               // adding to todo list
               orders.add(rval);
           } while (c.moveToNext());
       }
    
       return orders;
   }
   
   public Order addOrder(Order order) {
   	SQLiteDatabase db = this.getWritableDatabase();
   	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_VENDORID, order.getVendor().getVendorId());
	    values.put(KEY_PRODUCTID, order.getProduct().getProductId());
	    values.put(KEY_CONSUMERID, order.getConsumerId());
	    values.put(KEY_AMOUNT, order.getAmount());
	    //values.put(KEY_TIME, order.getTime());
	    
	    // insert row
	    order.setOrderId(db.insert(TABLE_ORDERS, null, values));
	    return order;
   }
   
   public void reset() {
	   SQLiteDatabase db = this.getWritableDatabase();
	   // clear database
	   db.delete(TABLE_PRODUCTS, null, null);
	   db.delete(TABLE_ORDERS, null, null);
	   db.delete(TABLE_VENDORS, null, null);
	   db.delete(TABLE_CONSUMERS, null, null);
   }
}
