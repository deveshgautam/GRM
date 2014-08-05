package com.example.grm.order;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.example.grm.cblite.CBLiteOrder;

import android.util.Log;

public class OrderData {

	public static final String TAG = "GenieRM - OrderData";
	
	public ArrayList<String> parentArray 	= new ArrayList<String>();
	public ArrayList<ArrayList<Map<String, Object>>> childArrayOfArray 
											= new ArrayList<ArrayList<Map<String, Object>>>();
	
	public OrderData(Database database) throws IOException, CouchbaseLiteException {

   		Log.d(TAG, "OrderData");
   		ArrayList<Map<String, Object>> retrievedOrderArray = new ArrayList<Map<String, Object>>();
    	   		
		try {
			CBLiteOrder.addTestData(database, createDocContent());
	    	retrievedOrderArray = CBLiteOrder.getTestData(database);
	     }
	      finally {
	     }
		reformatData(retrievedOrderArray);
		this.setParentArray(parentArray);
		this.setChildArrayOfArray(childArrayOfArray);

	}
		
    public Map<String, Object> createDocContent(){
    
    	Log.d(TAG, "createDocContent");

    	Map<String, Object> docContent = new HashMap<String, Object>();
        
	 	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	    Calendar calendar = GregorianCalendar.getInstance();
	    String currentTimeString = dateFormatter.format(calendar.getTime());

	    UUID uuid = UUID.randomUUID();
	    long currentTime = calendar.getTimeInMillis();
	    String id = currentTime + "-" + uuid.toString();

	    docContent.put("_id", id);
	    docContent.put("type", "order");
	    docContent.put("ts", currentTimeString);
		docContent.put("res_id", "R123456" );
		docContent.put("stts", "New");
		docContent.put("stts_ts",  currentTimeString);
		docContent.put("c_id", "C1234567890");
		docContent.put("c_addr_ln", "B114, Central Market");
		docContent.put("c_area", "Sector 50");
		docContent.put("c_city", "Noida");
		docContent.put("c_state", "Uttar Pradesh");
		docContent.put("c_nm", "Dev G");
		docContent.put("c_ph", "+91 9876543210");
		docContent.put("item_cnt", 5);
		
		double ttlValue = 550.0 ;
		
		docContent.put("ttl_value", ttlValue);
		 
		ArrayList<Double> custGeo = new ArrayList<Double>();
		custGeo.add(28.57051);
		custGeo.add(77.36331);
		
		docContent.put("c_geo", custGeo);
		
		ArrayList<Map<String, Object>> orderItemArray = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = new HashMap<String, Object>();
		
		item = getOrderItemMap("Kadai Paneer", 180.00, 1);
		orderItemArray.add(item);
		
		item = getOrderItemMap("Kadai Chicken", 240.00, 1);
		orderItemArray.add(item);
		
		item = getOrderItemMap("Tandoori Roti", 15.00, 10);
		orderItemArray.add(item);
		
		item = getOrderItemMap("Pineapple Raita", 110.00, 2);
		orderItemArray.add(item);
		
		item = getOrderItemMap("Veg Pulao", 80.00, 1);
		orderItemArray.add(item);
		
		docContent.put("c_ordr", orderItemArray);
		
		return docContent ;
	    }
    public Map<String, Object> getOrderItemMap(String itemName, double itemRate, double itemQty){
		
	 	Log.d(TAG, "getOrderItemMap");

    	Map<String, Object> orderItemMap = new HashMap<String, Object>() ;
    	
    	orderItemMap.put("item_nm", itemName);
    	orderItemMap.put("item_rt", itemRate);
    	orderItemMap.put("item_qty", itemQty);
    	orderItemMap.put("item_value", itemRate * itemQty);
    	
    	return orderItemMap ;
	}
	public void reformatData(ArrayList<Map<String, Object>> retrievedOrderArray){
    	
	 	Log.d(TAG, "reformatData");
    	
    	ArrayList<Map<String, Object>> newOrderArray = new ArrayList<Map<String, Object>>();
    	ArrayList<Map<String, Object>> acceptedOrderArray = new ArrayList<Map<String, Object>>();
    	ArrayList<Map<String, Object>> readyOrderArray = new ArrayList<Map<String, Object>>();
    	ArrayList<Map<String, Object>> onTheWayOrderArray = new ArrayList<Map<String, Object>>();
    	ArrayList<Map<String, Object>> deliveredOrderArray = new ArrayList<Map<String, Object>>();
    	ArrayList<Map<String, Object>> declinedOrderArray = new ArrayList<Map<String, Object>>();
    	ArrayList<Map<String, Object>> cancelledOrderArray = new ArrayList<Map<String, Object>>();
    	
    	Map<String, Object> childMap = new HashMap<String, Object>();
	     	
    	for (int i=0; i < retrievedOrderArray.size(); i++ ){
    		String status = retrievedOrderArray.get(i).get("stts").toString();
    		childMap = retrievedOrderArray.get(i);
    		switch (status) {
	    	case "New":
	    		newOrderArray.add(childMap);
	    		break;
	    	case "Accepted":
	    		acceptedOrderArray.add(childMap);
	    		break;
	    	case "Ready":
	    		readyOrderArray.add(childMap);
	    		break;
	    	case "On The Way":
	    		onTheWayOrderArray.add(childMap);
	    		break;
	    	case "Delivered":
	    		deliveredOrderArray.add(childMap);
	    		break;
	    	case "Declined":
	    		declinedOrderArray.add(childMap);
	    		break;
	    	case "Cancelled":
	    		cancelledOrderArray.add(childMap);
	    		break;
	    	default:
	    		break;
	        }	
    	}
    	
    	if (newOrderArray.size()> 0){
			parentArray.add("New");
			childArrayOfArray.add(newOrderArray);
		}
		if (acceptedOrderArray.size()> 0){
			parentArray.add("Accepted");
			childArrayOfArray.add(acceptedOrderArray);
		}
		if (readyOrderArray.size()> 0){
			parentArray.add("Ready");
			childArrayOfArray.add(readyOrderArray);
		}
		if (onTheWayOrderArray.size()> 0){
			parentArray.add("On the Way");
			childArrayOfArray.add(onTheWayOrderArray);
		}
		if (deliveredOrderArray.size()> 0){
			parentArray.add("Delivered");
			childArrayOfArray.add(deliveredOrderArray);
		}
		if (declinedOrderArray.size()> 0){
			parentArray.add("Declined");
		    childArrayOfArray.add(declinedOrderArray);
		}
		if (cancelledOrderArray.size()> 0){
			parentArray.add("Cancelled");
		    childArrayOfArray.add(cancelledOrderArray);
		}
    }
    public void setParentArray(ArrayList<String> parentArray) {
		Log.d(TAG, "setParentArray");
		this.parentArray = parentArray;
	}

	public void setChildArrayOfArray(ArrayList<ArrayList<Map<String, Object>>> childArrayOfArray) {
		Log.d(TAG, "setChildArrayOfArray");

		this.childArrayOfArray = childArrayOfArray;
	}
}