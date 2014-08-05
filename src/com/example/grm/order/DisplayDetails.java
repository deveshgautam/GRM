package com.example.grm.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.couchbase.lite.Database;
import com.example.grm.R;
import com.example.grm.Application;
import com.example.grm.cblite.CBLiteOrder;

import android.app.Activity; 
import android.content.Intent;
import android.os.Bundle; 
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;  
import android.widget.AdapterView.OnItemClickListener;

public class DisplayDetails extends Activity { 
	
	// Declare Variables   
	TextView textView;
	TextView txtdescription; 
	String orderId;    
	LVAdapter adapter;
	ListView list;     

    public static final String TAG = "GenieRM - DisplayDetails";

	@SuppressWarnings("unchecked")
	@Override  
	public void onCreate(Bundle savedInstanceState) {   
		super.onCreate(savedInstanceState);      
		
	    Log.d(TAG, "onCreate");

		setContentView(R.layout.activity_display_order);   
		
		Intent intent = getIntent();       
		orderId = intent.getStringExtra("order_id");  
		
		Log.d(TAG, "onCreate child " +  orderId);
		
		Map<String, Object> singleOrderMap = new HashMap<String, Object>();
		singleOrderMap = CBLiteOrder.getOrderById(getDatabase(), orderId) ;

		Log.d(TAG, "onCreate child " +  1);

	    String custArea = (String) singleOrderMap.get("c_area");
	    String custName = (String) singleOrderMap.get("c_nm");
	    String custPhone = (String) singleOrderMap.get("c_ph");
		
	    textView = (TextView) findViewById(R.id.textCustName1); 
        textView.setText(custName); 
		
        textView = (TextView) findViewById(R.id.textCustAddr1); 
        textView.setText(custArea); 
		
        textView = (TextView) findViewById(R.id.textCustPhone1); 
        textView.setText(custPhone); 
		
		list = (ListView) findViewById(R.id.listview);     

		ArrayList<Map<String, Object>> orderItemMapArray = (ArrayList<Map<String, Object>>) singleOrderMap.get("c_ordr");
		Log.d(TAG, "onCreate orderItemMapArray " +  orderItemMapArray);
		
		adapter = new LVAdapter(this.getApplicationContext(), R.layout.orders_elv_parent, orderItemMapArray);                

		Log.d(TAG, "onCreate child " +  3);

		// Binds the Adapter to the ListView         
		list.setAdapter(adapter);  
		
		Log.d(TAG, "onCreate 3");
		
		View empty = findViewById(R.id.empty_view);
		list.setEmptyView(empty);
		
		// Capture clicks on ListView items         
		
		list.setOnItemClickListener(new OnItemClickListener() {               
			@Override            
			public void onItemClick(AdapterView<?> parent, View view,        
					int position, long id) {          
				// Send single item click data to SingleItemView Class      
//				Intent i = new Intent(getActivity(), CResDetails.class);      
				// Pass all data restaurant          
//				i.putExtra("restaurant", restaurant);      
				// Pass all data description          
//				i.putExtra("position", position);     
				// Open SingleItemView.java Activity      
//				startActivity(i);       
				}
		});
	}   
    private Database getDatabase() {
		Application application = (Application) this.getApplication();
        return application.getDatabase();		
	}
	
    
    public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    
}