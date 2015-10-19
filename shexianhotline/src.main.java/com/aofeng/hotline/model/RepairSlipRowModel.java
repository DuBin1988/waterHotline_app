package com.aofeng.hotline.model;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gueei.binding.Command;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.StringObservable;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aofeng.hotline.R;
import com.aofeng.hotline.activity.MechanicsChooserActivity;
import com.aofeng.hotline.activity.SlipActivity;
import com.aofeng.hotline.modelview.LoginModel;
import com.aofeng.hotline.modelview.MainModel;
import com.aofeng.hotline.service.AlarmService;
import com.aofeng.utils.Util;
import com.aofeng.utils.Vault;

/**
 * 维修模型
 * @author lgy
 *
 */
public class RepairSlipRowModel {
	public MainModel model;
	
	public RepairSlipRowModel(MainModel model)
	{
		this.model = model;
	}
	
	public void setMute(String mute)
	{
		MUTE= mute;
		int flag = Integer.parseInt(mute);
		MUTED.set((flag & Vault.ITEM_ALARM)>0);//报警
		REPAIRED.set((flag & Vault.ITEM_REPAIRED)>0);//已维修
		INSPECTED.set((flag & Vault.ITEM_INSPECTED)>0);//已检
		UPLOADED.set((flag & Vault.ITEM_RETURNED)>0);//已返单
	}
	//自增ID
	public String ID;
	//是否已静音
	private String MUTE;
	public BooleanObservable MUTED = new BooleanObservable(false);//报警
	public BooleanObservable REPAIRED = new BooleanObservable(false);//已维修
	public BooleanObservable INSPECTED = new BooleanObservable(false); 
	public BooleanObservable UPLOADED = new BooleanObservable(false); 
	
	public String USERID;//用户ID
	public StringObservable USERNAME = new StringObservable("");//用户姓名 
	public StringObservable USERADDRESS = new StringObservable("");//用户地址 
	public StringObservable GASUSERTYPE = new StringObservable("");//用户类别
	public StringObservable LINKTYPE = new StringObservable("");//用户电话
	
	public StringObservable SENDER = new StringObservable("");//派单人
	public StringObservable CUCODE = new StringObservable("");//报修编号
	public StringObservable REPAIRTYPE = new StringObservable("");//报修类型
	public StringObservable PHONE = new StringObservable("");//来电号码
	public StringObservable SENDTIME = new StringObservable("");//派单时间
	public StringObservable REPAIRREASON = new StringObservable("");//来电内容
	public StringObservable STOPREMARK = new StringObservable("");//备注
	
	public StringObservable METERNUMBER = new StringObservable("");//表号
	public StringObservable METERTYPE = new StringObservable("");//气表型号
	public StringObservable LEFTRIGHTWATCH = new StringObservable("");//左右表
	public String lastrecord = "";//表读数
	public StringObservable metergasnums = new StringObservable("");//累计购气量
	public StringObservable gasmeteraccomodations = new StringObservable("");//表底数
	public StringObservable f_downloadstatus = new StringObservable("");//表底数
	public StringObservable f_workingdays = new StringObservable("");//表底数
	
	public StringObservable smwxjl = new StringObservable("");//维修记录
	
	public String gaswatchbrand = "";//气表品牌
	public String surplus = "";//补气量
	public String completion = "";//完成状态
	
	public Command DetailCmd = new Command() {
		@Override
		public void Invoke(View arg0, Object... arg1) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("ID", ID);
			bundle.putString("USERID", USERID);
			bundle.putString("USERNAME", USERNAME.get());
			bundle.putString("USERADDRESS", USERADDRESS.get());
			bundle.putString("GASUSERTYPE", GASUSERTYPE.get());
			bundle.putString("LINKTYPE", LINKTYPE.get());
			
			bundle.putString("SENDER", SENDER.get());
			bundle.putString("CUCODE", CUCODE.get());
			bundle.putString("REPAIRTYPE", REPAIRTYPE.get());
			bundle.putString("PHONE", PHONE.get());
			bundle.putString("SENDTIME", SENDTIME.get());
			bundle.putString("REPAIRREASON", REPAIRREASON.get());
			bundle.putString("STOPREMARK", STOPREMARK.get());
			
			bundle.putString("METERNUMBER", METERNUMBER.get());
			bundle.putString("METERTYPE", METERTYPE.get());
			bundle.putString("LEFTRIGHTWATCH", LEFTRIGHTWATCH.get());
			bundle.putString("lastrecord", lastrecord);
			bundle.putString("metergasnums", metergasnums.get());
			bundle.putString("gasmeteraccomodations", gasmeteraccomodations.get());
			
			bundle.putString("smwxjl", smwxjl.get());
			
			bundle.putString("gaswatchbrand", gaswatchbrand);
			bundle.putString("surplus", surplus);
			bundle.putString("completion", completion);
			bundle.putString("f_downloadstatus", f_downloadstatus.get());
			bundle.putString("f_workingdays", f_workingdays.get());
			
			setMute();//单条静音
			intent.setClass(model.mContext, SlipActivity.class);
			intent.putExtras(bundle);
			model.mContext.startActivity(intent);
			
			Thread th = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Message msg = new Message();
						try {
							// HttpGet getMethod = new HttpGet(Vault.PHONE_URL+"upload/"+CUCODE.get()+"/'"+smwxjl.get()+"'/"+ProduceTime()+"/"+pinpai+"/"+fangxiang+"/"+reading.get()+"/"+surplus.get()+"/"+status);						
							HttpGet getMethod = new HttpGet(Vault.PHONE_URL+"checkState/"+CUCODE.get().trim().replaceAll(" ", "%20")+"/"+Util.getSharedPreference(RepairSlipRowModel.this.model.mContext, Vault.CHECKER_NAME));
							HttpClient httpClient = new DefaultHttpClient();
							HttpResponse response = httpClient.execute(getMethod);
							int code = response.getStatusLine().getStatusCode();
							if (code == 200) {// 若状态码为200
								msg.what = 1;
							} else {
								msg.what = 0;
							}
						} catch (IOException e) {
							e.printStackTrace();
							msg.what = 0;
						}finally{
							mHandler.sendMessage(msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			th.start();
		}
		
	};
	private void setMute() {
		SQLiteDatabase db = null;
		try
		{
			db = this.model.mContext.openOrCreateDatabase(Util.getDBName(this.model.mContext), Context.MODE_PRIVATE, null);
			db.execSQL("update T_BX_REPAIR_ALL set MUTE='1' where MUTE='0' and f_cucode="+CUCODE.get());
			Cursor c=null;
			c=db.rawQuery("select * from T_BX_REPAIR_ALL where MUTE='0'", null);
			int i=c.getCount();
			if(c.getCount()==0){
				this.model.mContext.sendMessageToService(AlarmService.MSG_FROM_ACTIVITY_MUTE);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		finally
		{
			if(db != null)
				db.close();
		}
	}
	
	// 反馈查看任务状态
		public Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (1 == msg.what) {
					super.handleMessage(msg);
				
				}
				else if(0 == msg.what)
				{

				}
			}
		};
	
}
