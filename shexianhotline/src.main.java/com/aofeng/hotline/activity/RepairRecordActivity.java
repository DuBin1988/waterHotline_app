package com.aofeng.hotline.activity;

import java.util.Date;

import com.aofeng.hotline.R;
import com.aofeng.hotline.model.RepairRecordModel;
import com.aofeng.utils.Util;
import com.aofeng.utils.Vault;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import gueei.binding.app.BindingActivity;
import gueei.binding.collections.ArrayListObservable;

public class RepairRecordActivity extends BindingActivity{
	RepairRecordModel model;
	public void onCreate(Bundle bundle2){
		super.onCreate(bundle2);
		model = new RepairRecordModel(this);
		model.fillUserType();
		model.fillResult();
		model.fillCharge();
		model.fillSlipSpot();
		this.setAndBindRootView(R.layout.repair_item_view, model);
		if(bundle2 == null)
		{
			Bundle bundle = this.getIntent().getExtras();
			model.CUCODE.set(bundle.getString("CUCODE"));
			hookEvents();
//			trigger event
			model.fillSlipSpot();
			model.resuleid.set("" + Util.createSN(Util.getSharedPreference(this, Vault.USER_NAME)));
		}
		else
		{
			hookEvents();
			setSpinner(bundle2.getString("lstUsertype"), model.lstUsertype, R.id.lstUsertype);
			setSpinner(bundle2.getString("lstSlipSpot"), model.lstSlipSpot, R.id.lstSlipSpot);
			setSpinner(bundle2.getString("lstSlipDep"), model.lstSlipDep, R.id.lstSlipDep);
			setSpinner(bundle2.getString("lstSlipDetadep"), model.lstSlipDetadep, R.id.lstSlipDetadep);
			setSpinner(bundle2.getString("lstSlipReason"), model.lstSlipReason, R.id.lstSlipReason);
			setSpinner(bundle2.getString("lstRepairResult"), model.lstRepairResult, R.id.lstRepairResult);
			setSpinner(bundle2.getString("lstIcType"), model.lstIcType, R.id.lstIcType);
			setSpinner(bundle2.getString("lstIsMoney"), model.lstIsMoney, R.id.lstIsMoney);
		}
	}
	
	private void setSpinner(String text,	ArrayListObservable<String> lst, int rid) {
		if(text.length()==0)
			return;
		for(int i=0; i< lst.size(); i++)
		{
			if(text.equals(lst.get(i)))
				((Spinner)findViewById(rid)).setSelection(i);
		}
	}

	private void hookEvents() {
		OnItemSelectedListener listener = new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,	int position, long id) {
				int pos1 = ((Spinner)findViewById(R.id.lstUsertype)).getSelectedItemPosition();
				int pos2 = ((Spinner)findViewById(R.id.lstSlipSpot)).getSelectedItemPosition();
				model.fillSlipDep(model.lstUsertype.get(pos1), model.lstSlipSpot.get(pos2));
			}
			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
			}
		};
		Spinner spinner = (Spinner)this.findViewById(R.id.lstUsertype);
		spinner.setOnItemSelectedListener(listener);
		spinner = (Spinner)this.findViewById(R.id.lstSlipSpot);
		spinner.setOnItemSelectedListener(listener);
		//选择设备，填充子设备
		spinner = (Spinner)this.findViewById(R.id.lstSlipDep);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
				int pos = ((Spinner)findViewById(R.id.lstUsertype)).getSelectedItemPosition();
				model.fillSlipDetadep(model.lstUsertype.get(pos), model.lstSlipDep.get(position));
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		//选择子设备，填充
		spinner = (Spinner)this.findViewById(R.id.lstSlipDep);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
				int pos = ((Spinner)findViewById(R.id.lstUsertype)).getSelectedItemPosition();
				model.fillSlipDetadep(model.lstUsertype.get(pos), model.lstSlipDep.get(position));
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		//处理结果
		spinner = (Spinner)this.findViewById(R.id.lstRepairResult);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
				if(model.lstRepairResult.get(position).equals("换表"))
				{
					model.fillCards();
				}
				else
				{
					model.lstIcType.clear();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		//是否收费
		spinner = (Spinner)this.findViewById(R.id.lstIsMoney);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
				if(model.lstIsMoney.get(position).equals("是"))
				{
					findViewById(R.id.money).setEnabled(true);
				}
				else
				{
					model.money.set("");
					findViewById(R.id.money).setEnabled(false);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	/**
	 * 保存
	 */
	public void doSave() {
		Bundle bundle = this.getIntent().getExtras();
		SQLiteDatabase db = null;
		try
		{
				db = openOrCreateDatabase(Util.getDBName(this), Context.MODE_PRIVATE, null);
				String sql ="delete from T_BX_REPAIR_RESULT where ID=? and cucode=? and resuleid=?";
				db.execSQL(sql, new Object[]{bundle.get("ID"),bundle.get("CUCODE"),model.resuleid.get()});
				sql = "insert into T_BX_REPAIR_RESULT( " +
						"ID, " +
						"resuleid," +
						"cucode ," +
						"usertype ," +
						"slipSpot ," +
						"slipDep ," +
						"slipDetadep ," +
						"slipReason ," +
						"repairResult ," +
						"newIcType ," +
						"moveGas ," +
						"isMoney ," +
						"money ," +
						"biaonum ," +
						"repairDate  ," +
						"mark) values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?)";
			db.execSQL(sql, new Object[]{
					bundle.get("ID"),
					model.resuleid.get(),
					bundle.get("CUCODE"),
					getSelected(R.id.lstUsertype, model.lstUsertype),
					getSelected(R.id.lstSlipSpot, model.lstSlipSpot),
					getSelected(R.id.lstSlipDep, model.lstSlipDep),
					getSelected(R.id.lstSlipDetadep, model.lstSlipDetadep),
					getSelected(R.id.lstSlipReason, model.lstSlipReason),
					getSelected(R.id.lstRepairResult, model.lstRepairResult),
					getSelected(R.id.lstIcType, model.lstIcType),
					model.moveGas.get(),
					getSelected(R.id.lstIsMoney, model.lstIsMoney),
					model.money.get(),
					model.biaonum.get(),
					Util.FormatDate("yyyy-MM-dd HH:mm:ss", (new Date()).getTime()),
					model.mark.get()
			});			
			Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Toast.makeText(this, "保存失败！", Toast.LENGTH_SHORT).show();
		}
		finally
		{
			if(db != null)
				db.close();
		}		
	}

	private String getSelected(int rid, ArrayListObservable<String> lst) {
		if(lst.size()==0)
			return "";
		int idx =	((Spinner)findViewById(rid)).getSelectedItemPosition();
		return lst.get(idx);
	}

	/**
	 * 校验
	 * @return
	 */
	public boolean validate() {
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putString("resuleid", model.resuleid.get());
		bundle.putString("lstUsertype", 	getSelected(R.id.lstUsertype, model.lstUsertype));
		bundle.putString("lstSlipSpot", 	getSelected(R.id.lstSlipSpot, model.lstSlipSpot));
		bundle.putString("lstSlipDep", 	getSelected(R.id.lstSlipDep, model.lstSlipDep));
		bundle.putString("lstSlipDetadep", 	getSelected(R.id.lstSlipDetadep, model.lstSlipDetadep));
		bundle.putString("lstSlipReason", 	getSelected(R.id.lstSlipReason, model.lstSlipReason));
		bundle.putString("lstRepairResult", 	getSelected(R.id.lstUsertype, model.lstRepairResult));
		bundle.putString("lstIcType", 	getSelected(R.id.lstIcType, model.lstIcType));
		bundle.putString("lstIsMoney", 	getSelected(R.id.lstIsMoney, model.lstIsMoney));
		bundle.putString("biaonum", model.biaonum.get());
		bundle.putString("moveGas", model.moveGas.get());
		bundle.putString("money", model.money.get());
		bundle.putString("mark", model.mark.get());		
	}
}
