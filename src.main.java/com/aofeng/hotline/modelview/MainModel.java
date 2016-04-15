package com.aofeng.hotline.modelview;
import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.IntegerObservable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aofeng.hotline.R;
import com.aofeng.hotline.activity.LoginActivity;
import com.aofeng.hotline.activity.MainActivity;
import com.aofeng.hotline.activity.MechanicsChooserActivity;
import com.aofeng.hotline.db.DBAdapter;
import com.aofeng.hotline.model.RepairSlipRowModel;
import com.aofeng.hotline.service.AlarmService;
import com.aofeng.utils.Util;
import com.aofeng.utils.Vault;

public class MainModel {
	public MainActivity mContext;
	private int idx;

	// ά���б�
	public ArrayListObservable<RepairSlipRowModel>lstRepairs = new ArrayListObservable<RepairSlipRowModel>(
			RepairSlipRowModel.class);
//	// �����б�
//	public ArrayListObservable<RepairSlipRowModel>lstEmergencies = new ArrayListObservable<RepairSlipRowModel>(
//			RepairSlipRowModel.class);
	
	public MainModel(MainActivity mContext) {
		this.mContext = mContext;
	}
//	//ά��
//	public IntegerObservable allImgId = new IntegerObservable(R.drawable.all_btn_hover);
//	public Command AllClicked = new Command(){
//		public void Invoke(View view, Object... args) {
//			HilightChosenImg(R.drawable.all_btn_hover);
//			mContext.findViewById(R.id.paneEmergency).setVisibility(LinearLayout.GONE);
//			mContext.findViewById(R.id.paneRepair).setVisibility(LinearLayout.VISIBLE);
//		}
//	};
//	//����
//	public IntegerObservable yiImgId = new IntegerObservable(R.drawable.yijian_btn);
//	public Command YiImgClicked = new Command(){
//		public void Invoke(View view, Object... args) {
//			HilightChosenImg(R.drawable.yijian_btn_hover);
//			mContext.findViewById(R.id.paneRepair).setVisibility(LinearLayout.GONE);
//			mContext.findViewById(R.id.paneEmergency).setVisibility(LinearLayout.VISIBLE);
//		}
//	};
//	//�ϴ�
//	public IntegerObservable imageViewWei = new IntegerObservable(R.drawable.all_btn_hover);
//	public Command imgUploadClicked = new Command(){
//		public void Invoke(View view, Object... args) {
//			HilightChosenImg(R.drawable.all_btn_hover);
//			
//		}
//	};
//	//����
//	public IntegerObservable imageViewJu = new IntegerObservable(R.drawable.yijian_btn);
//	public Command imgDoneClicked = new Command(){
//		public void Invoke(View view, Object... args) {
//			HilightChosenImg(R.drawable.all_btn_hover);
//			mContext.findViewById(R.id.paneRepair).setVisibility(LinearLayout.GONE);
//			mContext.findViewById(R.id.paneEmergency).setVisibility(LinearLayout.VISIBLE);
//		}
//	};
	/**
	 * ������ǰѡ��
	 * @param imgId
	 */
//	private void HilightChosenImg(int imgId) {
//
//		allImgId.set(R.drawable.all_btn);
//		yiImgId.set(R.drawable.yijian_btn);
//		imageViewWei.set(R.drawable.all_btn);//ȱͼƬ
//		imageViewJu.set(R.drawable.yijian_btn);//ȱͼƬ
//		if(imgId == R.drawable.all_btn_hover)//����
//		{
//			allImgId.set(imgId);
//			idx = 0;
//		}
//		else if(imgId == R.drawable.yijian_btn_hover)//ά��
//		{
//			yiImgId.set(imgId);
//			idx = 1;
//		}
//		else if(imgId == R.drawable.yijian_btn_hover)//�ϴ�
//		{
//			yiImgId.set(imgId);
//			idx = 2;
//		}		
//		else if(imgId == R.drawable.yijian_btn_hover)//����
//		{
//			yiImgId.set(imgId);
//			idx = 3;
//		}		
//		
//	}
	
		
//	//����
//	public Command onMute = new Command(){
//		public void Invoke(View view, Object... args) {
//			mContext.sendMessageToService(AlarmService.MSG_FROM_ACTIVITY_MUTE);
//			setMute();
//		}

//		private void setMute() {
//			SQLiteDatabase db = null;
//			try
//			{
//				db = mContext.openOrCreateDatabase(Util.getDBName(mContext), Context.MODE_PRIVATE, null);
//				if(idx==0){
//					db.execSQL("update T_BX_REPAIR_ALL set MUTE='1' where MUTE='0' and f_repairtype='���޵�");
//					FillEmergencyModel();
//				}else if(idx==1){
//					db.execSQL("update T_BX_REPAIR_ALL set MUTE='1' where MUTE='0' and f_repairtype='ά�޵�");
//					FillRepairModel();
//				}
//			}
//			catch(Exception e)
//			{
//				e.printStackTrace();
//				Toast.makeText(mContext, "����ά�޵�״̬����", Toast.LENGTH_SHORT).show();
//				return;
//			}
//			finally
//			{
//				if(db != null)
//					db.close();
//			}
//		}
//	};

	//�˳�
	public Command onExit = new Command(){
		public void Invoke(View view, Object... args) {
			mContext.service.stop();
			Intent intent = new Intent(mContext, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mContext.startActivity(intent);
		}
	};
	

	public void fillItem() {
		//if the server is still waiting to be bound
		try
		{
			DBAdapter.getInstance();
		}
		catch(Exception e)
		{
//			FillRepairModel();
			FillEmergencyModel();
			return;
		}
		int mid = GetMaxID("T_BX_REPAIR_ALL");
		if(mid == -1)
		{
			Toast.makeText(mContext, "��ȡ���ӳ���", Toast.LENGTH_LONG).show();
			return;
		}
		if(!FillRepairSlips(mid,"T_BX_REPAIR_ALL"))
		{
			Toast.makeText(mContext, "���ص��ӳ���", Toast.LENGTH_LONG).show();
			return;
		}
	}

	/**
	 * ���ص���
	 * @param mid
	 */
	private boolean FillRepairSlips(int mid, String tableName) {
		boolean result;
		try
		{
			List<Map<String, String>> list = GetNewRepairs(mid, tableName);
			if(list.size()>0)
				FillNewRepairs(list, tableName);
			list.clear(); list = GetupdateRepairs(tableName);
			if(list.size()>0)
				updateRepairs(list, tableName);
//			FillRepairModel();//ά�޵�
			FillEmergencyModel();//���޵�
			result=true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result=false;
			
		}
		return result;
	}
	private void FillEmergencyModel() {
		lstRepairs.clear();
		SQLiteDatabase db = null;
		try
		{
			db = mContext.openOrCreateDatabase(Util.getDBName(mContext), Context.MODE_PRIVATE, null);
			String sql = "select * from T_BX_REPAIR_ALL where  f_havacomplete not in ('�����')";
			//and f_jiedanren2=(select NAME  from t_user where ENAME= '" + Util.getSharedPreference(mContext, Vault.CHECKER_NAME) + "') "
			Cursor c = db.rawQuery(sql, null);
			int i=c.getCount();
			while(c.moveToNext())
			{
				String s2=c.getString(c.getColumnIndex("f_havacomplete"));
				RepairSlipRowModel model = new RepairSlipRowModel(this);
				model.ID = c.getString(c.getColumnIndex("ID"));
				model.setMute(c.getString(c.getColumnIndex("MUTE")));
				//model.USERID=c.getString(c.getColumnIndex("f_userid"));//�û�ID
				model.USERID.set(c.getString(c.getColumnIndex("f_userid")));//�û�����
				model.USERNAME.set(c.getString(c.getColumnIndex("f_username")));//�û�����
				model.USERADDRESS.set(c.getString(c.getColumnIndex("f_address")));//�û���ַ
			    model.GASUSERTYPE.set(c.getString(c.getColumnIndex("f_quyu")));//�û����
			    model.CARDID.set(c.getString(c.getColumnIndex("f_cardid")));//�û����
				model.LINKTYPE.set(c.getString(c.getColumnIndex("f_linktype")));//�û��绰
				model.SENDER.set(c.getString(c.getColumnIndex("f_sender")));//�ɵ���
				model.JIBIE.set(c.getString(c.getColumnIndex("f_jibie")));//��������
				model.SENDTIME.set(c.getString(c.getColumnIndex("f_senddate"))+" "+c.getString(c.getColumnIndex("f_sendtime")));//�ɵ�ʱ��
				model.CUCODE.set(c.getString(c.getColumnIndex("f_cucode")));//���ޱ��
				model.LAIYUAN.set(c.getString(c.getColumnIndex("f_laiyuan")));//���ޱ��
				model.REPAIRTYPE.set(c.getString(c.getColumnIndex("f_repairtype")));//��������
				model.PHONE.set(c.getString(c.getColumnIndex("f_phone")));//�������
				model.REPAIRREASON.set(c.getString(c.getColumnIndex("f_repairreason")));//��������
				model.STOPREMARK.set(c.getString(c.getColumnIndex("f_dealonline")));//��ע
				model.METERNUMBER.set(c.getString(c.getColumnIndex("f_meternumber")));//��ע
				model.JIEDANDATE.set(c.getString(c.getColumnIndex("f_jiedandate")));//��ע
				model.JIEDANTIME.set(c.getString(c.getColumnIndex("f_jiedantime")));//��ע
				model.FUZEREN.set(c.getString(c.getColumnIndex("f_fuzeren")));//��ע
				model.WANGONGRIQI.set(c.getString(c.getColumnIndex("f_wangongdate")));//��ע
				model.WANGGONG.set(c.getString(c.getColumnIndex("f_wangong")));//��ע
		//		model.METERNUMBER.set(c.getString(c.getColumnIndex("f_meternumber")));		//���
			//	model.METERTYPE.set(c.getString(c.getColumnIndex("f_metertype")));	//�����ͺ�
			//	model.LEFTRIGHTWATCH.set(c.getString(c.getColumnIndex("f_aroundmeter")));//���ұ�
			//	model.lastrecord=c.getString(c.getColumnIndex("f_lastrecord"));	//�����
			//	model.metergasnums.set(c.getString(c.getColumnIndex("f_metergasnums")));	//�ۼƹ�����
			//	model.gasmeteraccomodations.set(c.getString(c.getColumnIndex("f_gasmeteraccomodations")));	//�����
				model.smwxjl.set(c.getString(c.getColumnIndex("f_qingkuang")));	//ά�޼�¼
				model.JIEGUO.set(c.getString(c.getColumnIndex("f_jieguo")));	//ά�޼�¼
			//	model.gaswatchbrand=c.getString(c.getColumnIndex("f_gaswatchbrand"));	//����Ʒ��
			//	model.surplus=c.getString(c.getColumnIndex("surplus"));	//������
				model.f_havacomplete=c.getString(c.getColumnIndex("f_havacomplete"));	//���״̬
				model.f_downloadstatus.set(c.getString(c.getColumnIndex("f_downloadstatus")));	//����״̬
				model.f_shixian.set(c.getString(c.getColumnIndex("f_shixian")));	//����״̬
				if("����".equals(model.f_downloadstatus.get()))
					model.gongdanstatus.set(true);
				else
					model.gongdanstatus.set(false);
				lstRepairs.add(model);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			if(db != null)
				db.close();
		}
	}
//	private void FillRepairModel() {
//		lstRepairs.clear();
//		SQLiteDatabase db = null;
//		try
//		{
//			db = mContext.openOrCreateDatabase(Util.getDBName(mContext), Context.MODE_PRIVATE, null);//hotline_1.db
//			String sql = "select * from T_BX_REPAIR_ALL where (CAST(MUTE as INTEGER) &24)=0 and f_repairtype='ά�޵�'";
//			Cursor c = db.rawQuery(sql, null);
//			while(c.moveToNext())
//			{
//				RepairSlipRowModel model = new RepairSlipRowModel(this);
//				model.ID = c.getString(c.getColumnIndex("ID"));
//				model.setMute(c.getString(c.getColumnIndex("MUTE")));
//				model.USERID=c.getString(c.getColumnIndex("f_userid"));//�û�ID
//				model.USERNAME.set(c.getString(c.getColumnIndex("f_username")));//�û�����
//				model.USERADDRESS.set(c.getString(c.getColumnIndex("f_useraddress")));//�û���ַ
//				model.GASUSERTYPE.set(c.getString(c.getColumnIndex("f_gasusertype")));//�û����
//				model.LINKTYPE.set(c.getString(c.getColumnIndex("f_linktype")));//�û��绰
//				model.SENDER.set(c.getString(c.getColumnIndex("f_sender")));//�ɵ���
//				model.SENDTIME.set(c.getString(c.getColumnIndex("f_senddate"))+" "+c.getString(c.getColumnIndex("f_sendtime")));//�ɵ�ʱ��
//				model.CUCODE.set(c.getString(c.getColumnIndex("f_cucode")));//���ޱ��
//				model.REPAIRTYPE.set(c.getString(c.getColumnIndex("f_repairtype")));//��������
//				model.PHONE.set(c.getString(c.getColumnIndex("f_phone")));//�������
//				model.REPAIRREASON.set(c.getString(c.getColumnIndex("f_repairreason")));//��������
//				model.STOPREMARK.set(c.getString(c.getColumnIndex("f_stopremark")));//��ע
//				
//				model.METERNUMBER.set(c.getString(c.getColumnIndex("f_meternumber")));		//���
//				model.METERTYPE.set(c.getString(c.getColumnIndex("f_metertype")));	//�����ͺ�
//				model.LEFTRIGHTWATCH.set(c.getString(c.getColumnIndex("f_aroundmeter")));//���ұ�
//				model.lastrecord.set(c.getString(c.getColumnIndex("f_lastrecord")));	//�����
//				model.metergasnums.set(c.getString(c.getColumnIndex("f_metergasnums")));	//�ۼƹ�����
//				model.gasmeteraccomodations.set(c.getString(c.getColumnIndex("f_gasmeteraccomodations")));	//�����
//				lstRepairs.add(model);
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		finally
//		{
//			if(db != null)
//				db.close();
//		}
//	}


	/**
	 * 
	 * @param list
	 * @param tableName
	 */
	private void FillNewRepairs(List<Map<String, String>> list, String tableName) {
		SQLiteDatabase db = null;
		try
		{
			db = mContext.openOrCreateDatabase(Util.getDBName(mContext), Context.MODE_PRIVATE, null);//hotline_1.db
			for(Map<String, String> map : list)
			{
				String sql = "INSERT INTO " + tableName  + "(MUTE";
				String val = "'0'";
				for(String key : map.keySet())
				{
					if(key.toUpperCase().equals("ID") || key.toUpperCase().equals("MUTE"))
						continue;
					sql += "," + key;
					String colVal = map.get(key);
					if(colVal == null || colVal.equals("null"))
						val += ",NULL";
					else
						val += ",'" + colVal + "'";
				}
				sql += ") VALUES(" + val + ")";
				db.execSQL(sql);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//we cant handle the exception here, so give it to the big banana
		finally
		{
			if(db != null)
				db.close();
		}
		
	}


/**
 * Extract newly arrived slips from the service side database, 
 * should do read only operation to avoid contention.
 * @param mid
 * @return
 */
private List<Map<String, String>> GetNewRepairs(int mid, String tableName) {
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	SQLiteDatabase db = DBAdapter.getInstance().openDatabase();//hotline_1_1.db
	try
	{
			String sql = "select * from " + tableName + " where ID>" + mid;
			Cursor c = db.rawQuery(sql, null);
			String[] names = c.getColumnNames();
			while(c.moveToNext())
			{
				Map<String,String> map = new HashMap<String,String>();
				for(int i=0; i<c.getColumnCount(); i++)
					map.put(names[i], c.getString(i));
				list.add(map);
			}
	}
	catch(Exception e){
		e.printStackTrace();
	}
	finally
	{
		if(db != null)
			DBAdapter.getInstance().closeDatabase();
	}
	return list;
}


/**
 * �õ�����¼ ��
 * @param string
 * @return
 */
	private int GetMaxID(String tableName) {
		SQLiteDatabase db = null;
		try
		{
				db = mContext.openOrCreateDatabase(Util.getDBName(mContext), Context.MODE_PRIVATE, null);//hotline_1.db
				String sql = "select max(ID) from  " + tableName;
				Cursor c = db.rawQuery(sql, null);
				if(c.moveToNext())
				{
					return  c.getInt(0);
				}
				else
					return 0;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		finally
		{
			if(db != null)
				db.close();
		}
	}
	
	
	/**  
	 * ���¹���״̬
	 * @param list
	 * @param tableName
	 */
	private void updateRepairs(List<Map<String, String>> list, String tableName) {
		SQLiteDatabase db = null;
		try
		{
			db = mContext.openOrCreateDatabase(Util.getDBName(mContext), Context.MODE_PRIVATE, null);//hotline_1.db
			for(Map<String, String> map : list)
			{
				String sql = "select ID from " + tableName + " where f_downloadstatus ='����' and ID='"+map.get("ID")+"' and f_cucode='"+map.get("f_cucode")+"'";
				Cursor c = db.rawQuery(sql, null);
				while(c.moveToNext())
				{
					sql = "update " + tableName  + " set f_downloadstatus='"+map.get("f_downloadstatus")+"' where ID='"+map.get("ID")+"' and f_cucode='"+map.get("f_cucode")+"'";
					db.execSQL(sql);
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//we cant handle the exception here, so give it to the big banana
		finally
		{
			if(db != null)
				db.close();
		}
		
	}
	
	/**
	 * Extract newly arrived slips from the service side database, 
	 * should do read only operation to avoid contention.
	 * @param mid
	 * @return
	 */
	private List<Map<String, String>> GetupdateRepairs(String tableName) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SQLiteDatabase db = DBAdapter.getInstance().openDatabase();//hotline_1_1.db
		try
		{
				String sql = "select ID,f_cucode,f_downloadstatus from " + tableName + " where f_downloadstatus='�����ѳ���'";
				Cursor c = db.rawQuery(sql, null);
				String[] names = c.getColumnNames();
				while(c.moveToNext())
				{
					Map<String,String> map = new HashMap<String,String>();
					for(int i=0; i<c.getColumnCount(); i++)
						map.put(names[i], c.getString(i));
					list.add(map);
				}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			if(db != null)
				DBAdapter.getInstance().closeDatabase();
		}
		return list;
	}

}
