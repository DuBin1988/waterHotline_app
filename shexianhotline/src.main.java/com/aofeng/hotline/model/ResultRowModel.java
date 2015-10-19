package com.aofeng.hotline.model;

import com.aofeng.hotline.activity.RepairResultActivity;
import com.aofeng.utils.Util;

import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.StringObservable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ResultRowModel  {
	protected Activity mContext;
	public ResultRowModel(Activity context) {
		this.mContext = context;
	}

	//给的文档中的所有字段
	//resuleid=108848  resultCode=108847 slipDep=表故障 slipSpot=户内 usertype=民用 repairCode=B2010120800372 secDep=103 spaceDep=107 secDepname=城南中心站 spaceDepname=客服  

	//resuleid   本维修记录的id
	//下面五个数据从发来的维修单得到 猜想
	//resultCode  从返单得到的id
	//secDep=103 spaceDep=107 secDepname=城南中心站 spaceDepname=客服
	
	public StringObservable resuleid = new StringObservable(""); 
	public StringObservable resultCode = new StringObservable(""); 
	public StringObservable secDep = new StringObservable(""); 
	public StringObservable spaceDep = new StringObservable(""); 
	public StringObservable secDepname = new StringObservable(""); 
	public StringObservable spaceDepname = new StringObservable(""); 
	public String ID;
	//维修代码
	public StringObservable CUCODE = new StringObservable(""); 
	//用户性质
	public StringObservable usertype = new StringObservable(""); 
	//故障位置
	public StringObservable slipSpot = new StringObservable(""); 
	//故障设备
	public StringObservable slipDep = new StringObservable(""); 
	//详细故障设备 猜的
	public StringObservable slipDetadep = new StringObservable(""); 
	//故障原因 猜的
	public StringObservable slipReason = new StringObservable(""); 
	//维修结果 猜的
	public StringObservable repairResult = new StringObservable(""); 
	//更换表型 猜的
	public StringObservable newIcType = new StringObservable(""); 
	//转移气量 猜的
	public StringObservable moveGas = new StringObservable(""); 
	//是否收费 猜的
	public StringObservable isMoney = new StringObservable(""); 
	//费用 猜的
	public StringObservable money = new StringObservable(""); 
	//表底数 猜的
	public StringObservable biaonum = new StringObservable(""); 
	//备注 猜的
	public StringObservable mark = new StringObservable(""); 
	//维修时间 猜的
	public StringObservable repairDate = new StringObservable(""); 
	
	public ArrayListObservable<String>lstUsertype = new ArrayListObservable<String>(String.class);
	public ArrayListObservable<String>lstSlipSpot = new ArrayListObservable<String>(String.class);
	public ArrayListObservable<String>lstSlipDep = new ArrayListObservable<String>(String.class);
	public ArrayListObservable<String>lstSlipDetadep = new ArrayListObservable<String>(String.class);
	public ArrayListObservable<String>lstSlipReason = new ArrayListObservable<String>(String.class);
	public ArrayListObservable<String>lstRepairResult = new ArrayListObservable<String>(String.class);
	public ArrayListObservable<String>lstIcType = new ArrayListObservable<String>(String.class);
	public ArrayListObservable<String>lstIsMoney = new ArrayListObservable<String>(String.class);

	public static String[] aUserType = {"民用","工商","餐饮"};
	public static String[] aSlipSpot = {"户外","户内"};
	public static String[][] aSlipDep = {
		{"整楼压力高","整楼压力低","整楼无气","锅炉房压力高","锅炉房压力低","锅炉房无气","主体管道","庭院管道","庭院阀井","调压器","其它"},
		{"灶具","软管","管道","阀门故障","表故障","其它"},
		{"锅炉房压力高","锅炉房压力低","锅炉房无气","主体管道","庭院管道","庭院阀井","调压器","其它"},
		{"灶具","软管","管道","阀门故障","表故障","其它"},
		{"餐饮压力高","餐饮压力低","餐饮无气","主体管道","调压器","其它"},
		{"灶具","管道","阀门故障","表故障","其它"}
	}; 
	
	public static String[] pipes = {"阀前管", "阀后管"};
	public static String[] valves = {"自闭阀", "铜阀", "表前阀"};
	public static String[] pipeCauses = {"无气", "漏气"};
	public static String[] valveCauses ={"漏气", "已坏"};
	
	public static String[] aResults = {"其它","换管道", "补气", "换表", "换表前阀", "换软管", "换调压器", "换自闭阀", "已修好", "误报", "换铜阀"};
	public static String[] aCards = {"华捷加密(10)","华捷加密(12)", "华捷普通(10)", "华捷普通(12)", "秦港", "秦川", "天庆", "致力老表", "致力新表", "秦港工业", "赛福", "天然气无线", "工业无线"};
	
	public static String[] bools = {"是", "否"};
	
	public void fillUserType()
	{
		lstUsertype.setArray(aUserType);
	}
	
	public void fillCharge()
	{
		lstIsMoney.setArray(bools);
	}
	
	public void fillSlipSpot()
	{
		lstSlipSpot.setArray(aSlipSpot);
	}
	
	public void fillResult()
	{
		lstRepairResult.setArray(ResultRowModel.aResults);
	}
	
	public void fillCards()
	{
		this.lstIcType.setArray(ResultRowModel.aCards);
	}
	
	public void fillSlipDep(String userType, String indoorOutdoor)
	{
		int idx1 = find(ResultRowModel.aUserType, userType);
		int idx2 = 0;
		if(indoorOutdoor.equals("户内"))
			idx2 = 1;
		lstSlipDep.clear();
		lstSlipDep.setArray(ResultRowModel.aSlipDep[idx1*2 + idx2]);
	}

	private int find(String[] array, String item) {
		for(int i=0; i<array.length; i++)
			if(array[i].equals(item))
				return i;
		return 0;
	}
	
	public void fillSlipDetadep(String userType, String dev)
	{
		if(userType.equals("餐饮"))
		{
			this.lstSlipDetadep.clear();
			this.lstSlipReason.clear();
			return;
		}
		if(dev.equals("管道"))
		{
			this.lstSlipDetadep.setArray(ResultRowModel.pipes);
			this.lstSlipReason.setArray(ResultRowModel.pipeCauses);
		}
		else if(dev.equals("阀门故障"))
		{
			if(userType.equals("工商"))
			{
				this.lstSlipDetadep.setArray(ResultRowModel.pipes);
				this.lstSlipReason.setArray(ResultRowModel.pipeCauses);
			}
			else
			{
				this.lstSlipDetadep.setArray(ResultRowModel.valves);
				this.lstSlipReason.setArray(ResultRowModel.valveCauses);
			}
		}
		else if(dev.equals("灶具") ||dev.equals("软管"))
		{
			this.lstSlipDetadep.clear();
			this.lstSlipReason.setArray(ResultRowModel.pipeCauses);
		}
		else
		{
			this.lstSlipDetadep.clear();
			this.lstSlipReason.clear();
		}
	}	
	
	public Command onDelete = new Command()
	{
		@Override
		public void Invoke(View arg0, Object... arg1) {
			TextView tv = new TextView(mContext);
			tv.setText("确认要删除吗？");
			Dialog alertDialog = new AlertDialog.Builder(mContext).   
					setView(tv).
					setTitle("确认").   
					setIcon(android.R.drawable.ic_dialog_info).
					setPositiveButton("确定", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							SQLiteDatabase db = null;
							try
							{
								db = mContext.openOrCreateDatabase(Util.getDBName(mContext), Context.MODE_PRIVATE, null);
								String sql ="delete from T_BX_REPAIR_RESULT where resuleid=?";
								db.execSQL(sql, new Object[]{resuleid.get()});
								((RepairResultActivity)mContext).deleteARow(ResultRowModel.this);
								Toast.makeText(mContext, "删除成功！", Toast.LENGTH_SHORT).show();
							}
							catch(Exception e)
							{
								e.printStackTrace();
								Toast.makeText(mContext, "删除失败！", Toast.LENGTH_SHORT).show();
							}
							finally
							{
								if(db != null)
									db.close();
							}		
						}
					}).setNegativeButton("取消", null).
					create();  
			alertDialog.setCancelable(false);
			alertDialog.show();
		}
		
	};
}
