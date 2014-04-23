package com.medixpress.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.medixpress.Inventory.DataLoader.LoadListener;
import com.medixpress.Inventory.ItemListFragment.Callbacks;
import com.medixpress.Inventory.adapters.CustomListAdapter;
import com.medixpress.Inventory.adapters.OrderListAdapter;
import com.medixpress.Inventory.adapters.ProductListAdapter;
import com.medixpress.Inventory.adapters.ReportListAdapter;
import com.medixpress.SQLite.DatabaseHelper;
import com.medixpress.SQLite.Order;
import com.medixpress.SQLite.Product;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.BaseExpandableListAdapter;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ItemDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details (if present) is a
 * {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required {@link ItemListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class ItemListActivity extends FragmentActivity implements
		ItemListFragment.Callbacks, ActionBar.TabListener {
	private static final String TAG = "ItemListActivity";

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	
	/**
	 * DataLoader object - manages our Database
	 */
	private static DataLoader loader = null;
	
	/**
	 * Bundles for Adapter states
	 */
	private HashMap<String, Bundle> bundles = new HashMap<String, Bundle>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);
		
		loader = new DataLoader();
		loader.registerLoadListener(loadListener);
		loader.loadAll(this);
		
		ItemListFragment listFragment = ((ItemListFragment)getFragmentManager().
				findFragmentById(R.id.item_list));

		if (findViewById(R.id.item_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			listFragment.setActivateOnItemClick(true);
		}
		
		// Initialize the ActionBar Tabs
		initTabs();
		// Initalize ItemListFragment with current Tab
		updateListFragment();
		// TODO: If exposing deep links into your app, handle intents here.
	}
	
	/**
	 * Create the ActionBar Tabs
	 */
	private void initTabs() {
		 // setup action bar for tabs
	    ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    //actionBar.setDisplayShowTitleEnabled(false);

    	Tab tab = actionBar.newTab()
    			.setText(getString(R.string.tab_orders))
    			.setTabListener(this);
    	actionBar.addTab(tab);
    	tab = actionBar.newTab()
    			.setText(getString(R.string.tab_products))
    			.setTabListener(this);
    	actionBar.addTab(tab);
    	tab = actionBar.newTab()
    			.setText(getString(R.string.tab_reports))
    			.setTabListener(this);
    	actionBar.addTab(tab);
	}

	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(long id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			int typeId = getActionBar().getSelectedNavigationIndex();
			arguments.putLong(ItemDetailFragment.ARG_ITEM_ID, 
					id);
			arguments.putInt(ItemDetailFragment.ARG_ITEM_TYPEID, 
					typeId);
			ItemDetailFragment fragment = new ItemDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Integer typeId = (Integer)getActionBar().getSelectedNavigationIndex();
			Intent detailIntent = new Intent(this, ItemDetailActivity.class);
			detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, (Long)id);
			detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_TYPEID, (Integer)typeId);
			startActivity(detailIntent);
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * Changes the ItemList to 
	 */

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		updateListFragment();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		ItemListFragment listFragment = ((ItemListFragment)getFragmentManager().
				findFragmentById(R.id.item_list));
		CustomListAdapter adapter = listFragment.getAdapter();
		Bundle b = new Bundle();
		for (int i = 0;i < adapter.getGroupCount(); i++) {
			b.putBoolean("GROUP_"+i, adapter.isExpanded(i));
		}
		bundles.put((String) tab.getText(), b);
	}
	
	private void updateListFragment() {
		ItemListFragment listFragment = ((ItemListFragment)getFragmentManager().
				findFragmentById(R.id.item_list));
		final ActionBar bar = getActionBar();
		String tabText = (String) bar
				.getTabAt(bar.getSelectedNavigationIndex()).getText();
		CustomListAdapter adapter = getListAdapter(tabText);
		listFragment.setAdapter(adapter);
		if (bundles.get(tabText) != null) {
			Bundle b = bundles.get(tabText);
			for (int i = 0;i < adapter.getGroupCount(); i++) {
				if (b.getBoolean("GROUP_"+i)) {
					listFragment.expand(i);
				}
			}
			bundles.put(tabText, null);
		}
	}
	
	private LoadListener loadListener = new LoadListener() {
		@Override
		public void OnLoadFinished() {
			updateListFragment();
		}
	};
	
	/**
	 * Creates a ExpandableListAdapter for the given tabIndex
	 * @param tabIndex index of the ActionBar Tabs
	 */
	public CustomListAdapter getListAdapter(String tab) {
		Resources res = getResources();
		CustomListAdapter rval = null;
		if (tab.equals(getString(R.string.tab_orders))) {
			// ORders tab
			ArrayList<String> listDataHeader = new ArrayList<String>(Arrays.asList(
					res.getStringArray(R.array.order_types)));
			List<Order> orders = loader.getOrders();
			if (orders == null) {
				orders = new ArrayList<Order>();
			}
			rval = new OrderListAdapter(this, listDataHeader, orders);
		} else if (tab.equals(getString(R.string.tab_products))) {
			// Products tab
			ArrayList<String> listDataHeader = new ArrayList<String>(Arrays.asList(
					res.getStringArray(R.array.product_types)));
			List<Product> products = loader.getProducts();
			if (products == null) {
				products = new ArrayList<Product>();
			}
			rval = new ProductListAdapter(this, listDataHeader, products);
		} else if (tab.equals(getString(R.string.tab_reports))) {
			// Reports tab
			ArrayList<String> listDataHeader = new ArrayList<String>(Arrays.asList(
					res.getStringArray(R.array.report_types)));
			Map<String, List<String>> listChildData = 
					new HashMap<String, List<String>>();
			ArrayList<String> children = new ArrayList<String>(Arrays.asList(
					res.getStringArray(R.array.report_types_sales)));
			listChildData.put(listDataHeader.get(0), children);
			children = new ArrayList<String>(Arrays.asList(
					res.getStringArray(R.array.report_types_bvw)));
			listChildData.put(listDataHeader.get(1), children);
			children = new ArrayList<String>(Arrays.asList(
					res.getStringArray(R.array.report_types_mgmt)));
			listChildData.put(listDataHeader.get(2), children);
			rval = new ReportListAdapter(this, listDataHeader, listChildData);
		}
		return rval;
	}
	
	public static DataLoader getLoader() {
		return loader;
	}
}
