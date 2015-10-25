package com.aofeng.utils;

public class Vault {
	/**
	 * 用户ID
	 */
	public static String USER_ID = "USER_ID";
	/**
	 * 登录用户名
	 */
	public static String USER_NAME = "USER_NAME";
	/**
	 * 登录密码
	 */
	public static String PASSWORD ="ENCRYPT";	
	/**
	 * 登录中文名
	 */
	public  static String CHECKER_NAME ="CHECKER_NAME";
	/**
	 * 场站名字
	 */
	public  static String STATION_NAME ="STATION_NAME";
	/**
	 * 场站名字
	 */
	public  static String STATION_ID ="STATION_ID";
	/**
	 * 检查周期
	 */
	public static long INTERVAL = 1*10*1000L;
	
	
	public static int ITEM_ALARM = 1;             //报警位
	public static int ITEM_INSPECTED = 2;     //已检
	public static int ITEM_REPAIRED = 4;       //已修
	public static int ITEM_RETURNED = 8;     //已返单
//	public static int ITEM_REJECTED = 16;    //退单
	
	/**
	 * 上传标记
	 */
	public static int UPLOAD_FLAG = 1;
	
	/**
	 * 已检标记
	 */
	public static int INSPECT_FLAG = 2;
	
	/**
	 * 新增标记
	 */
	public static int NEW_FLAG = 4;
	
	/**
	 * 删除标记
	 */
	public static int DELETE_FLAG = 8;
	
	/**
	 * 维修标记
	 */
	public static int REPAIR_FLAG = 16;
	
	/**
	 * 拒检标记
	 */
	public static int DENIED_FLAG = 32;
	
	/**
	 * 无人标记
	 */
	public static int NOANSWER_FLAG = 64;
	

	public static String REPAIRED_NOT="未维修";
	public static String REPAIRED_UNUPLOADED="未上传";
	public static String REPAIRED_UPLOADED="已上传";
	
	public static  String packageName= "com.aofeng.hotline";
	
	public static  String apkName ="downloadhotline.apk";
	public static String appID="hotline";
	
	
//	public static  String downloadURL = "http://192.168.173.1:8080/safecheck.apk";
//	public static  String checkVersionURL = "http://192.168.173.1:8080/rs/db/one/from%20t_singlevalue%20where%20name='hotline版本号'";
//	public static String  DB_URL = "http://192.168.173.1:8080/rs/db/";//数据服务地址
//	public static String  IIS_URL = "http://192.168.173.1:8080/rs/wx/";//入户安检服务
//	public static String  PHONE_URL = "http://192.168.173.1:8080/rs/phone/";//96777
//	public static String AUTH_URL = "http://192.168.173.1:83/rs/user/";
	
//测试环境
/*	public static  String downloadURL = "http://113.140.5.42:7000/safecheck.apk";
 	public static  String checkVersionURL = "http://113.140.5.42:7000/rs/db/one/from%20t_singlevalue%20where%20name='hotline版本号'";
 	public static String  DB_URL = "http://113.140.5.42:7000/rs/db/";//数据服务地址
 	public static String  IIS_URL = "http://113.140.5.42:7000/rs/wx/";//入户安检服务
 	public static String  PHONE_URL = "http://113.140.5.42:7000/rs/phone/";//96777
 	public static String AUTH_URL = "http://113.140.5.42:7001/rs/user/";
    */
	
//	public static  String downloadURL = "http://192.168.10.116:8080/safecheck.apk";
//	public static  String checkVersionURL = "http://192.168.10.116:8080/rs/db/one/from%20t_singlevalue%20where%20name='hotline版本号'";
//	public static String  DB_URL = "http://192.168.10.116:8080/rs/db/";//数据服务地址
//	public static String  IIS_URL = "http://192.168.10.116:8080/rs/wx/";//入户安检服务
//	public static String  PHONE_URL = "http://192.168.10.116:8080/rs/phone/";//96777

//测试环境
	public static  String downloadURL = "http://192.168.1.108:8080/safecheck.apk";
	public static  String checkVersionURL = "http://192.168.1.108:8080/rs/db/one/from%20t_singlevalue%20where%20name='hotline版本号'";
	public static String  DB_URL = "http://192.168.1.108:8080/rs/db/";//数据服务地址
	public static String  IIS_URL = "http://192.168.1.108:8080/rs/wx/";//入户安检服务
	public static String  PHONE_URL = "http://192.168.1.108:8080/rs/phone/";//96777
	public static String AUTH_URL = "http://192.168.1.108:83/rs/user/";
	/*	*/
	
	
	//正式环境
	/*	public static  String downloadURL = "http://183.196.91.66:8080/hotline/shexianhotline.apk";
		public static  String checkVersionURL = "http://183.196.91.66:8080/hotline/rs/db/one/from%20t_singlevalue%20where%20name='hotline版本号'";
		public static String  DB_URL = "http://183.196.91.66:8080/hotline/rs/db/";//数据服务地址
		public static String  IIS_URL = "http://183.196.91.66:8080/hotline/rs/wx/";//入户安检服务
		public static String  PHONE_URL = "http://183.196.91.66:8080/hotline/rs/phone/";//96777
		public static String AUTH_URL = "http://http://183.196.91.66:8080/rs/user/";
		*/  //110.18.70.106
		
		//公司托管环境
	/*	 	public static  String downloadURL = "http://125.76.225.223:3000/alashanhotlinedb/alashanhotline.apk";
			public static  String checkVersionURL = "http://125.76.225.223:3000/alashanhotlinedb/rs/db/one/from%20t_singlevalue%20where%20name='hotline版本号'";
			public static String  DB_URL = "http://125.76.225.223:3000/alashanhotlinedb/rs/db/";//数据服务地址
			public static String  IIS_URL = "http://125.76.225.223:3000/alashanhotlinedb/rs/wx/";//入户安检服务
			public static String  PHONE_URL = "http://125.76.225.223:3000/alashanhotlinedb/rs/phone/";//96777
			public static String AUTH_URL = "http://125.76.225.223:3000/alashanhotlineres/rs/user/";
	 	*/  //110.18.70.106
	
}
