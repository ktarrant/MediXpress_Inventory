package com.medixpress.Inventory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.medixpress.SQLite.DatabaseHelper;
import com.medixpress.SQLite.DemoDatabase;
import com.medixpress.SQLite.Order;
import com.medixpress.SQLite.Product;
import com.medixpress.SQLite.Vendor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class DataLoader {
	private final static String TAG = "DataLoader";
	private Context mContext = null;
	private DatabaseHelper helper = null;
	private Vendor vendor = null;
	private List<Product> products = null;
	private List<Order> orders = null;
	private Map<Long, Bitmap> productImages = null;
	private ArrayList<LoadListener> listeners
		= new ArrayList<LoadListener>();
	
	public interface LoadListener {
		public void OnLoadFinished();
	}
	
	public void registerLoadListener(LoadListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}
	public void unregisterLoadListener(LoadListener l) {
		listeners.remove(l);
	}
	
	public void loadAll(Context context) {
		new InitDatabase().execute(context);
	}
	
	private void notifyListeners() {
		for (LoadListener listener : listeners) {
			listener.OnLoadFinished();
		}
	}
	
	private class InitDatabase extends AsyncTask<Context, Void, DatabaseHelper> {
		
		protected DatabaseHelper doInBackground(Context...context) {
			mContext = context[0];
			return new DatabaseHelper(context[0]);
		}
		
		protected void onPostExecute(DatabaseHelper h) {
			helper = h;
			Log.i(TAG, "InitDatabase");
			if (helper == null) {
				  Toast.makeText(mContext, 
					"Could not reach MediXpress database. "+
					"Please try again later.", Toast.LENGTH_LONG).show();
			} else {
				// Create a fake database. In the future, a vendorId
				// will be obtained from login which will be used
				// throughout the session
				long vendorId;
				if (vendor == null) {
					vendorId = DemoDatabase.createDemoDatabase(helper);
				} else {
					vendorId = vendor.getVendorId();
				}
				// Load the vendor from the database asynchronously
				new InitVendor().execute(vendorId);
			}
		}
	}
	
	private class InitVendor extends AsyncTask<Long, Void, Vendor> {
		
	  protected Vendor doInBackground(Long... vendorId) {
	      return helper.getVendor(vendorId[0]);
	  }
	
	  protected void onPostExecute(Vendor dv) {
		  vendor = dv;
		  Log.i(TAG, "InitVendor : " + dv.getName());
		  if (vendor == null) {
			  Toast.makeText(mContext, 
				"There was an error logging into MediXpress. "+
				"Please try again later.", Toast.LENGTH_LONG).show();
		  } else {
			  new InitProducts().execute(vendor);
		  }
	  }
	}
	
	private class InitProducts extends AsyncTask<Vendor, Void, List<Product>> {
		
		  protected List<Product> doInBackground(Vendor... vendor) {
		      return helper.getAllProducts(vendor[0]);
		  }
		
		  protected void onPostExecute(List<Product> p) {
			  products = p;
			  Log.i(TAG, "InitProducts : " + p.size() + " products");
			  if (products == null) {
				  Toast.makeText(mContext, 
					"Failed to load products from database. "+
					"Please try again later.", Toast.LENGTH_LONG).show();
			  }
			  new InitOrders().execute(vendor);
		  }
		}
	
	private class InitOrders extends AsyncTask<Vendor, Void, List<Order>> {
		
		  protected List<Order> doInBackground(Vendor... vendor) {
		      return helper.getAllOrders(vendor[0]);
		  }
		
		  protected void onPostExecute(List<Order> o) {
			  orders = o;
			  Log.i(TAG, "InitOrders : " + o.size() + " orders");
			  if (orders == null) {
				  Toast.makeText(mContext, 
					"Failed to load orders from database. "+
					"Please try again later.", Toast.LENGTH_LONG).show();
			  }
			  new LoadImages().execute(products);
		  }
		}
	
	private Bitmap loadImageFromCache(Long productId) {
		File cacheDir = new File(mContext.getCacheDir(), "products");
		cacheDir.mkdirs();
		final String fn = String.format("%016X", productId);
		File[] pFiles = cacheDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.getAbsolutePath().contains(fn)) {
					return true;
				}
				return false;
			}
		});
		if (pFiles.length == 0) {
			return null;
		} else {
			return BitmapFactory.decodeFile(pFiles[0].getAbsolutePath());
		}
	}
	
	public void saveImageToCache(Long productId, Bitmap img) {   
	    File cacheDir = new File(mContext.getCacheDir(), "products");
	    cacheDir.mkdirs();
	    final String fn = String.format("%016X", productId);
	    File cacheFile = new File(cacheDir, fn+".png");   
	    try {      
            cacheFile.createNewFile();       
            FileOutputStream fos = new FileOutputStream(cacheFile);    
            img.compress(CompressFormat.PNG, 100, fos);       
            fos.flush();       
            fos.close();    
          } catch (Exception e) {       
            Log.e("error", "Error when saving image to cache. ", e);    
          }
	}
	
	private class LoadImages extends AsyncTask<List<Product>, Void, Map<Long, Bitmap>> {
		
		@Override
		protected Map<Long, Bitmap> doInBackground(List<Product>... products) {
			HashMap<Long, Bitmap> rval = new HashMap<Long, Bitmap>();
			for (Product product : products[0]) {
				// First try to load from cache
				Bitmap img = loadImageFromCache(product.getProductId());
				// If not cache then query server for img
				if (img == null) {
					// TODO: Query server for image if it doesn't work
					img = DemoDatabase.createDemoBitmap(mContext, product);
					if (img != null) {
						saveImageToCache(product.getProductId(), img);
					}
				}
				rval.put(product.getProductId(), img);
			}
			return rval;
		}
		
		@Override
		protected void onPostExecute(Map<Long, Bitmap> img_arr) {
			productImages = img_arr;
			Log.i(TAG, "LoadImages : " + img_arr.size() + " images");
			notifyListeners();
		}
	}

	public List<Product> getProducts() {
		return products;
	}
	public List<Order> getOrders() {
		return orders;
	}
	public DatabaseHelper getHelper() {
		return helper;
	}
	public Bitmap getProductImage(long productId) {
		if (productImages != null)
			return productImages.get(productId);
		else return null;
	}
}
