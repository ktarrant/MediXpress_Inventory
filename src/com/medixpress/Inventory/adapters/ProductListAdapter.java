package com.medixpress.Inventory.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.medixpress.SQLite.Product;

import com.medixpress.Inventory.R;
import com.medixpress.Inventory.View.ProductListItem;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductListAdapter extends BaseExpandableListAdapter {
	private final static String TAG = "ProductListAdapter"; 

	private LayoutInflater inflater = null;
    private Map<Long, Bitmap> productImages = null;
    private Map<Long, ProductListItem> productItems = null;
    private List<String> headerLabels = null;
    private Map<String, List<Product>> headerChildren = null;
	private List<Product> products;
	private Context context;

	public ProductListAdapter(Context context, List<String> headerLabels, 
			List<Product> products) {
		super();
	    this.products = products;
	    this.context = context;
	    this.productImages = new HashMap<Long, Bitmap>();
	    this.productItems = new HashMap<Long, ProductListItem>();
	    inflater = (LayoutInflater) 
	    		context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    this.headerLabels = headerLabels;
	    this.headerChildren = new HashMap<String, List<Product>>();
	    for (String headerLabel : headerLabels) {
	    	this.headerChildren.put(headerLabel, new ArrayList<Product>());
	    }
	    for (Product product : products) {
	    	List<Product> children = this.headerChildren.get(headerLabels.get(product.getTypeId()));
	    	children.add(product);
	    }
	}

	@Override
    public Product getChild(int groupPosition, int childPositon) {
        return this.headerChildren.get(this.headerLabels.get(groupPosition))
                .get(childPositon);//
    }

	@Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final Product product = getChild(groupPosition, childPosition);
        
        //Log.i(TAG, String.format("(%d,%d}: %s", groupPosition, childPosition, product.getName()));
        
        if (convertView == null) {
			convertView = new ProductListItem(context);
        }
		((ProductListItem)convertView).setProduct(product);
		//convertView.setOnClickListener(productPressListener);
		Bitmap productImage = productImages.get(product.getProductId());
		if (productImage != null) {
			((ProductListItem)convertView).setImage(productImage);
		}
		productItems.put(product.getProductId(), (ProductListItem) convertView);

        return convertView;
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getChild(groupPosition, childPosition).getProductId();
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

	public void setProductImages(Map<Long, Bitmap> productImages) {
		this.productImages = productImages;
		for (Product product : products) {
			Bitmap pI = productImages.get(product.getProductId());
			if (pI != null) {
				ProductListItem I = productItems.get(product.getProductId());
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
