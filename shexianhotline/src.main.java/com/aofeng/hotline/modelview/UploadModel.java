package com.aofeng.hotline.modelview;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;

import com.aofeng.hotline.activity.MainActivity;
import com.aofeng.hotline.model.RepairSlipRowModel;
import com.aofeng.hotline.model.UploadRowModel;
import com.aofeng.utils.Util;
import com.aofeng.utils.Vault;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class UploadModel {
	public Activity mContext;
	public boolean cancelable;
	
	public Activity getActivity() {
		return mContext;
	}

	public UploadModel(Activity context) {
		this.mContext = context;
	}
	String batchJSON="[";
	// 维修列表
	public ArrayListObservable<UploadRowModel>NotUpload = new ArrayListObservable<UploadRowModel>(
			UploadRowModel.class);
	public void showNotUpload(){
		NotUpload.clear();
		SQLiteDatabase db = null;
		try
		{	
			db = mContext.openOrCreateDatabase(Util.getDBName(mContext), Context.MODE_PRIVATE, null);
			String sql = "select * from T_BX_REPAIR_ALL where completion in ('已完成','不具备通气')";
			Cursor c = db.rawQuery(sql, null);
			while(c.moveToNext())
			{
				UploadRowModel model = new UploadRowModel(this);
				model.USERNAME.set(c.getString(c.getColumnIndex("f_username")));//用户姓名
				model.PHONE.set(c.getString(c.getColumnIndex("f_phone")));//来电号码
				String s=c.getString(c.getColumnIndex("completion"));
				model.SENDER.set(c.getString(c.getColumnIndex("f_sender")));//派单人
				model.SENDTIME.set(c.getString(c.getColumnIndex("f_senddate"))+" "+c.getString(c.getColumnIndex("f_sendtime")));//派单时间
				model.CUCODE.set(c.getString(c.getColumnIndex("f_cucode")));//报修编号
				
				NotUpload.add(model);
				if(!"[".equals(batchJSON)){
					batchJSON+=",";
				}//{cucode}/{smwxjl}/{finishtime}
				/* batchJSON+="{cucode:"+c.getString(c.getColumnIndex("f_cucode"))+
						",smwxjl:'"+c.getString(c.getColumnIndex("f_smwxjl"))+"'"+
						",finishtime:"+c.getString(c.getColumnIndex("finishtime"))+
						",f_gaswatchbrand:"+c.getString(c.getColumnIndex("f_gaswatchbrand"))+
						",f_aroundmeter:"+c.getString(c.getColumnIndex("f_aroundmeter"))+
						",f_lastrecord:"+c.getString(c.getColumnIndex("f_lastrecord"))+
						",surplus:"+c.getString(c.getColumnIndex("surplus"))+
						",completion:"+c.getString(c.getColumnIndex("completion"))+"}"; */
				batchJSON+="{cucode:"+c.getString(c.getColumnIndex("f_cucode")).trim()+
						",smwxjl:'"+c.getString(c.getColumnIndex("inshi")).trim()+":"+c.getString(c.getColumnIndex("f_smwxjl")).trim().replaceAll("\n", "。")+"'"+
						",finishtime:"+c.getString(c.getColumnIndex("finishtime")).trim()+
						",f_gaswatchbrand:"+c.getString(c.getColumnIndex("f_gaswatchbrand")).trim()+
						",f_aroundmeter:"+c.getString(c.getColumnIndex("f_aroundmeter")).trim()+
						",f_lastrecord:"+c.getString(c.getColumnIndex("f_lastrecord")).trim()+
						",surplus:"+c.getString(c.getColumnIndex("surplus")).trim()+
						//",servercheck:'"+c.getString(c.getColumnIndex("servercheck")).trim()+"'"+
						//",shul:'"+c.getString(c.getColumnIndex("shul")).trim()+"'"+
						",completion:"+c.getString(c.getColumnIndex("completion")).trim()+"}";
				
			}
			batchJSON+="]";
		}catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			if(db != null)
				db.close();
		}
	}
	public Command patchUpload = new Command()
	{
		@Override//resultState
		public void Invoke(android.view.View arg0, Object... arg1) {
			if(batchJSON.equals("[]")){
				Toast.makeText(mContext, "没有需要上传的记录", Toast.LENGTH_SHORT).show();
				return;
			}
			Thread th = new Thread(new Runnable() {
				@Override
				public void run() {
					Message msg = new Message();
					try {
						String url = Vault.PHONE_URL + "batchUpload";

						// 创建POSTMethod    record/batch/{sgoperator}/{sgnetwork}
						HttpPost postMethod =new HttpPost(url);/*建立HTTP Post连线*/
						StringEntity se = new StringEntity(batchJSON,"UTF-8");
						postMethod.setEntity(se);
						
						// 执行POSTMethod
						HttpClient httpClient = new DefaultHttpClient();
						HttpResponse response = httpClient.execute(postMethod);
						int code = response.getStatusLine().getStatusCode();
						if (code == 200) {// 若状态码为200
							msg.what = 1;
							msg.obj = EntityUtils.toString(response.getEntity(), "UTF8");
						} else {
							msg.what = 0;
						}
					}  catch (IOException e) {
						e.printStackTrace();
						msg.what = 0;
					}finally{
						mpatchUpload.sendMessage(msg);
					}
				}
			});
			th.start();
		}
	};
	private final Handler mpatchUpload = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				okAndDelete(msg.obj.toString());
			}else {
				Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	private void okAndDelete(String str){
		SQLiteDatabase db =null;
		SQLiteDatabase db2=null;
		try{
		db = mContext.openOrCreateDatabase(Util.getDBName(mContext), Context.MODE_PRIVATE, null);
		db2 = mContext.openOrCreateDatabase(Util.getDBName2(mContext), Context.MODE_PRIVATE, null);
		String arr[]=str.split(",");
		if(arr.length%2==1){
			Toast.makeText(mContext, "服务器返回错误", Toast.LENGTH_SHORT).show();
			return;
		}
			for(int i=0;i<arr.length;){
				if("ok".equals(arr[i+1])){
					db2.execSQL("delete from T_BX_REPAIR_ALL where f_cucode="+arr[i]);
					db.execSQL("delete from T_BX_REPAIR_ALL where f_cucode="+arr[i] + " and completion in ('已完成','不具备通气')");
					i+=2;
				}
			}
			Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			db.close();
			db2.close();
			showNotUpload();
		}
	}
}
