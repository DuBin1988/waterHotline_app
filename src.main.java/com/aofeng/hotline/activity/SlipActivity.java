package com.aofeng.hotline.activity;

import gueei.binding.Command;
import gueei.binding.app.BindingActivity;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.aofeng.hotline.R;
import com.aofeng.utils.Util;
import com.aofeng.utils.Vault;


public class SlipActivity extends BindingActivity {
	private SlipActivity sa=this;
	private String shul ="";
	private String servercheck ="";
	private Bundle bundle=null;
	private String gdstatus=""; //判断是，工作，还是 查看工单
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setAndBindRootView(R.layout.repair, this);
		bundle = getIntent().getExtras();
		gdstatus=bundle.getString("gdstatus");
		if("ischeck".equals(gdstatus)){
			if("入户".equals(bundle.getString("inshi")))
				((Spinner)findViewById(R.id.inshi)).setSelection(0);
			else
				((Spinner)findViewById(R.id.inshi)).setSelection(1);
		}
		//sa.inshi.set(bundle.getString("gdstatus"));
		Spinner inshiListener =(Spinner)findViewById(R.id.inshi);
		inshiListener.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0,
					android.view.View arg1, int arg2, long arg3) {
				final String inshi1=sa.inshi.get(((Spinner)findViewById(R.id.inshi)).getSelectedItemPosition());
			//	surplus.set("0");
			//	reading.set("0");
				smwxjl.set("-");
				JIEGUO.set("-");
			//	((Spinner)findViewById(R.id.f_rqbiaoxing)).setSelection(0);
				((Spinner)findViewById(R.id.f_completion)).setSelection(1);
				if("入户".equals(inshi1)){
					((LinearLayout)findViewById(R.id.inshiline)).setVisibility(android.view.View.VISIBLE);
					//((Spinner)findViewById(R.id.f_gas_meter_brand)).setSelection(1);
					
				}else{
					((LinearLayout)findViewById(R.id.inshiline)).setVisibility(android.view.View.GONE);
					//((Spinner)findViewById(R.id.f_gas_meter_brand)).setSelection(0);
					
				}
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}

		});
	}

	
	@Override
	protected void onResume() {
		super.onResume();
	//	FillBindingList();
		View(bundle);
	}



	@Override
	public void onBackPressed() {
		if(isBusy)
			Toast.makeText(this, "请等待操作完成。", Toast.LENGTH_SHORT).show();
		else
		{
			this.finish();
			Intent intent = new Intent();
			if("ischeck".equals(gdstatus)) //返回之前的入口 
				intent.setClass(this, showhisActivity.class);
			else
				intent.setClass(this, MainActivity.class);
			//intent.setClass(this, MainActivity.class);
			startActivity(intent);
			super.onBackPressed();
		}
	}


	private void View(Bundle bundle) {
		ID=bundle.getString("ID");
		USERID.set(bundle.getString("USERID"));
		USERNAME.set(bundle.getString("USERNAME"));
		USERADDRESS.set(bundle.getString("USERADDRESS"));
		GASUSERTYPE.set(bundle.getString("GASUSERTYPE"));
		CARDID.set(bundle.getString("CARDID"));
		LINKTYPE.set(bundle.getString("LINKTYPE"));
		LAIYUAN.set(bundle.getString("LAIYUAN"));
		SENDER.set(bundle.getString("SENDER"));
		SENDTIME.set(bundle.getString("SENDTIME"));
		CUCODE.set(bundle.getString("CUCODE"));
		REPAIRTYPE.set(bundle.getString("REPAIRTYPE"));
		REPAIRREASON.set(bundle.getString("REPAIRREASON"));
		PHONE.set(bundle.getString("PHONE"));
		STOPREMARK.set(bundle.getString("STOPREMARK"));
		METERNUMBER.set(bundle.getString("METERNUMBER"));
		JIEDANDATE.set(bundle.getString("JIEDANDATE"));
		JIEDANTIME.set(bundle.getString("JIEDANTIME"));
		FUZEREN.set(bundle.getString("FUZEREN"));
		WANGONGRIQI.set(bundle.getString("WANGONGRIQI"));
		WANGGONG.set(bundle.getString("WANGGONG"));
		
	//	METERNUMBER.set(bundle.getString("METERNUMBER"));
	//	METERTYPE.set(bundle.getString("METERTYPE"));
	//	LEFTRIGHTWATCH.set(bundle.getString("LEFTRIGHTWATCH"));
	//	lastrecord.set(bundle.getString("lastrecord"));
	//	metergasnums.set(bundle.getString("metergasnums"));
	//	gasmeteraccomodations.set(bundle.getString("gasmeteraccomodations"));
		f_shixian.set(bundle.getString("f_shixian"));
		f_downloadstatus.set(bundle.getString("f_downloadstatus"));
		if("工单已撤销".equals(f_downloadstatus.get())){
			findViewById(R.id.button2).setVisibility(android.view.View.VISIBLE);
			findViewById(R.id.button1).setVisibility(android.view.View.GONE);
			//findViewById(R.id.Button02).setVisibility(android.view.View.GONE);
			//findViewById(R.id.button1).setVisibility(android.view.View.GONE);
		}else{
			findViewById(R.id.button2).setVisibility(android.view.View.GONE);
			//findViewById(R.id.Button02).setVisibility(android.view.View.VISIBLE);
			//findViewById(R.id.button1).setVisibility(android.view.View.VISIBLE);
		}
		
		smwxjl.set(bundle.getString("smwxjl"));
		JIEGUO.set(bundle.getString("JIEGUO"));
		
	//	Util.SelectItem(bundle.getString("gaswatchbrand"), this.gas_meter_brand, ((Spinner)findViewById(R.id.f_gas_meter_brand)));//设置
	//	Util.SelectItem(bundle.getString("LEFTRIGHTWATCH"), this.rqbiaoxing, ((Spinner)findViewById(R.id.f_rqbiaoxing)));//设置
		Util.SelectItem(bundle.getString("completion"), this.completion, ((Spinner)findViewById(R.id.f_completion)));//设置
	//	surplus.set(bundle.getString("surplus"));
		//reading.set(bundle.getString("lastrecord"));
		if("ischeck".equals(gdstatus)){ //返回之前的入口 
			findViewById(R.id.Button02).setVisibility(android.view.View.GONE);
			findViewById(R.id.button2).setVisibility(android.view.View.GONE);
			findViewById(R.id.call).setVisibility(android.view.View.GONE);
			findViewById(R.id.button1).setVisibility(android.view.View.GONE);
		}
	}

	
	private boolean isBusy = false;
	
	/**
	 * 退单
	 */
	public Command cmdDeal = new Command()
	{
		@Override//resultState
		public void Invoke(android.view.View arg0, Object... arg1) {
			Thread th = new Thread(new Runnable() {
				@Override
				public void run() {
					Message msg = new Message();
					try {
						HttpGet getMethod = new HttpGet(Vault.PHONE_URL + "resultState/" + CUCODE.get());
						HttpClient httpClient = new DefaultHttpClient();
						HttpResponse response = httpClient.execute(getMethod);
						int code = response.getStatusLine().getStatusCode();
						if (code == 200) {// 若状态码为200
							msg.what = 1;
						} else {
							msg.what = 0;
						}
					}  catch (IOException e) {
						e.printStackTrace();
						msg.what = 0;
					}finally{
						mCmdDeal.sendMessage(msg);
					}
				}
			});
			th.start();
		}
	};
	private final Handler mCmdDeal = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				Toast.makeText(sa, "退单成功", Toast.LENGTH_SHORT).show();
				deleteAndResult(openOrCreateDatabase(Util.getDBName(sa), Context.MODE_PRIVATE, null), CUCODE.get());
				onBackPressed();
			}else {
				Toast.makeText(sa, "退单失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	/**
	 * 上传
	 */
	public Command upload = new Command(){
		@Override//resultState
		public void Invoke(android.view.View arg0, Object... arg1){
		//	final String pinpai=sa.gas_meter_brand.get(((Spinner)findViewById(R.id.f_gas_meter_brand)).getSelectedItemPosition());
		//	final String fangxiang=sa.rqbiaoxing.get(((Spinner)findViewById(R.id.f_rqbiaoxing)).getSelectedItemPosition());
			final String status=sa.completion.get(((Spinner)findViewById(R.id.f_completion)).getSelectedItemPosition());
			final String inshi1=sa.inshi.get(((Spinner)findViewById(R.id.inshi)).getSelectedItemPosition());
			if("是".equals(inshi1)){
				if(Check(smwxjl)||Check(JIEGUO)){
					//||pinpai==null||pinpai==""||reading.get()==null||reading.get()==""||surplus.get()==null||surplus.get()==""
					Toast.makeText(sa, "处理情况不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			Thread th = new Thread(new Runnable(){
				@Override
				public void run() {
					Message msg = new Message();
					try {
						
						//servercheck_shul();
						
						// HttpGet getMethod = new HttpGet(Vault.PHONE_URL+"upload/"+CUCODE.get()+"/'"+smwxjl.get()+"'/"+ProduceTime()+"/"+pinpai+"/"+fangxiang+"/"+reading.get()+"/"+surplus.get()+"/"+status);						
						HttpGet getMethod = new HttpGet(Vault.PHONE_URL+"upload/"+CUCODE.get().trim().replaceAll(" ", "%20")+"/'"+inshi1.trim().replaceAll(" ", "%20")+":"+smwxjl.get().trim().replaceAll("\n", "。").replaceAll(" ", "%20")+"'/"+":"+JIEGUO.get().trim().replaceAll("\n", "。").replaceAll(" ", "%20")+"'/"+ProduceTime().trim().replaceAll(" ", "%20")+"/"+status.trim().replaceAll(" ", "%20"));//+"/"+servercheck.trim().replaceAll(" ", "%20")+"/"+shul.trim().replaceAll(" ", "%20"));
						HttpClient httpClient = new DefaultHttpClient();
						//+pinpai.trim().replaceAll(" ", "%20")+"/"+fangxiang.trim().replaceAll(" ", "%20")+"/"+reading.get().trim().replaceAll(" ", "%20")+"/"+surplus.get().trim().replaceAll(" ", "%20")+"/"
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
				}
			});
			th.start();
		}
	};
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				Toast.makeText(sa, "上传维修记录成功", Toast.LENGTH_SHORT).show();
				/*if(sa.completion.get(((Spinner)findViewById(R.id.f_completion)).getSelectedItemPosition()).equals("已完成") || sa.completion.get(((Spinner)findViewById(R.id.f_completion)).getSelectedItemPosition()).equals("不具备通气")){
					deleteAndResult(openOrCreateDatabase(Util.getDBName(sa), Context.MODE_PRIVATE, null), CUCODE.get());
				}else{
					localSave();
				}*/
				f_uploadstatus.set("true");
				localSave();
				onBackPressed();
			} else if(msg.what==0){
				if(localSave()){
					f_uploadstatus.set("false");
					Toast.makeText(sa, "本地保存维修记录成功", Toast.LENGTH_SHORT).show();
					onBackPressed();
				}else{
					Toast.makeText(sa, "本地保存维修记录失败", Toast.LENGTH_SHORT).show();
				}
				return;
			}
		}
	};
	private boolean localSave(){
		SQLiteDatabase db = null;
		Date d=new Date();
		try
		{
			//servercheck_shul();
			
			String sql = "update T_BX_REPAIR_ALL set " +
					"f_qingkuang=?," +//维修记录
					"f_jieguo=?," +//维修记录
				//	"finishtime=?," +//维修时间
				//	"f_gaswatchbrand=?," +//更换后气表品牌
					//"f_aroundmeter=?," +//左右表
				//	"f_lastrecord=?," +//表读数
				//	"surplus=?," +//补气量
				//	"completion=?, " +//
					"f_uploadstatus=? " +//
				//	"inshi=? " +//
					//"servercheck=?, " +//
					//"shul=? " +//
					"where f_cucode=?";
			db = openOrCreateDatabase(Util.getDBName(this), Context.MODE_PRIVATE, null);
			db.execSQL(sql, new String[]{smwxjl.get(),//维修记录
					                   JIEGUO.get(),//结果查询
									//	ProduceTime(),//维修时间
								//		this.gas_meter_brand.get(((Spinner)findViewById(R.id.f_gas_meter_brand)).getSelectedItemPosition()),//气表品牌
								//		this.rqbiaoxing.get(((Spinner)findViewById(R.id.f_rqbiaoxing)).getSelectedItemPosition()),//左右表
									//	reading.get(),//表读数
										//surplus.get(),//补气量
									//	this.completion.get(((Spinner)findViewById(R.id.f_completion)).getSelectedItemPosition()),//完成状态
										this.f_uploadstatus.get(),//上传标记
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
	
	/*
	private void servercheck_shul(){
		shul=check_null(f_singlemonth.get()).trim()+"-:"+check_null(f_innerstraight.get()).trim()+"-:"+check_null(f_outstraight.get()).trim()+"-:"+check_null(f_metercontact.get()).trim()+"-:"+check_null(f_metermats.get().trim())+"-:"+check_null(f_bend.get()).trim()+"-:"+check_null(f_shortsilk.get()).trim()+"-:"+check_null(f_aluminumtube.get()).trim();
		servercheck=f_openstopper.get()+"-:"+f_openfire.get()+"-:"+f_meterafter.get()+"-:"+f_chaseleaks.get()+"-:"+f_service.get()+"-:"+f_repair.get()+"-:"+f_installcalorifier.get()+"-:"+f_changemeter.get()+"-:"+f_linksinglemonth.get()+"-:"+f_refitmeter.get()+"-:"+f_refitmeterafter.get()+"-:"+f_moveverticalbar.get()+"-:"+f_frostdamage.get()+"-:"+f_otherservice.get();
	}
	*/
	private String check_null(String s){
		if(null!=s && !"".equals(s)){
			return s;
		}else{
			return "0";
		}
	}
	
	private boolean Check(StringObservable so){
		if(so.get()==null||so.get()==""){//^[_,.!\n\w\u4e00-\u9fa5]*$
			return true;
		}else{
			return false;
		}
	}
	
	private String ProduceTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		Date d=new Date();
		return sdf.format(d);
	}
	
	/**
	 * 状态确认成功
	 */
	public Command querenDeal = new Command()
	{
		@Override//resultState
		public void Invoke(android.view.View arg0, Object... arg1) {
			
					Toast.makeText(sa, "状态确认成功", Toast.LENGTH_SHORT).show();
					deleteAndResult(openOrCreateDatabase(Util.getDBName(sa), Context.MODE_PRIVATE, null), CUCODE.get());
					onBackPressed();
				
		}
	};
	
	/**
	 * 安检
	 */
//	public Command onInspect = new Command()
//	{
//		@Override
//		public void Invoke(View arg0, Object... arg1) {
//			if(ResultList.size()==0)
//			{
//				Toast.makeText(RepairResultActivity.this, "请先添加维修记录。", Toast.LENGTH_SHORT).show();
//				return;
//			}
////			if(cardId.get().length()!=10)
////			{
////				Toast.makeText(RepairResultActivity.this, "卡号必须为10位。", Toast.LENGTH_SHORT).show();
////				return;
////			}
//			updateRepairState(Vault.ITEM_REPAIRED);
//			//删除缓存
//			Util.ClearCache(RepairResultActivity.this, 
//					CUCODE.get() + "_" + Util.getSharedPreference(RepairResultActivity.this, Vault.USER_NAME));
//			Intent intent = new Intent();
//			// 利用包袱传递参数给Activity
//			Bundle bundle = new Bundle();
//			bundle.putString("ID", CUCODE.get());
//			bundle.putString("slipId", ID);
//			bundle.putString("CUCODE",  CUCODE.get());
//			bundle.putBoolean("INSPECTED", inspected);
////			bundle.putString("CardID", cardId.get());
//			intent.setClass(RepairResultActivity.this, IndoorInspectActivity.class);
//			intent.putExtras(bundle);
//			startActivity(intent);			
//		}
//		
//	};
	/**
	 * 删除
	 * @param db
	 * @param tablename
	 * @param cucode
	 * @return
	 */
	private void deleteAndResult(SQLiteDatabase db,String cucode){
		try
		{
			db.execSQL("delete from T_BX_REPAIR_ALL where f_cucode="+cucode);
			db=openOrCreateDatabase(Util.getDBName2(sa), Context.MODE_PRIVATE, null);
			db.execSQL("delete from T_BX_REPAIR_ALL where f_cucode="+cucode);
			db.close();
		} catch(Exception e){
			e.printStackTrace();
			Toast.makeText(sa, "删除本地维修记录失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 根据参数填充列表
	 */
//	private void FillBindingList() {
//	//	Object[] list = {this.gas_meter_brand};
//		String[] codes = {"气表品牌"};
//		SQLiteDatabase db = null;
//		try
//		{
//			db=openOrCreateDatabase(Util.getDBName(sa), Context.MODE_PRIVATE, null);
//			for(int i=0; i<codes.length; i++)
//			{
//				ArrayListObservable<String> olist = (ArrayListObservable<String>)list[i];
//				olist.clear();
//				olist.add("未入户");
//				olist.add("不确定");
//				String sql = "select NAME from T_PARAMS where code=? order by id";
//				Cursor c = db.rawQuery(sql, new String[] { codes[i] });
//				while(c.moveToNext())
//				{
//					olist.add(c.getString(0));
//				}
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		finally
//		{
//			if(db != null)
//				db.close();
//		}	
//		
//	}
	
	private String ID;
	//public String USERID;//用户ID
	public StringObservable USERNAME = new StringObservable("");//用户姓名 
	public StringObservable USERID = new StringObservable("");//用户姓名 
	public StringObservable USERADDRESS = new StringObservable("");//用户地址 
	public StringObservable GASUSERTYPE = new StringObservable("");//用户类别
	public StringObservable CARDID = new StringObservable("");//用户类别
	public StringObservable LINKTYPE = new StringObservable("");//用户电话
	public StringObservable LAIYUAN = new StringObservable("");//信息来源
	public StringObservable SENDER = new StringObservable("");//派单人
	public StringObservable CUCODE = new StringObservable("");//报修编号
	public StringObservable REPAIRTYPE = new StringObservable("");//报修类型
	public StringObservable PHONE = new StringObservable("");//来电号码
	public StringObservable SENDTIME = new StringObservable("");//派单时间
	public StringObservable REPAIRREASON = new StringObservable("");//反映内容
	public StringObservable STOPREMARK = new StringObservable("");//备注
	public StringObservable METERNUMBER = new StringObservable("");//备注
	public StringObservable JIEDANDATE = new StringObservable("");//备注
	public StringObservable JIEDANTIME = new StringObservable("");//备注
	public StringObservable FUZEREN = new StringObservable("");//备注
	public StringObservable WANGONGRIQI = new StringObservable("");//备注
	public StringObservable WANGGONG = new StringObservable("");//备注
//	public StringObservable METERNUMBER = new StringObservable("");//表号
	//public StringObservable METERTYPE = new StringObservable("");//气表型号
//	public StringObservable LEFTRIGHTWATCH = new StringObservable("");//左右表
//	public StringObservable lastrecord = new StringObservable("");//表读数
//	public StringObservable metergasnums = new StringObservable("");//累计购气量
//	public StringObservable gasmeteraccomodations = new StringObservable("");//表底数
	public StringObservable f_downloadstatus = new StringObservable("");//表底数
	public StringObservable smwxjl = new StringObservable("");//维修结果
	public StringObservable JIEGUO = new StringObservable("");//维修结果
	//public ArrayListObservable<String> gas_meter_brand = new ArrayListObservable<String>(String.class);//气表品牌
//	public ArrayListObservable<String> rqbiaoxing = new ArrayListObservable<String>(String.class, new String[]{"左表", "右表"});//左右表
	public ArrayListObservable<String> completion = new ArrayListObservable<String>(String.class, new String[]{"已完成","未完成"});//完成情况
//	public StringObservable reading = new StringObservable("");//当前读数
	//public StringObservable surplus = new StringObservable("");//剩余气量
	public ArrayListObservable<String> inshi = new ArrayListObservable<String>(String.class, new String[]{"是", "否"});//完成情况
	public StringObservable f_shixian = new StringObservable("");
	public StringObservable f_uploadstatus = new StringObservable("false");
	/*  -   
	public BooleanObservable f_openstopper = new BooleanObservable(false);
	public BooleanObservable f_openfire = new BooleanObservable(false);
	public BooleanObservable f_meterafter = new BooleanObservable(false);
	public BooleanObservable f_chaseleaks = new BooleanObservable(false);
	public BooleanObservable f_service = new BooleanObservable(false);
	public BooleanObservable f_repair = new BooleanObservable(false);
	public BooleanObservable f_installcalorifier = new BooleanObservable(false);
	public BooleanObservable f_changemeter = new BooleanObservable(false);
	public BooleanObservable f_linksinglemonth = new BooleanObservable(false);
	public BooleanObservable f_refitmeter = new BooleanObservable(false);
	public BooleanObservable f_refitmeterafter = new BooleanObservable(false);
	public BooleanObservable f_moveverticalbar = new BooleanObservable(false);
	public BooleanObservable f_frostdamage = new BooleanObservable(false);
	public BooleanObservable f_otherservice = new BooleanObservable(false);
	
	
	
	public StringObservable f_singlemonth = new StringObservable("");
	public StringObservable f_innerstraight = new StringObservable("");
	public StringObservable f_outstraight = new StringObservable("");
	public StringObservable f_metercontact = new StringObservable("");
	public StringObservable f_metermats = new StringObservable("");
	public StringObservable f_bend = new StringObservable("");
	public StringObservable f_shortsilk = new StringObservable("");
	public StringObservable f_aluminumtube = new StringObservable("");
	
	*/
	
//	@Override
//	protected void onDestroy() {
//		Intent intent = new Intent();
//		intent.setClass(this, MainActivity.class);
//		startActivity(intent);
//		super.onDestroy();
//	}
	
	public Command call = new Command(){
		@Override
		public void Invoke(android.view.View arg0, Object... arg1) {
			String phone = PHONE.get().trim();
			if(phone!=null&&!"".equals(phone)&&phone.matches("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)")){
				Uri uri=Uri.parse("tel:"+phone);  
				Intent intent=new Intent();  
				intent.setAction(Intent.ACTION_CALL);  
				intent.setData(uri);  
				SlipActivity.this.startActivity(intent);
			}else{
				Toast.makeText(sa, "号码格式不正确", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
}
