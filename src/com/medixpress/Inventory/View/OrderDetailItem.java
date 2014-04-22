package com.medixpress.Inventory.View;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.medixpress.SQLite.Order;
import com.medixpress.SQLite.Product;

import com.medixpress.Inventory.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderDetailItem extends FrameLayout {
		private View rootView = null;
		
		private Bitmap img = null;
		
		public OrderDetailItem(Context context, AttributeSet attrs, int defStyle) {
			super (context, attrs, defStyle);
			
			initView();
		}

		public OrderDetailItem(Context context, AttributeSet attrs) {
			super(context, attrs);
			initView();
		}
		
		
		public OrderDetailItem(Context context) {
			super(context);
			
			initView();
		}
		
		private void initView() {
			rootView = inflate(getContext(), R.layout.detail_order, null);
			this.addView(rootView);
		}
		
		public void setImage(Bitmap img) {
			this.img = img;
			if (rootView != null) {
				ImageView imageView = (ImageView) rootView.findViewById(R.id.consumerImage);
				imageView.setImageBitmap(img);
			}
		}
		
		public void setOrder(Order order) {
			ImageView imageView = (ImageView) rootView.findViewById(R.id.consumerImage);
			TextView titleView = (TextView) rootView.findViewById(R.id.orderTitle);
			TextView summaryView = (TextView) rootView.findViewById(R.id.orderSummary);
			
			Product product = order.getProduct();
			
			float saleValue = order.getAmount() * product.getValue();
			// titleView has the name
			titleView.setText(Html.fromHtml(
					String.format("<b>$%.2f</b> - <b>%.2f g</b> of "+
							"<b>%s</b>",
							saleValue, order.getAmount(),
							product.getName())));
			
			// The description has symptoms and keywords is they are not null
			SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss MM/dd");
			
			summaryView.setText(Html.fromHtml(String.format(
					"Ordered from <i>%s</i> at <i>%s</i><br>"+
					"%.2f g @ <b>$%.2f per g</b> ($%.2f value)",
					order.getConsumerId(), f.format(new Date(order.getTime())),
					order.getAmount(), product.getValue(),
					saleValue)));
			// TODO: replace consumerId with a Consumer object
		}
}
