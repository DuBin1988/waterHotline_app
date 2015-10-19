package com.aofeng.hotline.service;

import java.net.URLEncoder;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aofeng.hotline.R;
import com.aofeng.hotline.db.DBAdapter;
import com.aofeng.utils.Util;
import com.aofeng.utils.Vault;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class AlarmService extends AbstractService {
    public static final int MSG_FROM_ACTIVITY_MUTE = 0;
    public static final int MSG_TO_ACTIVITY_ALARM = 1;
    public static final int MSG_TO_ACTIVITY_NETWORK_ERROR = 2;
	Player player;
	boolean shouldStop;
	Thread th;
	boolean all=true;

	@Override
	public void onStartService() {
		player = new Player();
		DBAdapter.initializeInstance(this, Util.getDBName2(this));
		th = new Thread(new Runnable() {	
			@Override
			public void run() {
				onTimerTick();
			}
		});
		th.start();
	}

	@Override
	public void onStopService() {
	       if(player != null)
	       {
	    	   player.stop();
	    	   player = null;
	       }
	       shouldStop = true;
	       try {
	    	th.interrupt();  //dont sleep
			th.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	        Log.i("AlarmService", "Service Stopped.");
	}

	@Override
	public void onReceiveMessage(Message msg) {
		if(msg.what == AlarmService.MSG_FROM_ACTIVITY_MUTE)
			player.stop();
 	}
	
    private void onTimerTick() {
    	for(;;)
    	{
	        try {
	        		if(shouldStop)
	        		{
	        			if(player != null)
	        				player.stop();
	        			return;
	        		}
	        		try
	        		{
	        			Thread.sleep(Vault.INTERVAL);
	        		}
	        		catch(InterruptedException e)
	        		{
	           			if(player != null)
	        				player.stop();
	        			return;
	        		}
	    	        Log.i("AlarmService", "Check new slips...");
		        	//if(HasNew() && !shouldStop)
		        	if(!shouldStop)
		        	{
		        		if(HasNew()){
			        		player.play(this, R.raw.ding);
				        	Message msg = Message.obtain(null, AlarmService.MSG_TO_ACTIVITY_ALARM, 0, 0);
				        	this.send(msg);
		        		}
		        		//if(Hasrevgongdan()) //检测是否有撤销
			        		//;
		        	}
		        	else
		        	{
		        		if(shouldStop)
		        		{
		           			if(player != null)
		        				player.stop();
		        			return;
		        		}
			        	Message msg = Message.obtain(null, AlarmService.MSG_TO_ACTIVITY_ALARM, -1, -1);
			        	this.send(msg);
		        	}
	        	}
	        catch (Exception t) { 
	            Log.e("TimerTick", "Timer Tick Failed.", t);            
	        }
        } 
    }

    /**
     * 提取保修，抢修数据，如果有新数据则响铃
     * @return
     */
	private boolean HasNew() {
		try {
			boolean result = false;
			String url="";
			if(all){
				url = Vault.PHONE_URL + "download/"+Util.getSharedPreference(this, Vault.CHECKER_NAME)+"/Yes";
				all=false;
			}else{
				url = Vault.PHONE_URL + "download/"+Util.getSharedPreference(this, Vault.CHECKER_NAME)+"/No";
			}
			HttpGet getMethod = new HttpGet(url);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(getMethod);
			int code = response.getStatusLine().getStatusCode();
			if (code != 200)
				return result;
			Log.i("AlarmService", url);
			String jsonString = EntityUtils.toString(response.getEntity(),"UTF8");
			JSONArray rows = new JSONArray(jsonString);
			if(rows.length()>0)
				result |= IfNewFixArrived(rows);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
        	Message msg = Message.obtain(null, AlarmService.MSG_TO_ACTIVITY_NETWORK_ERROR, -1, -1);
        	this.send(msg);			
			return false;
		}finally{
			Hasrevgongdan();  //检测是否有撤销
			//Hasrevgongdan();  //检测 抢单
		}
		
		
	}

	
	/**
	 * 是否有新的记录
	 * @param rows
	 * @return
	 */
	private boolean IfNewFixArrived(JSONArray rows) {
		boolean result = false;
		SQLiteDatabase db = DBAdapter.getInstance().openDatabase();//hotline_1_1.db
		try
		{
			for(int i=0; i<rows.length(); i++)
			{
				JSONObject row = rows.getJSONObject(i);
				String sql = "select ID from T_BX_REPAIR_ALL where f_cucode=?";
				Cursor c = db.rawQuery(sql, new String[]{row.getString("f_cucode")});
				if(!c.moveToNext())
				{
					result = true;
					SaveRow(db, row, "T_BX_REPAIR_ALL");
				}
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(db != null)
				DBAdapter.getInstance().closeDatabase();
		}		
		return result;
	}

	/**
	 * 保存保修记录
	 * @param row
	 * @throws JSONException 
	 */
	private void SaveRow(SQLiteDatabase db, JSONObject row, String TableName) throws JSONException {
		String sql = "INSERT INTO " + TableName + " (";
		String cols = "MUTE";
		String values = "'N'";
		Cursor c = db.query(TableName, null, null, null, null, null, null);
		String[] names = c.getColumnNames();
		Iterator<String> itr = row.keys();
		while(itr.hasNext())
		{
			String col = itr.next();
			Object obj = row.get(col);
			if(keyInColumns(names, col))
			{
				cols += "," + col;
				if(obj != null)
				{
						values += ",'" + obj.toString().replace("'", "") + "'";
				}
				else
					values += ",null";
				}
		}
		sql = sql + cols + ") VALUES (" + values + ")";
		db.execSQL(sql);
	}
	
    /**
     * 检测撤销的工单
     * @return
     */
	private boolean Hasrevgongdan() {
		try {
			boolean result = false;
			String url=Vault.PHONE_URL + "revgongdan/"+Util.getSharedPreference(this, Vault.CHECKER_NAME)+"/No";
			HttpGet getMethod = new HttpGet(url);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(getMethod);
			int code = response.getStatusLine().getStatusCode();
			if (code != 200)
				return result;
			Log.i("AlarmService", url);
			String jsonString = EntityUtils.toString(response.getEntity(),"UTF8");
			JSONArray rows = new JSONArray(jsonString);
			if(rows.length()>0)
				result |= IfNewFixArrived1(rows);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
        	Message msg = Message.obtain(null, AlarmService.MSG_TO_ACTIVITY_NETWORK_ERROR, -1, -1);
        	this.send(msg);			
			return false;
		}
		
	}

	
	/**
	 * 是否有新的记录
	 * @param rows
	 * @return
	 */
	private boolean IfNewFixArrived1(JSONArray rows) {
		boolean result = false;
		SQLiteDatabase db = DBAdapter.getInstance().openDatabase();//hotline_1_1.db
		try
		{
			for(int i=0; i<rows.length(); i++)
			{
				JSONObject row = rows.getJSONObject(i);
				String sql = "select ID from T_BX_REPAIR_ALL where f_downloadstatus='正常' and f_cucode=?";
				Cursor c = db.rawQuery(sql, new String[]{row.getString("f_cucode")});
				if(c.moveToNext())
				{
					if("工单已撤销".equals(row.get("f_downloadstatus").toString().replace("'", ""))){
						sql = "update T_BX_REPAIR_ALL set f_downloadstatus='" + row.get("f_downloadstatus").toString().replace("'", "")+"' where f_cucode='"+row.getString("f_cucode")+"'";
						db.execSQL(sql);
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(db != null)
				DBAdapter.getInstance().closeDatabase();
		}
		return result;
	}
	
	private boolean keyInColumns(String[] names, String col) {
		for(String name :names)
			if(name.equals(col))
				return true;
		return false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}    
	
	
}
