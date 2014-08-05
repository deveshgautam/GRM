
package com.example.grm;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.replicator.Replication;
import com.couchbase.lite.util.Log;

import org.apache.http.client.HttpResponseException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Observable;

public class Application extends android.app.Application {

    public static final String TAG = "GenieRM - Application";
    private static final String DATABASE_NAME = "geniedb";
    private static final String SYNC_URL = "http://sync.couchbasecloud.com:4984/todos4/";

    private Manager manager;
    private Database database;

    private OnSyncProgressChangeObservable onSyncProgressChangeObservable;
    private OnSyncUnauthorizedObservable onSyncUnauthorizedObservable;

    private void initDatabase() {
    	
   		Log.d(TAG, "initDatabase");

        try {
            Manager.enableLogging(Log.TAG, Log.VERBOSE);
            Manager.enableLogging(Log.TAG_SYNC, Log.DEBUG);
            Manager.enableLogging(Log.TAG_QUERY, Log.DEBUG);
            Manager.enableLogging(Log.TAG_VIEW, Log.DEBUG);
            Manager.enableLogging(Log.TAG_DATABASE, Log.DEBUG);

            manager = new Manager(new AndroidContext(getApplicationContext()), Manager.DEFAULT_OPTIONS);
        } catch (IOException e) {
            Log.e(TAG, "Cannot create Manager object", e);
            return;
        }

        try {
            database = manager.getDatabase(DATABASE_NAME);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Cannot get Database", e);
            return;
        }
    }

    private void initObservable() {
        
    	Log.d(TAG, "initObservable");

    	onSyncProgressChangeObservable = new OnSyncProgressChangeObservable();
        onSyncUnauthorizedObservable = new OnSyncUnauthorizedObservable();
    }

    private synchronized void updateSyncProgress(int completedCount, int totalCount) {
    	Log.d(TAG, "updateSyncProgress");

    	onSyncProgressChangeObservable.notifyChanges(completedCount, totalCount);
    }

    public void startReplicationSyncWithCustomCookie(String name, String value, String path, Date expirationDate, boolean secure, boolean httpOnly) {

    	Log.d(TAG, "startReplicationSyncWithCustomCookie");

        Replication[] replications = createReplications();
        Replication pullRep = replications[0];
        Replication pushRep = replications[1];

        pullRep.setCookie(name, value, path, expirationDate, secure, httpOnly);
        pushRep.setCookie(name, value, path, expirationDate, secure, httpOnly);

        pullRep.start();
        pushRep.start();

        Log.v(TAG, "Start Replication Sync ...");

    }

    public void startReplicationSyncWithStoredCustomCookie() {

    	Log.d(TAG, "startReplicationSyncWithStoredCustomCookie");

        Replication[] replications = createReplications();
        Replication pullRep = replications[0];
        Replication pushRep = replications[1];

        pullRep.start();
        pushRep.start();

        Log.v(TAG, "Start Replication Sync ...");

    }

    public void startReplicationSyncWithFacebookLogin() {

    	Log.d(TAG, "startReplicationSyncWithFacebookLogin");

    	Replication[] replications = createReplications();
        Replication pullRep = replications[0];
        Replication pushRep = replications[1];

        pullRep.start();
        pushRep.start();

        Log.v(TAG, "Start Replication Sync ...");
    }

    public Replication[] createReplications() {

    	Log.d(TAG, "createReplications");

    	URL syncUrl;
        try {
            syncUrl = new URL(SYNC_URL);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Invalid Sync Url", e);
            throw new RuntimeException(e);
        }

        Replication pullRep = database.createPullReplication(syncUrl);
        pullRep.setContinuous(true);
        pullRep.addChangeListener(getReplicationChangeListener());

        Replication pushRep = database.createPushReplication(syncUrl);
        pushRep.setContinuous(true);
        pushRep.addChangeListener(getReplicationChangeListener());

        return new Replication[]{pullRep, pushRep};

    }

    private Replication.ChangeListener getReplicationChangeListener() {
    	Log.d(TAG, "getReplicationChangeListener");

    	return new Replication.ChangeListener() {
            @Override
            public void changed(Replication.ChangeEvent event) {
                Replication replication = event.getSource();
                if (replication.getLastError() != null) {
                    Throwable lastError = replication.getLastError();
                    if (lastError instanceof HttpResponseException) {
                        HttpResponseException responseException = (HttpResponseException) lastError;
                        if (responseException.getStatusCode() == 401) {
                            onSyncUnauthorizedObservable.notifyChanges();
                        }
                    }
                }
                updateSyncProgress(replication.getCompletedChangesCount(),
                        replication.getChangesCount());
            }
        };
    }

    @Override
    public void onCreate() {
    	Log.d(TAG, "onCreate");

    	super.onCreate();

        initDatabase();
        initObservable();
    }

    public Database getDatabase() {
    	Log.d(TAG, "getDatabase");

    	return this.database;
    }

    public OnSyncProgressChangeObservable getOnSyncProgressChangeObservable() {
    	Log.d(TAG, "getOnSyncProgressChangeObservable");

    	return onSyncProgressChangeObservable;
    }

    public OnSyncUnauthorizedObservable getOnSyncUnauthorizedObservable() {
        return onSyncUnauthorizedObservable;
    }

    
    static class OnSyncProgressChangeObservable extends Observable {
        private void notifyChanges(int completedCount, int totalCount) {
        	Log.d(TAG, "notifyChanges");

        	SyncProgress progress = new SyncProgress();
            progress.completedCount = completedCount;
            progress.totalCount = totalCount;
            setChanged();
            notifyObservers(progress);
        }
    }

    static class OnSyncUnauthorizedObservable extends Observable {
        private void notifyChanges() {

        	Log.d(TAG, "notifyChanges2");
        	setChanged();
            notifyObservers();
        }
    }

    static class SyncProgress {
        public int completedCount;
        public int totalCount;
    }


}
