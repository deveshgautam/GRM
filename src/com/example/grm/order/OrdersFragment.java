package com.example.grm.order;

import java.io.IOException;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.replicator.Replication;
import com.couchbase.lite.replicator.Replication.ChangeEvent;
import com.example.grm.R;
import com.example.grm.Application;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class OrdersFragment extends Fragment implements Replication.ChangeListener,
		OnItemClickListener, OnItemLongClickListener, OnKeyListener {

    public static final String TAG = "GenieRM - OrdersFragment";

    OrderData testData = null;
	    
    View rootView ;

	public OrdersFragment() {
    // Empty constructor required for fragment subclasses
	}

	private Database getDatabase() {
		Application application = (Application) getActivity().getApplication();
        return application.getDatabase();		
	}
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.d(TAG, "onCreateView");
		try {
			testData = new OrderData(getDatabase());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CouchbaseLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rootView = inflater.inflate(R.layout.fragment_orders_elv, container, false);
				
		ExpandableListView elv = (ExpandableListView) rootView.findViewById(R.id.orders_elv);
		
        elv.setAdapter(new ELVAdapter(getActivity(), testData.parentArray, testData.childArrayOfArray));
        

        // Listview Group click listener
        elv.setOnGroupClickListener(new OnGroupClickListener() {
 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                return false;
            }
        });
 
        // Listview Group expanded listener
        elv.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
            }
        });
 
        // Listview Group collasped listener
        elv.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int groupPosition) {
				// TODO Auto-generated method stub
            }
        });

     	// set the ClickListener to handle the click event on child item
	     	elv.setOnChildClickListener(new OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
	                    int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
					Log.d(TAG, "onChildClick");
				
					String orderId = testData.childArrayOfArray.get(groupPosition).get(childPosition).get("_id").toString();
					Intent i = new Intent(getActivity(), DisplayDetails.class);      
					i.putExtra("order_id", orderId);      
               		startActivity(i);
               		return true;
				}
	         });
		return rootView;
	}

	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changed(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}

