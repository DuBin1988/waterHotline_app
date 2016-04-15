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
 * ά��ģ��
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
		MUTED.set((flag & Vault.ITEM_ALARM)>0);//����
		REPAIRED.set((flag & Vault.ITEM_REPAIRED)>0);//��ά��
		INSPECTED.set((flag & Vault.ITEM_INSPECTED)>0);//�Ѽ�
		UPLOADED.set((flag & Vault.ITEM_RETURNED)>0);//�ѷ���
	}
	//����ID
	public String ID;
	//�Ƿ��Ѿ���
	private String MUTE;
	public BooleanObservable MUTED = new BooleanObservable(false);//����
	public BooleanObservable gongdanstatus = new BooleanObservable(true);//����
	public BooleanObservable REPAIRED = new BooleanObservable(false);//��ά��
	public BooleanObservable INSPECTED = new BooleanObservable(false); 
	public BooleanObservable UPLOADED = new BooleanObservable(false); 
	
	//public String USERID;//�û�ID
	public StringObservable USERNAME = new StringObservable("");//�û����� 
	public StringObservable USERADDRESS = new StringObservable("");//�û���ַ 
	public StringObservable GASUSERTYPE = new StringObservable("");//�û����
	public StringObservable CARDID = new StringObservable("");//�û����
	public StringObservable LINKTYPE = new StringObservable("");//�û��绰
	public StringObservable USERID = new StringObservable("");//�û��绰
	public StringObservable SENDER = new StringObservable("");//�ɵ���
	public StringObservable CUCODE = new StringObservable("");//���ޱ��
	public StringObservable LAIYUAN = new StringObservable("");//��Ϣ��Դ
	public StringObservable REPAIRTYPE = new StringObservable("");//��������
	public StringObservable PHONE = new StringObservable("");//�������
	public StringObservable SENDTIME = new StringObservable("");//�ɵ�ʱ��
	public StringObservable JIBIE = new StringObservable("");//�ɵ�ʱ��
	public StringObservable REPAIRREASON = new StringObservable("");//��������
	public StringObservable STOPREMARK = new StringObservable("");//��ע
	public StringObservable METERNUMBER = new StringObservable("");//��ע
	public StringObservable JIEDANDATE = new StringObservable("");//��ע
	public StringObservable JIEDANTIME = new StringObservable("");//��ע
	public StringObservable FUZEREN = new StringObservable("");//��ע
	public StringObservable WANGONGRIQI = new StringObservable("");//��ע
	public StringObservable WANGGONG = new StringObservable("");//��ע
	
//	public StringObservable METERNUMBER = new StringObservable("");//���
//	public StringObservable METERTYPE = new StringObservable("");//�����ͺ�
//	public StringObservable LEFTRIGHTWATCH = new StringObservable("");//���ұ�
//	public String lastrecord = "";//�����
	public StringObservable metergasnums = new StringObservable("");//�ۼƹ�����
	public StringObservable gasmeteraccomodations = new StringObservable("");//�����
	public StringObservable f_downloadstatus = new StringObservable("");//�����
	public StringObservable f_shixian = new StringObservable("");//�����
	
	
	public StringObservable smwxjl = new StringObservable("");//ά�޼�¼
	public StringObservable JIEGUO = new StringObservable("");//ά�޼�¼
	
	public String gaswatchbrand = "";//����Ʒ��
	//public String surplus = "";//������
	public String f_havacomplete = "";//���״̬
	
	public Command DetailCmd = new Command() {
		@Override
		public void Invoke(View arg0, Object... arg1) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("ID", ID);
			bundle.putString("USERID", USERID.get());
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
			bundle.putString("JIEDANDATE", JIEDANDATE.get());
			bundle.putString("JIEDANTIME", JIEDANTIME.get());
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
			bundle.putString("gdstatus", "isdo"); //���������ȥ��״̬
			
			setMute();//��������
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
							if (code == 200) {// ��״̬��Ϊ200
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
	
	// �����鿴����״̬
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
							f_downloadstatus.set("����");
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
					Toast.makeText(model.mContext, "�����ʽ����ȷ", Toast.LENGTH_SHORT).show();
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
								if (code == 200) {// ��״̬��Ϊ200
									msg.what = 3;
									msg.obj=EntityUtils.toString(response.getEntity(),"UTF8");
								}else{
									msg.what = 4;
									msg.obj="{\"f_info\":\"�����ȡ���ݳ���\"}";
								}
							} catch (IOException e) {
								e.printStackTrace();
								msg.what = 4;
								msg.obj="{\"f_info\":\"�����ȡ���ݳ���\"}";
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
		
		//ʧ�ܵ����ʧ������
		private void okAndDelete(String str){
			SQLiteDatabase db =null;
			SQLiteDatabase db2=null;
			try{
				db = model.mContext.openOrCreateDatabase(Util.getDBName(model.mContext), Context.MODE_PRIVATE, null);
				db2 = model.mContext.openOrCreateDatabase(Util.getDBName2(model.mContext), Context.MODE_PRIVATE, null);
				if("update".equals(str)){
					db2.execSQL("update T_BX_REPAIR_ALL set f_downloadstatus='����' where f_cucode='"+CUCODE.get()+"'");
					db.execSQL("update T_BX_REPAIR_ALL set f_downloadstatus='����' where f_cucode='"+CUCODE.get()+"'");
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
