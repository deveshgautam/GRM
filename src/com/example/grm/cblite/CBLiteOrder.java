package com.example.grm.cblite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;

import android.annotation.SuppressLint;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class CBLiteOrder {
    
	private static final String VIEW_NAME = "orders";
    private static final String BY_ID_VIEW_NAME = "orders_by_id";

    private static final String DOC_TYPE = "order";
    public static final String TAG = "GenieRM - document/CBLiteOrder";
    
    public static Query getQuery(Database database) {
     
    	Log.d(TAG, "getQuery");

    	com.couchbase.lite.View view = database.getView(VIEW_NAME);
  
    	if (view.getMap() == null) {
    		Mapper map = new Mapper() {
                @Override
                public void map(Map<String, Object> document, Emitter emitter) {

                	if (DOC_TYPE.equals(document.get("type"))) {
                        java.util.List<Object> keys = new ArrayList<Object>();
                        keys.add(document.get("stts"));
                        emitter.emit(keys, document); 
        	         }
                }
            };
            view.setMap(map, "1.3");
        }

        Query query = view.createQuery();
        query.setDescending(false);

        java.util.List<Object> endKeys = new ArrayList<Object>();
        endKeys.add("New");
        endKeys.add(new HashMap<String, Object>());

        java.util.List<Object> startKeys = new ArrayList<Object>();
        startKeys.add("New");

        query.setStartKey(startKeys);
        query.setEndKey(endKeys);

        return query;
    }
    public static Query getQueryById(Database database, String orderId) {

    	Log.d(TAG, "getQueryById");

    	com.couchbase.lite.View view = database.getView(BY_ID_VIEW_NAME);
        if (view.getMap() == null) {
            Mapper map = new Mapper() {
                @Override
                public void map(Map<String, Object> document, Emitter emitter) {
                    if (DOC_TYPE.equals(document.get("type"))) {
                        emitter.emit(document.get("_id"), document);
                    }
                }
            };
            view.setMap(map, "1.1");
        }

        Query query = view.createQuery();
        java.util.List<Object> keys = new ArrayList<Object>();
        keys.add(orderId);
        query.setKeys(keys);

        return query;
    }

    public static Map<String, Object> getOrderById(Database database, String orderId) {

    	Log.d(TAG, "getOrderById");

    	Document document = null;
        try {
            QueryEnumerator enumerator = CBLiteOrder.getQueryById(database, orderId).run();
            document = enumerator != null && enumerator.getCount() > 0 ?
                    enumerator.getRow(0).getDocument() : null;
        } catch (CouchbaseLiteException e) { }

         Map<String, Object> singleOrderMap = new HashMap<String, Object>();
         singleOrderMap.putAll(document.getProperties());
         
        return singleOrderMap;
    }
    
    public static void updateOrderStatus(Document document, String newStatus)
            throws CouchbaseLiteException {
        
    	Log.d(TAG, "updateOrderStatus");
    	
    	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	    Calendar calendar = GregorianCalendar.getInstance();
	    String currentTimeString = dateFormatter.format(calendar.getTime());

    	Map<String, Object> properties = new HashMap<String, Object>();
        properties.putAll(document.getProperties());
        properties.put("stts", newStatus);
        properties.put("stts_ts",currentTimeString ) ;
        document.putProperties(properties);
    }
    
    public static void addTestData(Database database, Map<String, Object> docContent){

	 	// create an empty document
		Document document = database.createDocument();
		
		// write the document to the database
		try {
		 		document.putProperties(docContent);
		 	} catch (CouchbaseLiteException e) {
		 		e.printStackTrace();
		 		Log.e(TAG, "Cannot write document to database", e);
		 	}
	}
    public static ArrayList<Map<String, Object>> getTestData(Database database){

    	ArrayList<Map<String, Object>> retrievedOrderArray = new ArrayList<Map<String, Object>>();
    	
 		try {
 		    QueryEnumerator enumerator = getQuery(database).run();
 		   while (enumerator.hasNext()) {
 	            Document document = enumerator.next().getDocument();
 	            Map<String, Object> properties = new HashMap<String, Object>();
 	            properties.putAll(document.getProperties());
 	            retrievedOrderArray.add(properties);
 		   }
 		} catch (CouchbaseLiteException e) {
 			e.printStackTrace();
 		 	Log.e(TAG, "Cannot write document to database", e);
 		 	}
 		return retrievedOrderArray;
 	}
}
 