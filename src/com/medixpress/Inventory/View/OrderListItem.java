package com.medixpress.Inventory.View;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.medixpress.Inventory.R;
import com.medixpress.SQLite.Order;
import com.medixpress.SQLite.Product;

public class OrderListItem extends FrameLayout {

	private View rootView = null;
	
	public OrderListItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initView();
	}

	public OrderListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initView();
	}
	
	public OrderListItem(Context context) {
		super(context);
		
		initView();
	}
	
	private void initView() {
		rootView = inflate(getContext(), R.layout.item_order, null);
		this.addView(rootView);
	}
	
	public void setImage(Bitmap img) {
		//ImageView imageView = (ImageView) rootView.findViewById(R.id.productImage);
		//imageView.setImageBitmap(img);
	}
	
	public void setOrder(Order order) {
		TextView titleView = (TextView) rootView.findViewById(R.id.orderTitle);
		TextView descriptionView = (TextView) rootView.findViewById(R.id.orderDescription);
		
		Product product = order.getProduct();
		
		float saleValue = order.getAmount() * product.getValue();
		// titleView has the name
		titleView.setText(Html.fromHtml(
				String.format("<b>$%.2f</b><br>%.2f g of <i>%s</i>",
						saleValue, order.getAmount(),
						product.getName())));
		
		// The description has symptoms and keywords is they are not null
		SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss MM/dd");
		
		descriptionView.setText(Html.fromHtml(String.format(
				"Ordered from <i>%d</i> at <i>%s</i><br>",
				order.getConsumerId(), f.format(new Date(order.getTime())))));
		// TODO: replace consumerId with a Consumer object
	}

}
