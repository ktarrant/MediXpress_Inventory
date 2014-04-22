package com.medixpress.Inventory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
	
	   @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_splash);        
	        Thread background = new Thread() {
	            public void run() {
	                try {
	                    // Thread will sleep for 1.5
	                    sleep(1500);
	                    // After 5 seconds redirect to another intent
	                    Intent i =
	                    	new Intent(getBaseContext(),ItemListActivity.class);
	                    startActivity(i);
	                    //Remove activity
	                    finish();
	                } catch (Exception e) {
	                 
	                }
	            }
	        };
	        // start thread
	        background.start();
	    }
	     
	    @Override
	    protected void onDestroy() {
	         
	        super.onDestroy();
	         
	    }
}
