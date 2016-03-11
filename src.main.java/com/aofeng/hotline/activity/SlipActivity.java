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
	private String gdstatus=""; //�ж��ǣ����������� �鿴����
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setAndBindRootView(R.layout.repair, this);
		bundle = getIntent().getExtras();
		gdstatus=bundle.getString("gdstatus");
		if("ischeck".equals(gdstatus)){
			if("�뻧".equals(bundle.getString("inshi")))
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
				if("�뻧".equals(inshi1)){
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
			Toast.makeText(this, "��ȴ�������ɡ�", Toast.LENGTH_SHORT).show();
		else
		{
			this.finish();
			Intent intent = new Intent();
			if("ischeck".equals(gdstatus)) //����֮ǰ����� 
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
		if("�����ѳ���".equals(f_downloadstatus.get())){
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
		
	//	Util.SelectItem(bundle.getString("gaswatchbrand"), this.gas_meter_brand, ((Spinner)findViewById(R.id.f_gas_meter_brand)));//����
	//	Util.SelectItem(bundle.getString("LEFTRIGHTWATCH"), this.rqbiaoxing, ((Spinner)findViewById(R.id.f_rqbiaoxing)));//����
		Util.SelectItem(bundle.getString("completion"), this.completion, ((Spinner)findViewById(R.id.f_completion)));//����
	//	surplus.set(bundle.getString("surplus"));
		//reading.set(bundle.getString("lastrecord"));
		if("ischeck".equals(gdstatus)){ //����֮ǰ����� 
			findViewById(R.id.Button02).setVisibility(android.view.View.GONE);
			findViewById(R.id.button2).setVisibility(android.view.View.GONE);
			findViewById(R.id.call).setVisibility(android.view.View.GONE);
			findViewById(R.id.button1).setVisibility(android.view.View.GONE);
		}
	}

	
	private boolean isBusy = false;
	
	/**
	 * �˵�
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
						if (code == 200) {// ��״̬��Ϊ200
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
				Toast.makeText(sa, "�˵��ɹ�", Toast.LENGTH_SHORT).show();
				deleteAndResult(openOrCreateDatabase(Util.getDBName(sa), Context.MODE_PRIVATE, null), CUCODE.get());
				onBackPressed();
			}else {
				Toast.makeText(sa, "�˵�ʧ��", Toast.LENGTH_SHORT).show();
			}
		}
	};
	/**
	 * �ϴ�
	 */
	public Command upload = new Command(){
		@Override//resultState
		public void Invoke(android.view.View arg0, Object... arg1){
		//	final String pinpai=sa.gas_meter_brand.get(((Spinner)findViewById(R.id.f_gas_meter_brand)).getSelectedItemPosition());
		//	final String fangxiang=sa.rqbiaoxing.get(((Spinner)findViewById(R.id.f_rqbiaoxing)).getSelectedItemPosition());
			final String status=sa.completion.get(((Spinner)findViewById(R.id.f_completion)).getSelectedItemPosition());
			final String inshi1=sa.inshi.get(((Spinner)findViewById(R.id.inshi)).getSelectedItemPosition());
			if("��".equals(inshi1)){
				if(Check(smwxjl)||Check(JIEGUO)){
					//||pinpai==null||pinpai==""||reading.get()==null||reading.get()==""||surplus.get()==null||surplus.get()==""
					Toast.makeText(sa, "�����������Ϊ��", Toast.LENGTH_SHORT).show();
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
						HttpGet getMethod = new HttpGet(Vault.PHONE_URL+"upload/"+CUCODE.get().trim().replaceAll(" ", "%20")+"/'"+inshi1.trim().replaceAll(" ", "%20")+":"+smwxjl.get().trim().replaceAll("\n", "��").replaceAll(" ", "%20")+"'/"+":"+JIEGUO.get().trim().replaceAll("\n", "��").replaceAll(" ", "%20")+"'/"+ProduceTime().trim().replaceAll(" ", "%20")+"/"+status.trim().replaceAll(" ", "%20"));//+"/"+servercheck.trim().replaceAll(" ", "%20")+"/"+shul.trim().replaceAll(" ", "%20"));
						HttpClient httpClient = new DefaultHttpClient();
						//+pinpai.trim().replaceAll(" ", "%20")+"/"+fangxiang.trim().replaceAll(" ", "%20")+"/"+reading.get().trim().replaceAll(" ", "%20")+"/"+surplus.get().trim().replaceAll(" ", "%20")+"/"
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
				Toast.makeText(sa, "�ϴ�ά�޼�¼�ɹ�", Toast.LENGTH_SHORT).show();
				/*if(sa.completion.get(((Spinner)findViewById(R.id.f_completion)).getSelectedItemPosition()).equals("�����") || sa.completion.get(((Spinner)findViewById(R.id.f_completion)).getSelectedItemPosition()).equals("���߱�ͨ��")){
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
					Toast.makeText(sa, "���ر���ά�޼�¼�ɹ�", Toast.LENGTH_SHORT).show();
					onBackPressed();
				}else{
					Toast.makeText(sa, "���ر���ά�޼�¼ʧ��", Toast.LENGTH_SHORT).show();
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
					"f_qingkuang=?," +//ά�޼�¼
					"f_jieguo=?," +//ά�޼�¼
				//	"finishtime=?," +//ά��ʱ��
				//	"f_gaswatchbrand=?," +//����������Ʒ��
					//"f_aroundmeter=?," +//���ұ�
				//	"f_lastrecord=?," +//�����
				//	"surplus=?," +//������
				//	"completion=?, " +//
					"f_uploadstatus=? " +//
				//	"inshi=? " +//
					//"servercheck=?, " +//
					//"shul=? " +//
					"where f_cucode=?";
			db = openOrCreateDatabase(Util.getDBName(this), Context.MODE_PRIVATE, null);
			db.execSQL(sql, new String[]{smwxjl.get(),//ά�޼�¼
					                   JIEGUO.get(),//�����ѯ
									//	ProduceTime(),//ά��ʱ��
								//		this.gas_meter_brand.get(((Spinner)findViewById(R.id.f_gas_meter_brand)).getSelectedItemPosition()),//����Ʒ��
								//		this.rqbiaoxing.get(((Spinner)findViewById(R.id.f_rqbiaoxing)).getSelectedItemPosition()),//���ұ�
									//	reading.get(),//�����
										//surplus.get(),//������
									//	this.completion.get(((Spinner)findViewById(R.id.f_completion)).getSelectedItemPosition()),//���״̬
										this.f_uploadstatus.get(),//�ϴ����
									//	this.inshi.get(((Spinner)findViewById(R.id.inshi)).getSelectedItemPosition()),//�Ƿ��뻧
										//this.servercheck,//�Ƿ��뻧
										//this.shul,//�Ƿ��뻧
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
	 * ״̬ȷ�ϳɹ�
	 */
	public Command querenDeal = new Command()
	{
		@Override//resultState
		public void Invoke(android.view.View arg0, Object... arg1) {
			
					Toast.makeText(sa, "״̬ȷ�ϳɹ�", Toast.LENGTH_SHORT).show();
					deleteAndResult(openOrCreateDatabase(Util.getDBName(sa), Context.MODE_PRIVATE, null), CUCODE.get());
					onBackPressed();
				
		}
	};
	
	/**
	 * ����
	 */
//	public Command onInspect = new Command()
//	{
//		@Override
//		public void Invoke(View arg0, Object... arg1) {
//			if(ResultList.size()==0)
//			{
//				Toast.makeText(RepairResultActivity.this, "�������ά�޼�¼��", Toast.LENGTH_SHORT).show();
//				return;
//			}
////			if(cardId.get().length()!=10)
////			{
////				Toast.makeText(RepairResultActivity.this, "���ű���Ϊ10λ��", Toast.LENGTH_SHORT).show();
////				return;
////			}
//			updateRepairState(Vault.ITEM_REPAIRED);
//			//ɾ������
//			Util.ClearCache(RepairResultActivity.this, 
//					CUCODE.get() + "_" + Util.getSharedPreference(RepairResultActivity.this, Vault.USER_NAME));
//			Intent intent = new Intent();
//			// ���ð������ݲ�����Activity
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
	 * ɾ��
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
			Toast.makeText(sa, "ɾ������ά�޼�¼ʧ��", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * ���ݲ�������б�
	 */
//	private void FillBindingList() {
//	//	Object[] list = {this.gas_meter_brand};
//		String[] codes = {"����Ʒ��"};
//		SQLiteDatabase db = null;
//		try
//		{
//			db=openOrCreateDatabase(Util.getDBName(sa), Context.MODE_PRIVATE, null);
//			for(int i=0; i<codes.length; i++)
//			{
//				ArrayListObservable<String> olist = (ArrayListObservable<String>)list[i];
//				olist.clear();
//				olist.add("δ�뻧");
//				olist.add("��ȷ��");
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
	//public String USERID;//�û�ID
	public StringObservable USERNAME = new StringObservable("");//�û����� 
	public StringObservable USERID = new StringObservable("");//�û����� 
	public StringObservable USERADDRESS = new StringObservable("");//�û���ַ 
	public StringObservable GASUSERTYPE = new StringObservable("");//�û����
	public StringObservable CARDID = new StringObservable("");//�û����
	public StringObservable LINKTYPE = new StringObservable("");//�û��绰
	public StringObservable LAIYUAN = new StringObservable("");//��Ϣ��Դ
	public StringObservable SENDER = new StringObservable("");//�ɵ���
	public StringObservable CUCODE = new StringObservable("");//���ޱ��
	public StringObservable REPAIRTYPE = new StringObservable("");//��������
	public StringObservable PHONE = new StringObservable("");//�������
	public StringObservable SENDTIME = new StringObservable("");//�ɵ�ʱ��
	public StringObservable REPAIRREASON = new StringObservable("");//��ӳ����
	public StringObservable STOPREMARK = new StringObservable("");//��ע
	public StringObservable METERNUMBER = new StringObservable("");//��ע
	public StringObservable JIEDANDATE = new StringObservable("");//��ע
	public StringObservable JIEDANTIME = new StringObservable("");//��ע
	public StringObservable FUZEREN = new StringObservable("");//��ע
	public StringObservable WANGONGRIQI = new StringObservable("");//��ע
	public StringObservable WANGGONG = new StringObservable("");//��ע
//	public StringObservable METERNUMBER = new StringObservable("");//���
	//public StringObservable METERTYPE = new StringObservable("");//�����ͺ�
//	public StringObservable LEFTRIGHTWATCH = new StringObservable("");//���ұ�
//	public StringObservable lastrecord = new StringObservable("");//�����
//	public StringObservable metergasnums = new StringObservable("");//�ۼƹ�����
//	public StringObservable gasmeteraccomodations = new StringObservable("");//�����
	public StringObservable f_downloadstatus = new StringObservable("");//�����
	public StringObservable smwxjl = new StringObservable("");//ά�޽��
	public StringObservable JIEGUO = new StringObservable("");//ά�޽��
	//public ArrayListObservable<String> gas_meter_brand = new ArrayListObservable<String>(String.class);//����Ʒ��
//	public ArrayListObservable<String> rqbiaoxing = new ArrayListObservable<String>(String.class, new String[]{"���", "�ұ�"});//���ұ�
	public ArrayListObservable<String> completion = new ArrayListObservable<String>(String.class, new String[]{"�����","δ���"});//������
//	public StringObservable reading = new StringObservable("");//��ǰ����
	//public StringObservable surplus = new StringObservable("");//ʣ������
	public ArrayListObservable<String> inshi = new ArrayListObservable<String>(String.class, new String[]{"��", "��"});//������
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
				Toast.makeText(sa, "�����ʽ����ȷ", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
}
