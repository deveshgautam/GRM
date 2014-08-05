package com.example.grm.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.grm.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
	 
public class LVAdapter extends ArrayAdapter<Map<String, Object>>{
	ArrayList<Map<String, Object>> orderItemMapArray ;
	LayoutInflater inflater ;
	Context context;
	
    public static final String TAG = "GRM - LVAdapter";

	public LVAdapter(final Context context, int resource, ArrayList<Map<String, Object>> orderItemMapArray) {
		super(context, resource, orderItemMapArray);
		this.orderItemMapArray = orderItemMapArray;
		this.context = context;
		Log.d(TAG, "LVAdapter");
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
    	Log.d(TAG, "getCount " + orderItemMapArray.size());
		return orderItemMapArray.size();
	}
	@Override
	public Map<String, Object> getItem(int position) {
		// TODO Auto-generated method stub
    	Log.d(TAG, "getItem");
		return null;
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
    	Log.d(TAG, "getItemId");
		return position;
	}
	@Override
 	public View getView(final int position, View convertView, final ViewGroup parent) {      
    	Log.d(TAG, "getView");
		
    	ViewHolder viewHolder;
    	
		if(convertView == null) {
			final LayoutInflater inflater = (LayoutInflater)context.getSystemService
				      (Context.LAYOUT_INFLATER_SERVICE);
               convertView = inflater.inflate(R.layout.lv_order_items, null);
               viewHolder = new ViewHolder();
               convertView.setTag(viewHolder);
               viewHolder.txtItemName = (TextView) convertView.findViewById(R.id.txtItemName);
               viewHolder.txtItemRate = (TextView) convertView.findViewById(R.id.txtItemRate);
               viewHolder.txtItemQty = (TextView) convertView.findViewById(R.id.txtItemQty);
               viewHolder.txtItemValue = (TextView) convertView.findViewById(R.id.txtItemValue);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Map<String, Object> orderItemMap = new HashMap<String, Object>();
		
           orderItemMap = (Map<String, Object>) getItem(position);
   		// Capture position and set to the TextViews       
   		viewHolder.txtItemName.setText(String.format("%.2f %%", orderItemMap.get("item_nm").toString()));      
   		viewHolder.txtItemRate.setText(String.format("%.2f %%", orderItemMap.get("item_rt").toString()));      
   		viewHolder.txtItemQty.setText(String.format("%.2f %%", orderItemMap.get("item_qty").toString()));      
   		viewHolder.txtItemValue.setText(String.format("%.2f %%", orderItemMap.get("item_value").toString()));      
       
   		return convertView ;
		}
	private static class ViewHolder {
	 	public TextView txtItemName, txtItemRate, txtItemQty, txtItemValue;    
	}
}