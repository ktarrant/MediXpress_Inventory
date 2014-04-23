package com.medixpress.Inventory.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.medixpress.Inventory.R;
import com.medixpress.Inventory.View.ProductListItem;
import com.medixpress.SQLite.Product;

public class ReportListAdapter extends CustomListAdapter {
	private final static String TAG = "ReportListAdapter"; 

	private LayoutInflater inflater = null;
    private Map<Long, Bitmap> reportImages = null;
    private List<String> headerLabels = null;
    private Map<String, List<String>> headerChildren = null;
	private Context context;

	public ReportListAdapter(Context context, List<String> headerLabels, 
			Map<String, List<String>> headerChildren) {
		super();
	    this.context = context;
	    this.reportImages = new HashMap<Long, Bitmap>();
	    inflater = (LayoutInflater) 
	    		context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    this.headerLabels = headerLabels;
	    this.headerChildren = headerChildren;
	}

	@Override
    public String getChild(int groupPosition, int childPositon) {
        return this.headerChildren.get(this.headerLabels.get(groupPosition))
                .get(childPositon);//
    }

	@Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final String child = getChild(groupPosition, childPosition);
        
        //Log.i(TAG, String.format("(%d,%d}: %s", groupPosition, childPosition, product.getName()));
        
        if (convertView == null) {
			convertView = new TextView(context);
        }
		((TextView)convertView).setText(child);

        return convertView;
    }
	
	private int getFlattenedIndex(int groupPosition, int childPosition) {
		if (groupPosition > headerChildren.size()) {
			return -1;
		}
		int index = 0;
		for (int i = 0; i < groupPosition; i++) {
			index += headerChildren.get(headerLabels.get(i)).size();
		}//
		index += childPosition;
		return index;
	}
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getFlattenedIndex(groupPosition, childPosition);
    }
    
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.headerChildren.get(this.headerLabels.get(groupPosition))
                .size();
    }
 
    @Override
    public String getGroup(int groupPosition) {
        return this.headerLabels.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this.headerLabels.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_header, null);
        }
 
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.header_text);
        lblListHeader.setText(headerTitle);
        
        
        return convertView;
    }
	
	@Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
