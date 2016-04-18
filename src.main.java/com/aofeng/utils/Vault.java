package com.aofeng.utils;

public class Vault {
	/**
	 * �û�ID
	 */
	public static String USER_ID = "USER_ID";
	/**
	 * ��¼�û���
	 */
	public static String USER_NAME = "USER_NAME";
	/**
	 * ��¼����
	 */
	public static String PASSWORD ="ENCRYPT";	
	/**
	 * ��¼������
	 */
	public  static String CHECKER_NAME ="CHECKER_NAME";
	/**
	 * ��վ����
	 */
	public  static String STATION_NAME ="STATION_NAME";
	/**
	 * ��վ����
	 */
	public  static String STATION_ID ="STATION_ID";
	/**
	 * �������
	 */
	public static long INTERVAL = 1*10*1000L;
	
	
	public static int ITEM_ALARM = 1;             //����λ
	public static int ITEM_INSPECTED = 2;     //�Ѽ�
	public static int ITEM_REPAIRED = 4;       //����
	public static int ITEM_RETURNED = 8;     //�ѷ���
//	public static int ITEM_REJECTED = 16;    //�˵�
	
	/**
	 * �ϴ����
	 */
	public static int UPLOAD_FLAG = 1;
	
	/**
	 * �Ѽ���
	 */
	public static int INSPECT_FLAG = 2;
	
	/**
	 * �������
	 */
	public static int NEW_FLAG = 4;
	
	/**
	 * ɾ�����
	 */
	public static int DELETE_FLAG = 8;
	
	/**
	 * ά�ޱ��
	 */
	public static int REPAIR_FLAG = 16;
	
	/**
	 * �ܼ���
	 */
	public static int DENIED_FLAG = 32;
	
	/**
	 * ���˱��
	 */
	public static int NOANSWER_FLAG = 64;
	

	public static String REPAIRED_NOT="δά��";
	public static String REPAIRED_UNUPLOADED="δ�ϴ�";
	public static String REPAIRED_UPLOADED="���ϴ�";
	
	public static  String packageName= "com.aofeng.hotline";
	
	public static  String apkName ="downloadhotline.apk";
	public static String appID="hotline";
	
	
//	public static  String downloadURL = "http://192.168.173.1:8080/safecheck.apk";
//	public static  String checkVersionURL = "http://192.168.173.1:8080/rs/db/one/from%20t_singlevalue%20where%20name='hotline�汾��'";
//	public static String  DB_URL = "http://192.168.173.1:8080/rs/db/";//���ݷ����ַ
//	public static String  IIS_URL = "http://192.168.173.1:8080/rs/wx/";//�뻧�������
//	public static String  PHONE_URL = "http://192.168.173.1:8080/rs/phone/";//96777
//	public static String AUTH_URL = "http://192.168.173.1:83/rs/user/";
	
//���Ի���
/*	public static  String downloadURL = "http://113.140.5.42:7000/safecheck.apk";
 	public static  String checkVersionURL = "http://113.140.5.42:7000/rs/db/one/from%20t_singlevalue%20where%20name='hotline�汾��'";
 	public static String  DB_URL = "http://113.140.5.42:7000/rs/db/";//���ݷ����ַ
 	public static String  IIS_URL = "http://113.140.5.42:7000/rs/wx/";//�뻧�������
 	public static String  PHONE_URL = "http://113.140.5.42:7000/rs/phone/";//96777
 	public static String AUTH_URL = "http://113.140.5.42:7001/rs/user/";
    */
	
//	public static  String downloadURL = "http://192.168.10.116:8080/safecheck.apk";
//	public static  String checkVersionURL = "http://192.168.10.116:8080/rs/db/one/from%20t_singlevalue%20where%20name='hotline�汾��'";
//	public static String  DB_URL = "http://192.168.10.116:8080/rs/db/";//���ݷ����ַ
//	public static String  IIS_URL = "http://192.168.10.116:8080/rs/wx/";//�뻧�������
//	public static String  PHONE_URL = "http://192.168.10.116:8080/rs/phone/";//96777

//���Ի���
	public static  String downloadURL = "http://192.168.1.121:8080/safecheck.apk";
	public static  String checkVersionURL = "http://192.168.1.121:8080/rs/db/one/from%20t_singlevalue%20where%20name='hotline�汾��'";
	public static String  DB_URL = "http://192.168.1.121:8080/rs/db/";//���ݷ����ַ
	public static String  IIS_URL = "http://192.168.1.121:8080/rs/wx/";//�뻧�������
	public static String  PHONE_URL = "http://192.168.1.121:8080/rs/phone/";//96777
	public static String AUTH_URL = "http://192.168.1.121:82/rs/user/";
	/*	*/
	//��ʽ����
	/*	public static  String downloadURL = "http://183.196.91.66:8080/hotline/shexianhotline.apk";
		public static  String checkVersionURL = "http://183.196.91.66:8080/hotline/rs/db/one/from%20t_singlevalue%20where%20name='hotline�汾��'";
		public static String  DB_URL = "http://183.196.91.66:8080/hotline/rs/db/";//���ݷ����ַ
		public static String  IIS_URL = "http://183.196.91.66:8080/hotline/rs/wx/";//�뻧�������
		public static String  PHONE_URL = "http://183.196.91.66:8080/hotline/rs/phone/";//96777
		public static String AUTH_URL = "http://http://183.196.91.66:8080/rs/user/";
		*/  //110.18.70.106
		
		//��˾�йܻ���
	/*	 	public static  String downloadURL = "http://125.76.225.223:3000/alashanhotlinedb/alashanhotline.apk";
			public static  String checkVersionURL = "http://125.76.225.223:3000/alashanhotlinedb/rs/db/one/from%20t_singlevalue%20where%20name='hotline�汾��'";
			public static String  DB_URL = "http://125.76.225.223:3000/alashanhotlinedb/rs/db/";//���ݷ����ַ
			public static String  IIS_URL = "http://125.76.225.223:3000/alashanhotlinedb/rs/wx/";//�뻧�������
			public static String  PHONE_URL = "http://125.76.225.223:3000/alashanhotlinedb/rs/phone/";//96777
			public static String AUTH_URL = "http://125.76.225.223:3000/alashanhotlineres/rs/user/";
	 	*/  //110.18.70.106
	
}
