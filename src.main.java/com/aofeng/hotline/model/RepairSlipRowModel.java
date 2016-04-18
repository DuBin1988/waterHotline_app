package com.aofeng.hotline.model;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aofeng.hotline.R;
import com.aofeng.hotline.activity.MainActivity;
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
	public BooleanObservable gongdanstatus = new BooleanObservable(true);//报警
	public BooleanObservable REPAIRED = new BooleanObservable(false);//已维修
	public BooleanObservable INSPECTED = new BooleanObservable(false); 
	public BooleanObservable UPLOADED = new BooleanObservable(false); 
	
	//public String USERID;//用户ID
	public StringObservable USERNAME = new StringObservable("");//用户姓名 
	public StringObservable REPAIRID = new StringObservable("");//单子编号
	public StringObservable USERADDRESS = new StringObservable("");//用户地址 
	public StringObservable GASUSERTYPE = new StringObservable("");//用户类别
	public StringObservable CARDID = new StringObservable("");//用户类别
	public StringObservable LINKTYPE = new StringObservable("");//用户电话
	public StringObservable USERID = new StringObservable("");//用户电话
	public StringObservable SENDER = new StringObservable("");//派单人
	public StringObservable CUCODE = new StringObservable("");//报修编号
	public StringObservable LAIYUAN = new StringObservable("");//信息来源
	public StringObservable REPAIRTYPE = new StringObservable("");//报修类型
	public StringObservable PHONE = new StringObservable("");//来电号码
	public StringObservable SENDTIME = new StringObservable("");//派单时间
	public StringObservable JIBIE = new StringObservable("");//派单时间
	public StringObservable REPAIRREASON = new StringObservable("");//来电内容
	public StringObservable STOPREMARK = new StringObservable("");//备注
	public StringObservable METERNUMBER = new StringObservable("");//备注
	public StringObservable JIEDANDATE = new StringObservable("");//备注
	public StringObservable JIEDANTIME = new StringObservable("");//备注
	public StringObservable FUZEREN = new StringObservable("");//备注
	public StringObservable WANGONGRIQI = new StringObservable("");//备注
	public StringObservable WANGGONG = new StringObservable("");//备注
	
//	public StringObservable METERNUMBER = new StringObservable("");//表号
//	public StringObservable METERTYPE = new StringObservable("");//气表型号
//	public StringObservable LEFTRIGHTWATCH = new StringObservable("");//左右表
//	public String lastrecord = "";//表读数
	public StringObservable metergasnums = new StringObservable("");//累计购气量
	public StringObservable gasmeteraccomodations = new StringObservable("");//表底数
	public StringObservable f_downloadstatus = new StringObservable("");//表底数
	public StringObservable f_shixian = new StringObservable("");//表底数
	
	
	public StringObservable smwxjl = new StringObservable("");//维修记录
	public StringObservable JIEGUO = new StringObservable("");//维修记录
	
	public String gaswatchbrand = "";//气表品牌
	//public String surplus = "";//补气量
	public String f_havacomplete = "";//完成状态
	 
	public Command DetailCmd = new Command() {
	
		@Override
		public void Invoke(View arg0, Object... arg1) {
			Intent intent = new Intent();
			Bundle  bundle = new Bundle();
			bundle.putString("JIEDANDATE", JIEDANDATE.get());
			bundle.putString("JIEDANTIME", JIEDANTIME.get());
			if(JIEDANDATE.get()==null){
				  localSave();
			}
			bundle.putString("ID", ID);
			bundle.putString("USERID", USERID.get());
			bundle.putString("REPAIRID", REPAIRID.get());
			bundle.putString("USERNAME", USERNAME.get());
			bundle.putString("USERADDRESS", USERADDRESS.get());
			bundle.putString("GASUSERTYPE", GASUSERTYPE.get());
			bundle.putString("CARDID", CARDID.get());
			bundle.putString("LINKTYPE", LINKTYPE.get());
			bundle.putString("SENDER", SENDER.get());
			bundle.putString("CUCODE", CUCODE.get());
			bundle.putString("LAIYUAN", LAIYUAN.get());
			bundle.putString("REPAIRTYPE", REPAIRTYPE.get());
			bundle.putString("PHONE", PHONE.get());
			bundle.putString("SENDTIME", SENDTIME.get());
			bundle.putString("REPAIRREASON", REPAIRREASON.get());
			bundle.putString("STOPREMARK", STOPREMARK.get());
			bundle.putString("METERNUMBER", METERNUMBER.get());
			
		//	bundle.putString("FUZEREN",FUZEREN.get());
			bundle.putString("WANGONGRIQI",WANGONGRIQI.get());
			bundle.putString("WANGGONG",WANGGONG.get());
	//		bundle.putString("METERNUMBER", METERNUMBER.get());
	//		bundle.putString("METERTYPE", METERTYPE.get());
	//		bundle.putString("LEFTRIGHTWATCH", LEFTRIGHTWATCH.get());
		//	bundle.putString("lastrecord", lastrecord);
			bundle.putString("metergasnums", metergasnums.get());
			bundle.putString("gasmeteraccomodations", gasmeteraccomodations.get());
//			bundle.putString("f_downloadstatus", f_downloadstatus.get());
//			bundle.putString("f_workingdays", f_workingdays.get());
			
			bundle.putString("smwxjl", smwxjl.get());
			bundle.putString("JIEGUO", JIEGUO.get());
			
			bundle.putString("gaswatchbrand", gaswatchbrand);
		//	bundle.putString("surplus", surplus);
			bundle.putString("f_havacomplete", f_havacomplete);
			bundle.putString("f_downloadstatus", f_downloadstatus.get());
			bundle.putString("f_shixian", f_shixian.get());
			bundle.putString("gdstatus", "isdo"); //工单传入过去的状态
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
	
	


	private String JieDanDate(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date d=new Date();
		return sdf.format(d);
	}
	private String JieDanTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
		Date d=new Date();
		return sdf.format(d);
	}
	private boolean localSave(){
		SQLiteDatabase db = null;
		Date d=new Date();
		try
		{
			String sql = "update T_BX_REPAIR_ALL set " +
//					"f_qingkuang=?," +//维修记录
//					"f_jieguo=?," +//维修记录
					"f_jiedandate=?,"+
					"f_jiedantime=?"+
				//	"finishtime=?," +//维修时间
				//	"f_gaswatchbrand=?," +//更换后气表品牌
					//"f_aroundmeter=?," +//左右表
				//	"f_lastrecord=?," +//表读数
				//	"surplus=?," +//补气量
				//	"completion=?, " +//
//					"f_uploadstatus=? " +//
				//	"inshi=? " +//
					//"servercheck=?, " +//
					//"shul=? " +//
					"where f_cucode=?";
			db = this.model.mContext.openOrCreateDatabase(Util.getDBName(this.model.mContext), Context.MODE_PRIVATE, null);
			db.execSQL(sql, new String[]{
//					                   smwxjl.get(),//维修记录
//					                   JIEGUO.get(),//结果查询
					                  JieDanDate().trim(),
					                   JieDanTime().trim(),
									//	ProduceTime(),//维修时间
								//		this.gas_meter_brand.get(((Spinner)findViewById(R.id.f_gas_meter_brand)).getSelectedItemPosition()),//气表品牌
								//		this.rqbiaoxing.get(((Spinner)findViewById(R.id.f_rqbiaoxing)).getSelectedItemPosition()),//左右表
									//	reading.get(),//表读数
										//surplus.get(),//补气量
									//	this.completion.get(((Spinner)findViewById(R.id.f_completion)).getSelectedItemPosition()),//完成状态
//										this.f_uploadstatus.get(),//上传标记
									//	this.inshi.get(((Spinner)findViewById(R.id.inshi)).getSelectedItemPosition()),//是否入户
										//this.servercheck,//是否入户
										//this.shul,//是否入户
										CUCODE.get()});

			return true;
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			db.close();
		}
	}


	
	
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
				else if(3 == msg.what)
				{
					try {						
						JSONObject obj=new JSONObject(msg.obj.toString());
						if("ok".equals(obj.getString("f_qdstatus"))){
							gongdanstatus.set(true);
							f_downloadstatus.set("正常");
							okAndDelete("update");							
						}else{
							okAndDelete("delete");
							model.fillItem();
						}						
						Toast.makeText(model.mContext, obj.getString("f_info"), Toast.LENGTH_LONG).show();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				else if(4 == msg.what)
				{
					try {
						JSONObject obj=new JSONObject(msg.obj.toString());
						Toast.makeText(model.mContext, obj.getString("f_info"), Toast.LENGTH_LONG).show();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		public Command call = new Command(){
			@Override
			public void Invoke(android.view.View arg0, Object... arg1) {
				String phone = PHONE.get().trim();
				if(phone!=null&&!"".equals(phone)&&phone.matches("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)")){
					Uri uri=Uri.parse("tel:"+phone);  
					Intent intent=new Intent();  
					intent.setAction(Intent.ACTION_CALL);  
					intent.setData(uri);  
					model.mContext.startActivity(intent);
				}else{
					Toast.makeText(model.mContext, "号码格式不正确", Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		public Command qiangdan = new Command(){
			@Override
			public void Invoke(android.view.View arg0, Object... arg1) {
				
				Thread th = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Message msg = new Message();
							try {
								// HttpGet getMethod = new HttpGet(Vault.PHONE_URL+"upload/"+CUCODE.get()+"/'"+smwxjl.get()+"'/"+ProduceTime()+"/"+pinpai+"/"+fangxiang+"/"+reading.get()+"/"+surplus.get()+"/"+status);						
								HttpGet getMethod = new HttpGet(Vault.PHONE_URL+"qiangdan/"+CUCODE.get().trim().replaceAll(" ", "%20")+"/"+Util.getSharedPreference(RepairSlipRowModel.this.model.mContext, Vault.CHECKER_NAME));
								HttpClient httpClient = new DefaultHttpClient();
								HttpResponse response = httpClient.execute(getMethod);
								int code = response.getStatusLine().getStatusCode();
								if (code == 200) {// 若状态码为200
									msg.what = 3;
									msg.obj=EntityUtils.toString(response.getEntity(),"UTF8");
								}else{
									msg.what = 4;
									msg.obj="{\"f_info\":\"网络读取数据出错\"}";
								}
							} catch (IOException e) {
								e.printStackTrace();
								msg.what = 4;
								msg.obj="{\"f_info\":\"网络读取数据出错\"}";
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
		
		//失败的清楚失败数据
		private void okAndDelete(String str){
			SQLiteDatabase db =null;
			SQLiteDatabase db2=null;
			try{
				db = model.mContext.openOrCreateDatabase(Util.getDBName(model.mContext), Context.MODE_PRIVATE, null);
				db2 = model.mContext.openOrCreateDatabase(Util.getDBName2(model.mContext), Context.MODE_PRIVATE, null);
				if("update".equals(str)){
					db2.execSQL("update T_BX_REPAIR_ALL set f_downloadstatus='正常' where f_cucode='"+CUCODE.get()+"'");
					db.execSQL("update T_BX_REPAIR_ALL set f_downloadstatus='正常' where f_cucode='"+CUCODE.get()+"'");
				}else{
					db2.execSQL("delete from T_BX_REPAIR_ALL where f_cucode='"+CUCODE.get()+"'");
					 db.execSQL("delete from T_BX_REPAIR_ALL where f_cucode='"+CUCODE.get()+"'");
				}
			} catch(Exception e){
				e.printStackTrace();
			}finally{
				db.close();
				db2.close();
			}
		}
		
	
}
