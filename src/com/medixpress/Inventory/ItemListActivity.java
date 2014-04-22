package com.medixpress.Inventory;

import com.medixpress.Inventory.ItemListFragment.Callbacks;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);

		if (findViewById(R.id.item_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ItemListFragment)getFragmentManager().findFragmentById(R.id.item_list))
				.setActivateOnItemClick(true);
		}
		
		// Initialize the ActionBar Tabs
		initTabs();

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
			arguments.putLong(ItemDetailFragment.ARG_ITEM_ID, id);
			arguments.putInt(ItemDetailFragment.ARG_ITEM_TYPEID, 
					getActionBar().getSelectedNavigationIndex());
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
			detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_TYPEID, typeId);
			Log.i(TAG, String.format("%d: %X", typeId, id));
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}
