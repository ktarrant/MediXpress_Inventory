package com.medixpress.Inventory;

import com.medixpress.Inventory.View.OrderDetailItem;
import com.medixpress.Inventory.View.ProductDetailItem;
import com.medixpress.SQLite.DatabaseHelper;
import com.medixpress.SQLite.DemoDatabase;
import com.medixpress.SQLite.Order;
import com.medixpress.SQLite.Product;

import android.os.Bundle;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class ItemDetailFragment extends Fragment {
	
	/**
	 * The fragment argument representing the item id that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	
	/**
	 * The fragment argument representing the item typeId that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_TYPEID = "item_typeId";
	
	/**
	 * The order or product id
	 */
	private Long id = null;
	
	/**
	 * Designates the type of id: order, product, vendor, etc.
	 */
	private Integer typeId = null;
	
	/**
	 * the Product object - if we are displaying a product
	 */
	private Product product = null;
	
	/**
	 * the Product object - if we are displaying a product
	 */
	private Order order = null;
	
	/**
	 * The productImage object
	 */
	private Bitmap productImage = null;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	
	/**
	 * The rootView of the fragment
	 */
	private View rootView = null;
	
	public ItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			id = getArguments().getLong(ARG_ITEM_ID);
		}
		if (getArguments().containsKey(ARG_ITEM_TYPEID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			typeId = getArguments().getInt(ARG_ITEM_TYPEID);
		}
		
		if (id != null && typeId != null) {
			DataLoader loader = ItemListActivity.getLoader();
			if (typeId == 0) {
				// Order
				setOrder(loader.getHelper().getOrder(id));
				setProduct(order.getProduct());
				setProductImage(loader.getProductImage(product.getProductId()));
			} else if (typeId == 1) {
				// Product
				setProduct(loader.getHelper().getProduct(id));
				setProductImage(loader.getProductImage(id));
			} else if (typeId == 2) {
				setProductImage(DemoDatabase.getDemoReport(getActivity(), id));
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (typeId != null) {
			if (typeId == 0) {
				// This is an order
				rootView = inflater.inflate(R.layout.fragment_order_detail,
						container, false);
			} else if (typeId == 1) {
				// This is a product
				rootView = inflater.inflate(R.layout.fragment_product_detail,
						container, false);
			} else if (typeId == 2) {
				// This is a report
				rootView = inflater.inflate(R.layout.fragment_report_detail,
						container, false);
			}
		}
		updateContent();
		return rootView;
	}
	
	private void updateContent() {
		if (typeId != null && rootView != null) {
			if (typeId == 0) {
				// For orders
				OrderDetailItem detailItem = 
						(OrderDetailItem) rootView.
						findViewById(R.id.item_detail_order);
				if (order != null) {
					detailItem.setOrder(order);
				}
			}
			if (typeId == 0 || typeId == 1) {
				// Both orders and products display products
				ProductDetailItem detailItem = 
						(ProductDetailItem) rootView.
						findViewById(R.id.item_detail_product);
				if (product != null) {
					detailItem.setProduct(product);
				}
				if (productImage != null) {
					detailItem.setImage(productImage);
				}
			}
			if (typeId == 2) {
				// For reports
				ImageView detailItem = 
						(ImageView) rootView.
						findViewById(R.id.item_detail_report);
				if (productImage != null) {
					detailItem.setImageBitmap(productImage);
				}
			}
		}
	}
	
	public void setProduct(Product product) {
		this.product = product;
		updateContent();
	}
	
	public void setOrder(Order order) {
		this.order = order;
		updateContent();
	}
	
	public void setProductImage(Bitmap image) {
		this.productImage = image;
		updateContent();
	}
}
