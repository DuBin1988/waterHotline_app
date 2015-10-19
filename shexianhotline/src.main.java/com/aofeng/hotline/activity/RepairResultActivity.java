package com.aofeng.hotline.activity;

import gueei.binding.Command;
import gueei.binding.app.BindingActivity;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.StringObservable;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aofeng.hotline.R;
import com.aofeng.hotline.model.ResultRowModel;
import com.aofeng.utils.Util;
import com.aofeng.utils.Vault;

public class RepairResultActivity extends BindingActivity{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setAndBindRootView(R.layout.repair_result, this);
		Bundle bundle = getIntent().getExtras();
		ID= bundle.getString("ID");
		USERID.set(bundle.getString("USERID"));
		CUCODE.set(bundle.getString("CUCODE"));
		METERNUMBER.set(bundle.getString("METERNUMBER"));
		if(savedInstanceState != null)
		{
			USERID.set(savedInstanceState.getString("USERID"));
			METERNUMBER.set(savedInstanceState.getString("METERNUMBER"));
		}
	}
	
	public String ID;
	public boolean inspected;
	//用户ID
	public StringObservable USERID = new StringObservable(); 
	//返单的表ID
	public StringObservable METERNUMBER = new StringObservable(); 
	//维修码
	public StringObservable CUCODE = new StringObservable(); 
	//返单号
	// 维修结果列表
	public ArrayListObservable<ResultRowModel> ResultList = new ArrayListObservable<ResultRowModel>(
			ResultRowModel.class);

	/**
	 * 添加维修记录
	 */
	public Command Add = new Command()
	{
		@Override
		public void Invoke(View arg0, Object... arg1) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("ID", ID);
			bundle.putString("USERID", USERID.get());
			bundle.putString("METERNUMBER", METERNUMBER.get());
			bundle.putString("CUCODE", CUCODE.get());
			intent.setClass(RepairResultActivity.this, RepairRecordActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);		
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		listRecords();
	}

	private void listRecords() {
		this.ResultList.clear();
		SQLiteDatabase db = null;
		try
		{
				db = openOrCreateDatabase(Util.getDBName(this), Context.MODE_PRIVATE, null);
				String sql = "select f_userid,f_watchnum from T_BX_REPAIR_ALL where ID =?";
				Cursor c = db.rawQuery(sql,new String[]{ID});
				if(c.moveToNext())
				{
					USERID.set(c.getString(0));
					METERNUMBER.set(c.getString(1));
				}
				c.close();
//				sql = "select ID from t_inspection where ID='" + CUCODE.get() + "_" + resultCode.get() + "_" + Util.getSharedPreference(RepairResultActivity.this, Vault.USER_NAME) + "'";
				sql = "select ID from t_inspection where ID='" + CUCODE.get() + "_" + Util.getSharedPreference(RepairResultActivity.this, Vault.USER_NAME) + "'";
				c = db.rawQuery(sql, null);
				if(c.moveToNext())
					inspected = true;
				else
					inspected = false;
				sql ="select * from T_BX_REPAIR_RESULT where cucode=? order by finishtime desc";
				c = db.rawQuery(sql, new String[]{this.CUCODE.get()});
				while(c.moveToNext())
				{
					ResultRowModel row = new ResultRowModel(this);
					row.resuleid.set(c.getString(c.getColumnIndex("resuleid")));
					row.CUCODE.set(c.getString(c.getColumnIndex("cucode")));
					row.usertype.set(c.getString(c.getColumnIndex("usertype")));
					row.slipDep.set(c.getString(c.getColumnIndex("slipDep")));
					row.slipDetadep.set(c.getString(c.getColumnIndex("slipDetadep")));
					row.slipSpot.set(c.getString(c.getColumnIndex("slipSpot")));
					row.repairResult.set(c.getString(c.getColumnIndex("repairResult")));
					row.isMoney.set(c.getString(c.getColumnIndex("isMoney")));
					row.newIcType.set(c.getString(c.getColumnIndex("newIcType")));
					row.slipReason.set(c.getString(c.getColumnIndex("slipReason")));
					row.money.set(c.getString(c.getColumnIndex("money")));
					row.moveGas.set(c.getString(c.getColumnIndex("moveGas")));
					row.mark.set(c.getString(c.getColumnIndex("mark")));
					row.biaonum.set(c.getString(c.getColumnIndex("biaonum")));
					row.repairDate.set(c.getString(c.getColumnIndex("repairDate")));
					ResultList.add(row);
				}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(db != null)
				db.close();
		}		
	}
	
	public void deleteARow(ResultRowModel row)
	{
		for(int i=0; i<ResultList.size(); i++)
		{
			if(ResultList.get(i).resuleid.get().equals(row.resuleid.get()))
			{
				ResultList.remove(i);
				break;
			}
		}
	}

	public Command onInspect = new Command()
	{
		@Override
		public void Invoke(View arg0, Object... arg1) {
			if(ResultList.size()==0)
			{
				Toast.makeText(RepairResultActivity.this, "请先添加维修记录。", Toast.LENGTH_SHORT).show();
				return;
			}
//			if(cardId.get().length()!=10)
//			{
//				Toast.makeText(RepairResultActivity.this, "卡号必须为10位。", Toast.LENGTH_SHORT).show();
//				return;
//			}
			updateRepairState(Vault.ITEM_REPAIRED);
			//删除缓存
			Util.ClearCache(RepairResultActivity.this, 
					CUCODE.get() + "_" + Util.getSharedPreference(RepairResultActivity.this, Vault.USER_NAME));
			Intent intent = new Intent();
			// 利用包袱传递参数给Activity
			Bundle bundle = new Bundle();
			bundle.putString("ID", CUCODE.get());
			bundle.putString("slipId", ID);
			bundle.putString("CUCODE",  CUCODE.get());
			bundle.putBoolean("INSPECTED", inspected);
//			bundle.putString("CardID", cardId.get());
			intent.setClass(RepairResultActivity.this, IndoorInspectActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);			
		}
		
	};

	protected void updateRepairState(int ITEM_REPAIRED) {
		SQLiteDatabase db = null;
		try
		{
				String sql = "update T_BX_FIRSTAID_PAPER ";
				if(CUCODE.get().startsWith("B"))
					sql = "update T_BX_REPAIR_ALL";
				sql += "  set MUTE= CAST((CAST(MUTE as INTEGER) |"  +  ITEM_REPAIRED + ")  as text), cardId2=?, biaoCode2=? where ID= " + ID;
				db = openOrCreateDatabase(Util.getDBName(this), Context.MODE_PRIVATE, null);
				db.execSQL(sql, new String[]{USERID.get(), METERNUMBER.get()});
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(db != null)
				db.close();
		}		
	}
}

