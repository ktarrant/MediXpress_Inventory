package com.medixpress.Inventory;



import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;

public class ItemListFragment extends Fragment implements 
	OnChildClickListener {
	private final static String TAG = "OnItemClickListener";
	
	private BaseExpandableListAdapter adapter = null;
	private ExpandableListView rootView = null;

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(long id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(long id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Do nothing
	
	}
	
	public void setAdapter(BaseExpandableListAdapter adapter) {
		this.adapter = adapter;
		if (rootView != null) {
			rootView.setAdapter(this.adapter);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	    // Get the View holding our ExpandableListView
	    View view = inflater.inflate(R.layout.fragment_item_list, container, false);
	    // Get the ExpandableListView
	    rootView = (ExpandableListView) view.findViewById(R.id.list_content);
	    // Assign our adapter
	    if (adapter != null) {
			rootView.setAdapter(adapter);
	    }
	    rootView.setOnChildClickListener(this);
	    return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		rootView.setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int flattenedPosition) {
		if (flattenedPosition == ListView.INVALID_POSITION) {
			rootView.setItemChecked(flattenedPosition, false);
		} else {
			rootView.setItemChecked(flattenedPosition, true);
		}

		mActivatedPosition = flattenedPosition;
	}
	
	private void setActivatedPosition(int groupPosition, int childPosition) {
		int index = rootView.getFlatListPosition(ExpandableListView.
				getPackedPositionForChild(groupPosition, childPosition));
		setActivatedPosition(index);
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		
		setActivatedPosition(groupPosition, childPosition);
		
		mCallbacks.onItemSelected(adapter.getChildId(groupPosition, childPosition));
		
		return false;
	}
	
	public void expand(int groupPosition) {
		rootView.expandGroup(groupPosition);
	}
}
