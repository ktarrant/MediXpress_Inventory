package com.medixpress.Inventory;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class ItemDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	
	/**
	 * The fragment argument representing the item typeId that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_TYPEID = "item_typeId";

	/**
	 * Either the productId or orderId of the item.
	 */
	private Long id = null;
	
	/**
	 * Designates the type of id: order, product, vendor, etc.
	 */
	private Integer typeId = null;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
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
			id = getArguments().getLong(ARG_ITEM_TYPEID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_detail,
				container, false);
//
		// Show the productId dawg
		if (id != null && typeId != null) {
			((TextView) rootView.findViewById(R.id.item_detail))
					.setText(String.format("%d: %X", typeId, id));
		}

		return rootView;
	}
}
