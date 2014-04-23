package com.medixpress.Inventory.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.medixpress.SQLite.Order;
import com.medixpress.SQLite.Product;

import com.medixpress.Inventory.R;
import com.medixpress.Inventory.View.OrderListItem;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderListAdapter extends CustomListAdapter {
	private final static String TAG = "OrderListAdapter"; 

	private LayoutInflater inflater = null;
    private Map<Long, Bitmap> consumerImages = null;
    private Map<Long, OrderListItem> orderItems = null;
    private List<String> headerLabels = null;
    private Map<String, List<Order>> headerChildren = null;
	private List<Order> orders;
	private Context context;

	public OrderListAdapter(Context context, List<String> headerLabels, 
			List<Order> orders) {
		super();
	    this.orders = orders;
	    this.context = context;
	    this.consumerImages = new HashMap<Long, Bitmap>();
	    this.orderItems = new HashMap<Long, OrderListItem>();
	    inflater = (LayoutInflater) 
	    		context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    this.headerLabels = headerLabels;
	    this.headerChildren = new HashMap<String, List<Order>>();
	    for (String headerLabel : headerLabels) {
	    	this.headerChildren.put(headerLabel, new ArrayList<Order>());
	    }
	    // TODO: sort into pending and old orders
    	List<Order> children = this.headerChildren.get(headerLabels.get(0));
    	for (Order order : orders) {
    		children.add(order);
    	}
	}

	@Override
    public Order getChild(int groupPosition, int childPositon) {
        return this.headerChildren.get(this.headerLabels.get(groupPosition))
                .get(childPositon);//
    }

	@Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final Order order = getChild(groupPosition, childPosition);
        
        //Log.i(TAG, String.format("(%d,%d}: %s", groupPosition, childPosition, product.getName()));
        
        if (convertView == null) {
			convertView = new OrderListItem(context);
        }
		((OrderListItem)convertView).setOrder(order);
		//productView.setOnClickListener(productPressListener);
		Bitmap consumerImage = consumerImages.get(order.getConsumerId());
		if (consumerImage != null) {
			((OrderListItem)convertView).setImage(consumerImage);
		}
		orderItems.put(order.getOrderId(), (OrderListItem) convertView);

        return convertView;
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getChild(groupPosition, childPosition).getOrderId();
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
        
        /*ImageView img = (ImageView) convertView.findViewById(R.id.header_expander);
        Resources res = parent.getResources();
        if (isExpanded) {
        	img.setImageDrawable(res.getDrawable(R.drawable.expander_ic_maximized));
        } else {
        	img.setImageDrawable(res.getDrawable(R.drawable.expander_ic_minimized));
        }*/
 
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.header_text);
        lblListHeader.setText(headerTitle);
        
        return convertView;
    }

	public void setConsumerImages(Map<Long, Bitmap> consumerImages) {
		this.consumerImages = consumerImages;
		for (Order order : orders) {
			Bitmap pI = consumerImages.get(order.getConsumerId());
			if (pI != null) {
				OrderListItem I = orderItems.get(order.getOrderId());
				if (I != null) {
					I.setImage(pI);
				}
			}
		}
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