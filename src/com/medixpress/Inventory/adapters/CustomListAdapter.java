package com.medixpress.Inventory.adapters;

import java.util.HashMap;
import java.util.Map;

import android.widget.BaseExpandableListAdapter;

public abstract class CustomListAdapter extends BaseExpandableListAdapter {

	private Map<Integer, Boolean> expandMap = new HashMap<Integer, Boolean>();
	
	public CustomListAdapter() {
		super();
	}
	
	public boolean isExpanded(int groupPosition) {
		if (!expandMap.containsKey(groupPosition)) {
			return false;
		} else {
			return expandMap.get(groupPosition);
		}
	}
	
	@Override
	public void onGroupCollapsed(int position) {
		super.onGroupCollapsed(position);
		expandMap.put(position, false);
	}
	
	@Override
	public void onGroupExpanded(int position) {
		super.onGroupExpanded(position);
		expandMap.put(position, true);
	}
}
