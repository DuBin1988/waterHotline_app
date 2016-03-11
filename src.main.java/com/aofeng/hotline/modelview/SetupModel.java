package com.aofeng.hotline.modelview;

import gueei.binding.Command;
import gueei.binding.observables.StringObservable;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.app.ActivityManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.aofeng.hotline.activity.BaseSetupActivity;
import com.aofeng.utils.RemoteReader;
import com.aofeng.utils.RemoteReaderListener;
import com.aofeng.utils.Util;
import com.aofeng.utils.Vault;

public class SetupModel {
	private BaseSetupActivity mContext;

	public SetupModel(BaseSetupActivity context){
		this.mContext = context;
		UseName.set(Util.getSharedPreference(mContext, Vault.USER_NAME));
	}

	//执行参数更新命令
	public Command UpdateParam = new Command(){
		public void Invoke(View view, Object... args) {
			if(mContext.isBusy)
			{
				Toast.makeText(mContext, "请等待上次操作完成。", Toast.LENGTH_SHORT).show();
				return;
			}	
			mContext.isBusy = true;
			if (!(Util.fileExists(mContext.getDatabasePath(Util.getDBName(mContext))))) 
				GetRepairManList(true);
			else
				GetRepairManList(false);
		}
	};
	
	//执行系统初始化命令
	public Command Init = new Command(){

		public void Invoke(View view, Object... args) {
			if(mContext.isBusy)
			{
				Toast.makeText(mContext, "请等待上次操作完成。", Toast.LENGTH_SHORT).show();
				return;
			}	
			mContext.isBusy = true;
			GetRepairManList(true);
			
		}
	};
	
	public void GetRepairManList(final boolean toCreateDB) {
//		RemoteReader reader = new RemoteReader(Vault.DB_URL + "sql/",
//				"select NAME, CODE from T_REPAIR_MECHANICS where STATION_ID =(select id from T_REPAIR_STATION where STATIONNAME='" + Util.getSharedPreference(mContext, Vault.STATION_NAME) + "')");
//		RemoteReader reader = new RemoteReader(Vault.DB_URL + "sql/",//所有维修员
//				"select ID, NAME, ENAME from t_user where charindex((select id from t_role where NAME='维修员'), roles, 1)>0");
//		reader.setRemoteReaderListener(new RemoteReaderListener() {
//			@Override
//			public void onSuccess(List<Map<String, Object>> map) {
//				super.onSuccess(map);
				GetParamList(toCreateDB);
//			}
//			@Override
//			public void onFailure(String errMsg) {
//				super.onFailure(errMsg);
//				Toast.makeText(mContext, "获取系统参数失败，请检查网络连接。", Toast.LENGTH_SHORT).show();
//				mContext.isBusy = false;
//			}
//
//		});
//		reader.start();		
	}
	/**
	 * 得到参数列表
	 */
	private void GetParamList(final boolean toCreateDB) {
		RemoteReader reader = new RemoteReader(Vault.DB_URL + "sql/",
				"select v.id id, p.Name code, v.Name name from T_PARAMETER p, T_PARAMVALUE v where p.ID = v.PROCESSID"
				+ " union select id, Value code, Name name from T_SINGLEVALUE");
		reader.setRemoteReaderListener(new RemoteReaderListener() {

			@Override
			public void onSuccess(List<Map<String, Object>> map) {
				super.onSuccess(map);
				if(toCreateDB)
					CreateDatabase(map);
				else
					UpdateParam(map);
				mContext.onComplete();
				mContext.isBusy = false;
			}

			@Override
			public void onFailure(String errMsg) {
				super.onFailure(errMsg);
				Toast.makeText(mContext, "获取系统参数失败，请检查网络连接。", Toast.LENGTH_SHORT).show();
				mContext.isBusy = false;
			}

		});
		reader.start();		
	}

	private void CreateDatabase(List<Map<String, Object>> map) {
			try {
				//建立数据库
				SQLiteDatabase db = mContext.openOrCreateDatabase(Util.getDBName(mContext), Context.MODE_PRIVATE, null);
				db.execSQL("DROP TABLE IF EXISTS t_version");
				String   sql = "CREATE TABLE t_version (" +
						"id VARCHAR PRIMARY KEY, " +
						"ver integer )";
				db.execSQL(sql);
				sql = "insert into t_version values('1', " + Util.getVersionCode(mContext) + ")";
				db.execSQL(sql);
				createTables(db);
//				//创建计划表
//				db.execSQL("DROP TABLE IF EXISTS t_checkplan");
//				sql = "CREATE TABLE t_checkplan (" +
//						"id VARCHAR PRIMARY KEY, " +
//						"f_date varchar," +
//						"f_name VARCHAR)";
//				db.execSQL(sql);
				//创建安检单
				db.execSQL("DROP TABLE IF EXISTS T_IC_SAFECHECK_PAPAER");
				sql = "CREATE TABLE T_IC_SAFECHECK_PAPAER (" +
						"id VARCHAR(80) PRIMARY KEY, " +
						" CONDITION            varchar(80)                    null," +             //检查情况
						"HasNotified				varchar(80)						null,"+				//到访不遇卡
						" USER_NAME            varchar(80)                    null," +             	 	//用户名称
						" TELPHONE             varchar(60)                   	 null," +          		//电话
						"ARRIVAL_TIME       varchar(80)                    	 null,"+			       //到达时间
						"DEPARTURE_TIME   varchar(80)             	    null,"+		   		   //离开时间
						" ROAD                 varchar(80)                    		null," +                  	//街道
						" UNIT_NAME            varchar(80)                    null," +          			//小区
						" CUS_DOM              varchar(20)                    null," +           			//楼号
						" CUS_DY               varchar(20)                 		   null," +               			//单元
						" CUS_FLOOR            varchar(20)                    null," +        		//楼层
						" CUS_ROOM             varchar(20)                    null," +       		//房号
						" OLD_ADDRESS          varchar(500)                 null," +  			//用户档案地址

					  " ROOM_STRUCTURE       varchar(80)                    null," +   	//房屋结构
					  " WARM                 varchar(80)                    null," +                		 //供暖方式
					  " SAVE_PEOPLE          varchar(20)                    null," +     			//安检员

					  " IC_METER_NAME        varchar(20)                    null," +        //IC卡表厂名称
					  " JB_METER_NAME        varchar(20)                    null," +       //基表厂家名称
					  "METER_TYPE                varchar(80)                      null,"+			//表型
					  "  CARD_ID            varchar(80)                    null," +             	 	//卡号
					  " JB_NUMBER            integer                        null," +          		//基表数
					  " SURPLUS_GAS          integer                        null," +              //剩余气量

					  " RQB_AROUND           varchar(80)                    null," +		//燃气表左右表
					  " RQB_JBCODE           varchar(80)                    null," +			//燃气表基表号
					  "METERMADEYEAR   varchar(80)              null,"	+			//燃气表生产年份
					  " RQB                  varchar(80)                    null," +						//	燃气表

					  " STANDPIPE            varchar(80)                    null," +				//立管
					  " RIGIDITY             varchar(80)                    null," +					//严密性测试
					  " STATIC               varchar(80)                    null," +					//静止压力
					  " STATIC_DATA          varchar(80)                    null," +			//静止压力值
					  " TABLE_TAP            varchar(80)                    null," +				//表前阀
					  " COOK_TAP             varchar(80)                    null," +				//灶前阀
					  " CLOSE_TAP            varchar(80)                    null," +				//自闭阀
					  " INDOOR               varchar(80)                    null," +				//户内管
					  "LEAKAGE_COOKER                       varchar(80)                   null,"+					//灶具漏气
					  "LEAKAGE_HEATER                       varchar(80)                   null,"+					//热水器漏气
					  "LEAKAGE_BOILER                      varchar(80)                   null,"+					//壁挂炉漏气
					  "LEAKAGE_NOTIFIED                     varchar(80)                   null,"+					//安检告知书
					  "LEAKGEPLACE    varchar(80)						null,"+					//漏气位置

					  " COOK_BRAND           varchar(80)                    null," +			//灶具品牌
					  "COOK_TYPE        			varchar(80)					null,"	+			//灶具型号
					  "COOKPIPE_NORMAL               	varchar(80)                 null," 	+ 			//灶具软管
					  "COOKERPIPECLAMPCOUNT             varchar(80)  					null, "	+			//安装管卡个数
					  "COOKERPIPYLENGTH				varchar(80)					null,"  +			//更换软管长度
					  "COOK_DATE            varchar(80)                    null," +			//灶具购置日期

					  "WATER_BRAND          varchar(80)                    null," +		//热水器品牌
					  "WATER_TYPE          varchar(80)                    null,"+				//热水器型号
					  "WATER_PIPE          varchar(80)                    null,"+	 			//热水器软管
					  "WATER_FLUE          varchar(80)                    null,"+				//热水器烟道
					  "WATER_NUME          varchar(80)                    null,"+				//更换管卡数
					  "WATER_DATE           varchar(80)                    null," +			//热水器购置日期
					  "WATER_HIDDEN         varchar(80)                    null," +		//热水器隐患

					  " WHE_BRAND            varchar(80)                    null," +			//	壁挂炉品牌
					  " WHE_TYPE            varchar(80)                    null," +	  		//壁挂炉型号
					  " WHE_DATE             varchar(80)                    null," +			//壁挂炉购置日期
					  " WHE_HIDDEN         varchar(80)                    null," +		//壁挂楼隐患

 					 " USER_SUGGESTION             varchar(80)         null," +			//客户意见
 					 " USER_SATISFIED             varchar(80)                 null,"	+			//客户满意度
 					 " USER_SIGN             varchar(80)                    		null," +			//客户签名

					  "THREAT            	  varchar(80)                    					null,"	+			//隐患
					  "PHOTO_FIRST           	  varchar(80)                    		null,"	+	  			//照片1
					  "PHOTO_SECOND           	  varchar(80)                    	null,"	+			//照片2
					  "PHOTO_THIRD           	  varchar(80)                    		null,"	+				//照片3
					  "PHOTO_FOUTH           	  varchar(80)                    	null,"	+			//照片4
					  "PHOTO_FIFTH        	  varchar(80)                    		null,"	+	 			//照片5
					  "NEEDS_REPAIR        	  varchar(80)                    		null,"	+	 			//是否需要维修
					  "REPAIRMAN        	  varchar(80)                    		null,"	+	 			//维修人
					  "REPAIRMAN_ID        	  varchar(80)                    		null,"	+	 			//维修人ID
					  "REPAIR_STATE	     varchar(80)                  null," +              //维修状态
					  "CHECKPLAN_ID VARCHAR(80) null)";                                    //安检计划ID
				db.execSQL(sql);
				//入户安检表
				db.execSQL("DROP TABLE IF EXISTS T_INSPECTION");
				sql = "CREATE TABLE T_INSPECTION ("+
						"ID  TEXT(255) NOT NULL,"+
						"F_CARDNUM  TEXT(255),"+
						"F_CONSUMERNAME  TEXT(255),"+
						"F_CONSUMERPHONE  TEXT(255),"+
						"F_ROOMNUMBER  TEXT(255),"+
						"F_RENKOU  REAL(10),"+
						"F_DONGJIELIANG  REAL(126),"+
						"F_COMMUNITY  TEXT(255),"+
						"F_COMMUNITY2  TEXT(255),"+
						"F_PROPERTY  TEXT(255),"+
						"F_UNITADDRESS  TEXT(255),"+
						"F_ANJIANREN  TEXT(255),"+
						"F_ANJIANRIQI  TEXT(7),"+
						"F_GONGNUAN  TEXT(255),"+
						"F_SHANGCIRIQI  TEXT(7),"+
						"F_JUJIAN  TEXT(255),"+
						"F_RUHU  TEXT(255),"+
						"F_BAOJINGQI  TEXT(255),"+
						"F_BAOJINGQICHANG  TEXT(255),"+
						"F_SHIYONGSHIJIAN  REAL(10),"+
						"F_TANTOU  TEXT(255),"+
						"F_LGZHENGCHANG  REAL(1),"+
						"F_LGANSHE  REAL(1),"+
						"F_LGMINGSHE  REAL(1),"+
						"F_LGFUSHI  REAL(1),"+
						"F_LGSIGAI  REAL(1),"+
						"F_LGLOUQI  REAL(1),"+
						"F_LGQITA  REAL(1),"+
						"F_LIGUAN  TEXT(255),"+
						"F_KAHAO  TEXT(255),"+
						"F_RQBIAOXING  TEXT(255),"+
						"F_BIAOCHANG  TEXT(255),"+
						"F_BIAOHAO  TEXT(255),"+
						"F_QBSHIYONG  TEXT(255),"+
						"F_JBDUSHU  REAL(10),"+
						"F_KACHANGJIA  TEXT(255),"+
						"F_SHENGYU  REAL(10),"+
						"F_SIBIAO  REAL(1),"+
						"F_CHANGTONG  REAL(1),"+
						"F_BIAOMENG  REAL(1),"+
						"F_QBXIUSHI  REAL(1),"+
						"F_FANZHUANG  REAL(1),"+
						"F_QBLOUQI  REAL(1),"+
						"F_QBQITA  REAL(1),"+
						"F_QIBIAO  TEXT(255),"+
						"F_QBCHULI  TEXT(255),"+
						"F_GASDOTELL  TEXT(255),"+
						"F_BIAOQIANFA  TEXT(255),"+
						"F_ZAOQIANFA  TEXT(255),"+
						"F_ZIBIFA  TEXT(255),"+
						"F_FMCHULI  TEXT(255),"+
						"F_BHGZHENGCHANG  REAL(1),"+
						"F_BHGBAOGUAN  REAL(1),"+
						"F_BHGLOUQI  REAL(1),"+
						"F_BHGWOSHI  REAL(1),"+
						"F_BHGKETING  REAL(1),"+
						"F_BHGGAIGUAN  REAL(1),"+
						"F_BHGDIANYUAN  REAL(1),"+
						"F_BHGZAOJU  REAL(1),"+
						"F_BHGCHULI  TEXT(255),"+
						"F_QPIPEDOTELL  TEXT(255),"+
						"F_JPGZHENGCHANG  REAL(1),"+
						"F_JPGLOUQI  REAL(1),"+
						"F_JPGLAOHUA  REAL(1),"+
						"F_JPGGUOCHANG  REAL(1),"+
						"F_JPGWU  REAL(1),"+
						"F_JPGWUGUANJIA  REAL(1),"+
						"F_JPGYOUGUANJIA  REAL(1),"+
						"F_JPGCHULI  TEXT(255),"+
						"F_PIPEDOTELL  TEXT(255),"+
						"F_ZJPINPAI  TEXT(255),"+
						"F_ZJSHIYONG  REAL(1),"+
						"F_ZJLEIXING  TEXT(255),"+
						"F_ZJNIAN  REAL(10),"+
						"F_ZJYUE  REAL(10),"+
						"F_ZJXIANZHUANG  TEXT(255),"+
						"F_ZJJIANYI  TEXT(255),"+
						"F_BGLPINPAI  TEXT(255),"+
						"F_BGLSHIYONG  REAL(1),"+
						"F_BGLXINGHAO  TEXT(255),"+
						"F_BGLGONGLV  REAL(10),"+
						"F_BGLNIAN  REAL(10),"+
						"F_BGLYUE  REAL(10),"+
						"F_BGLWEIZHI  TEXT(255),"+
						"F_BGLJIANYI  TEXT(255),"+
						"F_BEIZHU  TEXT(255),"+
						"F_PIPEDODATE  TEXT(7),"+
						"F_KEHUPINGJIA  TEXT(255),"+
						"F_BHGWXD  REAL(1),"+
						"F_RSHQPINPAI  TEXT(255),"+
						"F_RSHQSHIYONG  REAL(1),"+
						"F_RSHQXINGHAO  REAL(10),"+
						"F_RSHQNIAN  REAL(10),"+
						"F_RSHQYUE  REAL(10),"+
						"F_RSHQXIANZHUANG  TEXT(255),"+
						"F_RSHQJIANYI  TEXT(255),"+
						"OP_CODE  TEXT(255),"+
						"OP_DATE  TEXT(7),"+
						"OP_TIME  TEXT(7),"+
						"F_LASTSHENGYU  REAL(126),"+
						"F_MONTHS  REAL(126),"+
						"F_YUEJUN  REAL(126),"+
						"F_VAVLEDOTELL  TEXT(255),"+
						"F_SHANGCIYUQI  REAL(126),"+
						"BULU  TEXT(255),"+
						"F_KEHUYIJIAN  TEXT(255),"+
						"F_BGLXIANZHUANG  TEXT(255),"+
						"YI  REAL(10),"+
						"F_STATE  TEXT(255),"+
						"ANJIANRIQISTR  TEXT(255),"+
						"SHIYONGSHIJIANDATE  TEXT(7),"+
						"WEIXIUSHIJIANSTR  TEXT(255),"+
						"F_BUYGAS  REAL(126),"+
						"F_ZUZHU  TEXT(255),"+
						"F_RSQRST  TEXT(255),"+
						"F_BGRST  TEXT(255),"+
						"F_OLDJIBIAOSHU  REAL(10),"+
						"F_BGLTIAN  REAL(10),"+
						"F_PIPEBZ  TEXT(255),"+
						"F_RSQBZ  TEXT(255),"+
						"F_BIGUAGUOLUBZ  TEXT(255),"+
						"F_RQZCHAONIANXIAN  REAL(1),"+
						"F_RQZXH  REAL(1),"+
						"F_RGGC  REAL(1),"+
						"F_RGLH  REAL(1),"+
						"F_RSQLQ  REAL(1),"+
						"F_RQZLJST  REAL(1),"+
						"F_FBWXBB  REAL(1),"+
						"F_JULIBUGOU  REAL(1),"+
						"F_GUANDAOGUAWU  REAL(1),"+
						"F_JIECHUDIANYUAN  REAL(1),"+
						"F_GHRQZ  REAL(1),"+
						"F_GHRQZORWX  REAL(1),"+
						"F_JIANYIGAIZAO  REAL(1),"+
						"F_TZSHWX  REAL(1),"+
						"F_ZZDWGZ  REAL(1),"+
						"F_FBJXTF  REAL(1),"+
						"F_ZZDWGZ2  REAL(1),"+
						"F_YCGW  REAL(1),"+
						"F_YCDYX  REAL(1),"+
						"F_ZGBEIZHU  TEXT(255),"+
						"F_ISZHONGDIAN  TEXT(255),"+
						"CITY  TEXT(255),"+
						"F_AREA  TEXT(255),"+
						"ROAD  TEXT(255),"+
						"REGION_NAME  TEXT(255),"+
						"UNIT_NAME  TEXT(255),"+
						"UNIT_ADDRESS  TEXT(255),"+
						"CUS_DOM  TEXT(255),"+
						"CUS_DY  TEXT(255),"+
						"CUS_FLOOR  TEXT(255),"+
						"CUS_ROOM  TEXT(255),"+
	 					 " USER_SIGN             varchar(80)                    		null," +			//客户签名
						  "PHOTO_FIRST           	  varchar(80)                    		null,"	+	  			//照片1
						  "PHOTO_SECOND           	  varchar(80)                    	null,"	+			//照片2
						  "PHOTO_THIRD           	  varchar(80)                    		null,"	+				//照片3
						  "PHOTO_FOUTH           	  varchar(80)                    	null,"	+			//照片4
						  "PHOTO_FIFTH        	  varchar(80)                    		null,"	+	 			//照片5
						  "NEEDS_REPAIR        	  varchar(80)                    		null,"	+	 			//是否需要维修
						  "REPAIRMAN        	  varchar(80)                    		null,"	+	 			//维修人
						  "REPAIRMAN_ID        	  varchar(80)                    		null,"	+	 			//维修人ID
						  "REPAIR_STATE	     varchar(80)                  null," +              //维修状态
						  "CHECKPLAN_ID VARCHAR(80) null,"      +                            //安检计划ID
						  "REPAIR_DATE varchar(80) null, " +                               //维修日期
						  "CHECKPAPER_ID varchar(80) null ," +                     //安检单编号
						  "CONDITION varchar(80) null, " +                             //状态
							"hasNotified  varchar(80),"+                                     //已发到访不遇卡
							"ARRIVAL_TIME       varchar(80)                    	 null,"+			       //到达时间
							"DEPARTURE_TIME   varchar(80)             	    null,"+		   		   //离开时间
							"f_archiveaddress   varchar(255)             	    null,"+		   		   //档案地址
							"f_checktype   varchar(10)             	    null,"+		   		   //安检单、抄表单
							"F_ALARM_INSTALLATION_TIME  TEXT(255) null ," + 
							"F_ALARM_EXPIRE_TIME  TEXT(255) null ," + 
							"F_METER_MANUFACTURE_DATE  TEXT(255) null ," + 
							"F_METER_TYPE  TEXT(255) null ," + 
							"F_ICCARD_TYPE  TEXT(255) null ," + 
							"F_NEWMETER  REAL(1) null ," + 
							"F_BALANCE  TEXT(255) null ," + 
							"F_METER_COVER  TEXT(255) null ," + 
							"F_READING_MISMATCH  REAL(1) null ," + 
							"F_METER_WRAPPED  REAL(1) null ," + 
							"F_METER_HANGER  REAL(1) null ," + 
							"F_METER_NEARFIRE  REAL(1) null ," + 
							"F_METER_UNFAVORABLE  REAL(1) null ," + 
							"F_PLUMBING_TYPE  TEXT(255) null ," + 
							"F_PLUMBING_VALVE  REAL(1) null ," + 
							"F_PLUMBING_SLIPKNOT  REAL(1) null ," + 
							"F_PLUMBING_SCALEKNOT  REAL(1) null ," + 
							"F_PLUMBING_DIAMETER  REAL(1) null ," + 
							"F_LGBAOGUO  REAL(1) null ," + 
							"F_LGGUAWU  REAL(1) null ," + 
							"F_LGHUOYUAN  REAL(1) null ," + 
							"F_LGWEIGUDING  REAL(1) null ," + 
							"F_LGCHUANYUE  REAL(1) null ," + 
							"F_LGBUBIANWEIXIU  REAL(1) null ," + 
							"F_PLUMBING_LEAKAGE_VALVE  REAL(1) null ," + 
							"F_PLUMBING_LEAKAGE_SCALEKNOT  REAL(1) null ," + 
							"F_PLUMBING_LEAKAGE_SLIPKNOT  REAL(1) null ," + 
							"F_PLUMBING_LEAKAGE_TRIPLE  REAL(1) null ," + 
							"F_PLUMBING_LEAKAGE_DIAMETER  REAL(1) null ," + 
							"F_METERVALVE_TYPE  TEXT(255) null ," + 
							"F_BHGGUAWU  REAL(1) null ," + 
							"F_BHGJINZHIQUYU  REAL(1) null ," + 
							"F_BHGRST  REAL(1) null ," + 
							"F_BHGFUSHI  REAL(1) null ," + 
							"F_BHGBUBIANWEIXIU  REAL(1) null ," + 
							"F_BHGQITA  REAL(1) null ," + 
							"F_BHGBEIZHU  TEXT(255) null ," + 
							"F_JPGDIAODING  REAL(1) null ," + 
							"F_JPGANMAI  REAL(1) null ," + 
							"F_COOKER_INSTALLATION_TIME  TEXT(255) null ," + 
							"F_COOKER_EXPIRE_TIME  TEXT(255) null ," + 
							"F_HEATER_INSTALLATION_TIME  TEXT(255) null ," + 
							"F_HEATER_EXPIRE_TIME  TEXT(255) null ," + 
							"F_FURNACE_INSTALLATION_TIME  TEXT(255) null ," + 
							"F_FURNACE_EXPIRE_TIME  TEXT(255) null ," + 
							"F_COOKER_OVERDUE  REAL(1) null ," + 
							"F_COOKER_NOFIREPROTECTION  REAL(1) null ," + 
							"F_COOKER_LEAKAGE  REAL(1) null ," + 
							"F_HEATER_PLACE  TEXT(255) null ," + 
							"F_HEATER_OVERDUE  REAL(1) null ," + 
							"F_HEATER_SOFTCONNECTOR  REAL(1) null ," + 
							"F_HEATER_EMIT  REAL(1) null ," + 
							"F_HEATER_TRAPPED  REAL(1) null ," + 
							"F_HEATER_LEAKAGE  REAL(1) null ," + 
							"F_HEATER_LEAKAGE_CONNETOR  REAL(1) null ," + 
							"F_HEATER_LEAKAGE_VALVE  REAL(1) null ," + 
							"F_HEATER_LEAKAGE_HEATER  REAL(1) null ," + 
							"F_FURNACE_OVERDUE  REAL(1) null ," + 
							"F_FURNACE_SOFTCONNECTOR  REAL(1) null ," + 
							"F_FURNACE_EMIT  REAL(1) null ," + 
							"F_FURNACE_TRAPPED  REAL(1) null ," + 
							"F_FURNACE_LEAKAGE  REAL(1) null ," + 
							"F_FURNACE_LEAKAGE_CONNETOR  REAL(1) null ," + 
							"F_FURNACE_LEAKAGE_VALVE  REAL(1) null ," + 
							"F_FURNACE_LEAKAGE_FURNACE  REAL(1) null ," + 
							"F_COOKER_PRECAUTION_REMARK  TEXT(255) null ," + 
							"F_HEATER_PRECAUTION_REMARK  TEXT(255) null ," + 
							"F_FURNACE_PRECAUTION_REMARK  TEXT(255) null ," + 
							"F_PRECAUTION_KITCHEN  REAL(1) null ," + 
							"F_PRECAUTION_OPENKITCHEN  REAL(1) null ," + 
							"F_PRECAUTION_MULTISOURCE  REAL(1) null ," + 
							"F_PRECAUTION_OTHERUSE  TEXT(255) null ," + 
							"F_RENOW_ID  TEXT(255) null ," + 
							"F_JPG_TYPE  TEXT(255) null ," + 
							"PRIMARY KEY (ID))";
				db.execSQL(sql);
				//隐患表
				db.execSQL("DROP TABLE IF EXISTS T_IC_SAFECHECK_HIDDEN");
				sql = "create table T_IC_SAFECHECK_HIDDEN (" +
						"id VARCHAR(80) not null," +
						"equipment            varchar(80)                    not null,"+     //设备
						"param              varchar(80)                    not null,"+        //参数
						"value       varchar(80)                    not null,"+      //值
						"INSPECTION_ID  varchar(80)   not null ," +
						"NAME  varchar(80)   not null ," +
						"BZ                   varchar(80)                    null," +
						" PRIMARY KEY  (id, param, value))";
				db.execSQL(sql);

		
				//维修安检单表
				db.execSQL("DROP TABLE IF EXISTS T_REPAIR_TASK");
				sql = "create table T_REPAIR_TASK as select * from T_INSPECTION ";
				db.execSQL(sql);
				//维修隐患
				db.execSQL("DROP TABLE IF EXISTS T_REPAIR_ITEM");
				sql = "create table T_REPAIR_ITEM as select * from T_IC_SAFECHECK_HIDDEN ";
				db.execSQL(sql);
				
				//保存安检临时表
				db.execSQL("DROP TABLE IF EXISTS T_INP");
				sql = "create table T_INP as select * from T_INSPECTION ";
				db.execSQL(sql);
				//保存安检临时表
				db.execSQL("DROP TABLE IF EXISTS T_INP_LINE");
				sql = "create table T_INP_LINE as select * from T_IC_SAFECHECK_HIDDEN ";
				db.execSQL(sql);
				
				//保存维修参数
				db.execSQL("DROP TABLE IF EXISTS T_PARAMS");
				sql = "create table T_PARAMS (" +
						"ID                  varchar(80)                      null,"+  //参数编号
						"NAME             varchar(80)                      null,"+  //参数名称
						"CODE           varchar(80)                      null,"+  //参数代码
						"PRIMARY KEY  (ID, CODE))";
				db.execSQL(sql);
//				for(RepairMan rm : RepairManList)
//					db.execSQL("INSERT INTO T_PARAMS(ID, CODE, NAME) VALUES(?,?,?)", new String[]{"维修人员", rm.id, rm.name});
				for(int i=0; i<map.size(); i++)
				{
					Map<String, Object> aMap = map.get(i);
					db.execSQL("INSERT INTO T_PARAMS(ID, CODE, NAME) VALUES(?,?,?)", new String[]{ (String)aMap.get("col0"),  (String)aMap.get("col1"), (String)aMap.get("col2")});
				}
				//维修结果，把维修选项放到此表
				db.execSQL("DROP TABLE IF EXISTS T_REPAIR_RESULT");
				sql = "create table T_REPAIR_RESULT (" +
						"ID                  varchar(80)                      null,"+  //安检编号
						"CONTENT             varchar(200)                      null,"+  //维修内容
						"PRIMARY KEY  (ID, CONTENT))";
				db.execSQL(sql);
				//维修结果临时表
				db.execSQL("DROP TABLE IF EXISTS T_REPAIR_RESULT2");
				sql = "create table T_REPAIR_RESULT2 (" +
						"ID                  varchar(80)                      null,"+  //安检编号
						"CONTENT             varchar(200)                      null,"+  //维修内容
						"PRIMARY KEY  (ID, CONTENT))";
				db.execSQL(sql);
				db.close();
				db = mContext.openOrCreateDatabase(Util.getDBName2(mContext), Context.MODE_PRIVATE, null);
				createTables(db);
				db.close();
				
				//提示创建成功
				Toast toast = Toast.makeText(mContext, "初始化完成。", Toast.LENGTH_SHORT);
				toast.show();
			} catch(Exception e) {
				e.printStackTrace();
				Toast.makeText(mContext, "初始化失败！", Toast.LENGTH_SHORT).show();
			}
}
private void createTables(SQLiteDatabase db) {
		//本地保存的安检员
		db.execSQL("DROP TABLE IF EXISTS T_LOCAL_MECHANICS");
		String sql = "create table T_LOCAL_MECHANICS (" +
				"NAME                  varchar(80)                      null,"+  //维修员
				"CODE                  varchar(80)                      null,"+  //维修员编码
				"PRIMARY KEY  (NAME,CODE))";
		db.execSQL(sql);
		//维修列表							T_BX_REPAIR_ALL
		db.execSQL("DROP TABLE IF EXISTS T_BX_REPAIR_ALL");
		sql = "CREATE TABLE T_BX_REPAIR_ALL (ID integer primary key autoincrement"//维修记录id自增唯一
				//用户信息
				+  ",f_userid TEXT(20)"					//用户编号
				+  ",f_username TEXT(50)"				//用户姓名
				+  ",f_quyu TEXT(50)"				//用户类别f_cardid
				+  ",f_cardid TEXT(50)"				//用户卡号
				+  ",f_address TEXT(80)"				//用户地址
				+  ",f_linktype TEXT(20)"				//用户电话
				+  ",f_unitname TEXT(50)"				//用户单位
				//派单信息
				+  ",f_sender TEXT(50)"					//派单人
				+  ",f_senddate TEXT(20)"				//派单日期
				+  ",f_sendtime TEXT(20)"				//派单时间
				+  ",f_jibie TEXT(20)"				//工单级别
				+  ",f_cucode TEXT(20)"					//报修编号
				+  ",f_laiyuan TEXT(20)"					//信息来源
				+  ",f_repairtype TEXT(40)"				//报修类型
				+  ",f_phone TEXT(20)"					//来电电话
				+  ",f_repairreason TEXT(200)"			//反映内容（来电使所记录的故障）
				+  ",f_meternumber TEXT(200)"			//反映内容（来电使所记录的故障）
				+  ",f_dealonline TEXT(200)"			//备注
				+  ",f_jiedandate TEXT(200)"			//接单时期
				+  ",f_jiedan2 TEXT(200)"			//接单时期
				+  ",f_fuzeren TEXT(200)"			//接单时期
				+  ",f_wangongdate TEXT(200)"			//接单时期
				+  ",f_wangong TEXT(200)"			//接单时期
//				//气表信息
//				+  ",f_meternumber TEXT(20)"			//表号
//				+  ",f_gasmeteraccomodations TEXT(20)"	//表底数
//				+  ",f_lastrecord TEXT(20)"				//表读数
//				+  ",f_metertype TEXT(50)"				//气表型号
//				+  ",f_aroundmeter TEXT(20)"			//左右表
//				+  ",f_metergasnums TEXT(20)"			//累计购气量
				//程序辅助字段
				+  ",MUTE TEXT(10)"						//是否选择静音
			//	+  ",f_wangong TEXT(30)"//完成时间
				+  ",f_havacomplete TEXT(10) default '未完成'"//完成情况
			//	+  ",f_gaswatchbrand TEXT(40) null"//气表品牌
			//	+  ",surplus TEXT(20) default '0'"//补气量
				+  ",f_qingkuang TEXT(200)" //上门维修记录
				+  ",f_jieguo TEXT(200)" //上门维修记录
				+  ",f_downloadstatus TEXT(200)" // 工单状态
				+  ",inshi TEXT(200)"//上门维修记录
				+  ",f_uploadstatus TEXT(200)"//上传标记
				//+  ",servercheck TEXT(200)"//上门维修记录
				//+  ",shul TEXT(200)"//上门维修记录
				+  ",f_shixian TEXT(200)"// 工作时日
				+  ",f_qdstatus TEXT(200)"//抢单标记
				//结束
				+  ")";
		db.execSQL(sql);
//		//维修记录
//		db.execSQL("DROP TABLE IF EXISTS T_BX_REPAIR_RESULT");
//		sql = "CREATE TABLE T_BX_REPAIR_RESULT (ID TEXT(50) NOT NULL"
//				+	",f_cucode TEXT(20)"					//报修编号
//				+ 	",finishtime timestamp"//完成时间
//				+ 	"completion text(10)"//完成情况
//				+	",f_smwxjl TEXT(200)"//上门维修记录
//				+	")";			
////		+ "PRIMARY KEY  (ID, repairCode,resuleid))";	
//		db.execSQL(sql);

		}
private void UpdateParam(List<Map<String, Object>> map) {
		try {
			//建立数据库
			SQLiteDatabase db = mContext.openOrCreateDatabase(Util.getDBName(mContext), Context.MODE_PRIVATE, null);		
			//保存维修参数
			db.execSQL("DROP TABLE IF EXISTS T_PARAMS");
			String sql = "create table T_PARAMS (" +
					"ID                  varchar(80)                      null,"+  //参数编号
					"NAME             varchar(80)                      null,"+  //参数名称
					"CODE           varchar(80)                      null,"+  //参数代码
					"PRIMARY KEY  (ID, CODE))";
			db.execSQL(sql);
//			for(RepairMan rm : RepairManList)
//				db.execSQL("INSERT INTO T_PARAMS(ID, CODE, NAME) VALUES(?,?,?)", new String[]{"维修人员", rm.id, rm.name});
			//安检维修选项
			for(int i=0; i<map.size(); i++)
			{
				Map<String, Object> aMap = map.get(i);
				db.execSQL("INSERT INTO T_PARAMS(ID, CODE, NAME) VALUES(?,?,?)", new String[]{ (String)aMap.get("col0"),  (String)aMap.get("col1"), (String)aMap.get("col2")});
			}
			db.close();
			
			//提示创建成功
			Toast toast = Toast.makeText(mContext, "参数提取完成。", Toast.LENGTH_SHORT);
			toast.show();
		} catch(Exception e) {
			Toast.makeText(mContext, "参数提取失败！", Toast.LENGTH_SHORT).show();
		}
	}
//用户姓名
	public StringObservable UseName = new StringObservable("");

	// 旧密码
	public StringObservable OldPassword = new StringObservable("");
	// 新密码
	public StringObservable NewPassword = new StringObservable("");
	// 再次输入新密码
	public StringObservable NewPasswordAgain = new StringObservable("");

	public Command ChangePassword = new Command() {
		@Override
		public void Invoke(View view, Object... args) {
			//输入验证
			if (CheckPassword()) {
				//调用服务
				Thread th = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							HttpPost httpPost = new HttpPost(Vault.AUTH_URL + "entity");
							String s="[{data:{id:'" + Util.getSharedPreference(mContext, Vault.USER_ID) + "',password:'" + NewPassword.get() + "'}}]";
							StringEntity entity = new StringEntity("[{data:{id:'" + Util.getSharedPreference(mContext, Vault.USER_ID) + "',password:'" + NewPassword.get() + "'}}]" );
							httpPost.setEntity(entity);
							
							HttpClient httpClient = new DefaultHttpClient();
							HttpContext httpContext = new BasicHttpContext();
							HttpResponse response = httpClient.execute(httpPost, httpContext);
							int code = response.getStatusLine().getStatusCode();

							// 数据下载完成
							if (code == 200) {
								String strResult = EntityUtils.toString(response
										.getEntity());
								Message msg = new Message();
								msg.obj = strResult;
								msg.what = 1;
								mHandler.sendMessage(msg);
							}
							else 
							{
								Message msg = new Message();
								msg.what = 2;
								mHandler.sendMessage(msg);
							}
						}
						catch(Exception e)
						{
							Message msg = new Message();
							msg.what = 0;
							mHandler.sendMessage(msg);
						}
					}
				});
				th.start();
			}
		}
	};

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (1 == msg.what)
			{
				Toast.makeText(mContext, "密码修改成功！", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(mContext, "密码修改失败！", Toast.LENGTH_SHORT).show();
			}
		}
	};

	private boolean CheckPassword() {
		if ((OldPassword.get()).equals(Util.getSharedPreference(mContext, Vault.PASSWORD))) {
			if ((NewPassword).get().equals(NewPasswordAgain.get()) && (!(NewPassword.get().equals("")))) {
				return true;
			} else {
				Toast.makeText(mContext, "新密码输入有误，请重输！", Toast.LENGTH_SHORT).show();
				return false; 
			}
		} else {
			Toast.makeText(mContext, "原密码输入错误！", Toast.LENGTH_SHORT).show();
			return false;
		}
	}
}