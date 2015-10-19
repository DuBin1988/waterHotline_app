package com.aofeng.hotline.db;

import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static DBAdapter instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    public static synchronized void initializeInstance(Context context, String dbName) {
    	//reset the instance
    	if(instance != null)
    	{
    		mDatabaseHelper.close();
    		instance = null;
    	}
        if (instance == null) {
            instance = new DBAdapter();
            mDatabaseHelper = new DatabaseHelper(context, dbName);
        }
    }

    public static synchronized DBAdapter getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DBAdapter.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();

        }
    }
}

 class DatabaseHelper extends SQLiteOpenHelper {
    DatabaseHelper(Context context, String dbName) {
      super(context, dbName, null, 1);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {	
	}
}
