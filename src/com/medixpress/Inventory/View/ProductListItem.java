package com.medixpress.Inventory.View;

import com.medixpress.Inventory.R;
import com.medixpress.SQLite.Product;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ProductListItem extends FrameLayout {

	private View rootView = null;

	public ProductListItem(Context context, AttributeSet attrs, int defStyle) {
		super (context, attrs, defStyle);

		initView(context);
	}

	public ProductListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}


	public ProductListItem(Context context) {
		super(context);

		initView(context);
	}

	private void initView(Context context) {
		rootView = inflate(context, R.layout.item_product, this);
	}

	public void setImage(Bitmap img) {
		//ImageView imageView = (ImageView) rootView.findViewById(R.id.productImage);
		//imageView.setImageBitmap(img);
	}

	public void setProduct(Product product) {
		//ImageView imageView = (ImageView) rootView.findViewById(R.id.productImage);
		TextView titleView = (TextView) rootView.findViewById(R.id.productTitle);
		TextView summaryView = (TextView) rootView.findViewById(R.id.productSummary);

		// Get image from HashMap
		/*if (productImages.containsKey(product.getProductId())) {
			imageView.setImageBitmap(productImages.get(product.getProductId()));
		}*/

		// titleView has the name
		titleView.setText(product.getName());

		// The summary has the stock, value, and total value
		summaryView.setText(Html.fromHtml(
				String.format("Stock: <b>%.2f g</b><br>"+
						"<b>$%.2f per g</b><br>$%.2f value",
				product.getStock(), product.getValue(), 
				product.getStock()*product.getValue())));

		// The description has symptoms and keywords is they are not null
		/*String description = "";
		if (product.getKeywords() != null) {
			description += "<b>Keywords:</b>"+product.getKeywords();
		}
		if (product.getSymptoms() != null) {
			if (description.equals("")) {
				description += "<br />";
			}
			description += "<b>Symptoms Treated:</b>"+product.getSymptoms();
		}
		descriptionView.setText(description);*/
	}
}
