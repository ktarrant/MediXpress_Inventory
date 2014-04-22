package com.medixpress.Inventory.View;

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

public class ProductDetailItem extends FrameLayout {
		private View rootView = null;
		
		private Bitmap img = null;
		
		public ProductDetailItem(Context context, AttributeSet attrs, int defStyle) {
			super (context, attrs, defStyle);
			
			initView();
		}

		public ProductDetailItem(Context context, AttributeSet attrs) {
			super(context, attrs);
			initView();
		}
		
		
		public ProductDetailItem(Context context) {
			super(context);
			
			initView();
		}
		
		private void initView() {
			rootView = inflate(getContext(), R.layout.detail_product, null);
			this.addView(rootView);
		}
		
		public void setImage(Bitmap img) {
			this.img = img;
			if (rootView != null) {
				ImageView imageView = (ImageView) rootView.findViewById(R.id.productImage);
				imageView.setImageBitmap(img);
			}
		}
		
		public void setProduct(Product product) {
			ImageView imageView = (ImageView) rootView.findViewById(R.id.productImage);
			TextView titleView = (TextView) rootView.findViewById(R.id.productTitle);
			TextView summaryView = (TextView) rootView.findViewById(R.id.productSummary);
			TextView descriptionView = (TextView) rootView.findViewById(R.id.productDescription);
			
			// titleView has the name
			titleView.setText(Html.fromHtml("<b>"+product.getName()+"</b>"));
			
			// Set image if we have it
			if (img != null) {
				imageView.setImageBitmap(img);
			}
			
			// The summary has the stock, value, and total value
			summaryView.setText(Html.fromHtml(
					String.format("Stock: <b>%.2f g</b> @ "+
							"<b>$%.2f per g</b> ($%.2f value)",
					product.getStock(), product.getValue(), 
					product.getStock()*product.getValue())));
			
			// The description has symptoms and keywords is they are not null
			String description = "";
			if (product.getKeywords() != null) {
				description += "<b>Keywords:</b>"+product.getKeywords();
			}
			if (product.getSymptoms() != null) {
				if (description.equals("")) {
					description += "<br />";
				}
				description += "<b>Symptoms Treated:</b>"+product.getSymptoms();
			}
			descriptionView.setText(description);
		}
}
