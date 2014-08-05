package com.example.grm.order;


import java.util.ArrayList; 
import java.util.HashMap;
import java.util.Map;

import com.couchbase.lite.util.Log;
import com.example.grm.R;

import android.app.Activity; 
import android.content.Context;
import android.view.LayoutInflater; 
import android.view.View; 
import android.view.ViewGroup; 
import android.widget.BaseExpandableListAdapter; 
import android.widget.CheckedTextView; 
import android.widget.TextView;


public class ELVAdapter extends BaseExpandableListAdapter { 
	Context context ; 
	
    public static final String TAG = "GenieRM - ELVAdapter";

    private LayoutInflater inflater; 
    
    public ArrayList<String> parentArray 	= new ArrayList<String>();
	public ArrayList<ArrayList<Map<String, Object>>> childArrayOfArray 
											= new ArrayList<ArrayList<Map<String, Object>>>();
	
    public ELVAdapter(Context context, ArrayList<String> parentArray,
    		ArrayList<ArrayList<Map<String, Object>>> childArrayOfArray) { 
        this.context = context;
   	 	this.parentArray = parentArray; 
        this.childArrayOfArray = childArrayOfArray; 
    } 
   
     
     public void setInflater(LayoutInflater inflater, Activity activity) { 

    	 Log.d(TAG, "setInflater");

    	 this.inflater = inflater; 
     } 
   
    @Override 
     public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) { 
    	
     	Log.d(TAG, "getChildView");
     	
       	Map<String, Object> singleOrderMap = new HashMap<String, Object>(); 
       	
       	singleOrderMap = childArrayOfArray.get(groupPosition).get(childPosition); 
    	
   //    	String orderId = (String) singleOrderMap.get("_id");
    //   	String orderStatus = (String) singleOrderMap.get("stts");
	//    String orderTimestamp = (String) singleOrderMap.get("ts");
	//    String custAddrLnOne = (String) singleOrderMap.get("c_addr_ln");
	    String custArea = (String) singleOrderMap.get("c_area");
//	    String custCity = (String) singleOrderMap.get("c_city");
//	    String custState = (String) singleOrderMap.get("c_state");
	    String custName = (String) singleOrderMap.get("c_nm");
	    String custPhone = (String) singleOrderMap.get("c_ph");
//	    int item_cnt = (int) singleOrderMap.get("item_cnt");
//	    double ttlValue = (double) singleOrderMap.get("item_cnt");
	    		
//		double ttlValue = 550.0 ;
       	
    	TextView textView = null; 

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     		
        convertView = inflater.inflate(R.layout.orders_elv_child, null); 
             
         textView = (TextView) convertView.findViewById(R.id.textCustName); 
         textView.setText(custName); 
         
         textView = (TextView) convertView.findViewById(R.id.textCustAddr); 
         textView.setText(custArea); 
         
         textView = (TextView) convertView.findViewById(R.id.textCustPhone); 
          textView.setText(custPhone); 
         
         convertView.setClickable(false);
         return convertView; 
     } 

     @Override 
     public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) { 
         	inflater = (LayoutInflater) context          
    				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

         	Log.d(TAG, "getGroupView");
    
         	ArrayList<Map<String, Object>> child = 
         			(ArrayList<Map<String, Object>>) childArrayOfArray.get(groupPosition); 
         
           	int grpCnt = child.size();

         	Log.d(TAG, "grpCnt :" + grpCnt);
     		
            convertView = inflater.inflate(R.layout.orders_elv_parent, null); 
          
             String grpNameText = parentArray.get(groupPosition).toString() + 
            		 "  (" + grpCnt + ")";
         ((CheckedTextView) convertView).setText(grpNameText); 
         ((CheckedTextView) convertView).setChecked(isExpanded); 
         return convertView; 
     } 

     @Override 
     public Object getChild(int groupPosition, int childPosition) { 
         return null; 
     } 
     @Override 
     public long getChildId(int groupPosition, int childPosition) { 
         return 0; 
     } 
     @Override 
     public int getChildrenCount(int groupPosition) { 
         return ((ArrayList<Map<String, Object>>) childArrayOfArray.get(groupPosition)).size(); 
     } 
     @Override 
     public Object getGroup(int groupPosition) { 
         return null; 
     } 
     @Override 
     public int getGroupCount() { 
         return parentArray.size(); 
     } 
     @Override 
     public void onGroupCollapsed(int groupPosition) { 
         super.onGroupCollapsed(groupPosition); 
     } 
     @Override 
     public void onGroupExpanded(int groupPosition) { 
         super.onGroupExpanded(groupPosition); 
     } 
     @Override 
     public long getGroupId(int groupPosition) { 
         return 0; 
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