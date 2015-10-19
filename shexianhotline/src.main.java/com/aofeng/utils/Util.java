package com.aofeng.utils;

import gueei.binding.collections.ArrayListObservable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.Spinner;



public class Util {
	public static String FormatDateToday(String format)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return  formatter.format(new Date());
	}

	public static String FormatDate(String format, long l)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return  formatter.format(new Date(l));
	}
	
	public static String FormatTime(String format, String dt) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return  formatter.format(new Date(dt));
	}

	public static String FormatTimeNow(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return  formatter.format(new Date());
	}

	public static boolean fileExists(String path)
	{
		File file = new File(path);
		if(file.exists())
			return true;
		else
			return false;
	}

	public static boolean DBExists(Context context)
	{
		// �ж����ݿ��Ƿ���ڣ������ý���
		if (!(Util.fileExists(context.getDatabasePath(Util.getDBName(context))))) {
			return false;
		}
		else
		{
			//������б����Ƿ����
			SQLiteDatabase db = context.openOrCreateDatabase(Util.getDBName(context), Context.MODE_PRIVATE, null);
			try
			{
				String sql = "select * from T_IC_SAFECHECK_PAPAER where 1=1";
				db.rawQuery(sql, null);
				sql = "select * from T_INSPECTION where 1=1";
				db.rawQuery(sql, null);
				sql = "select * from T_IC_SAFECHECK_HIDDEN where 1=1";
				db.rawQuery(sql, null);
				sql = "select * from T_BX_REPAIR_ALL where 1=1";
				db.rawQuery(sql, null);
			}
			catch(Exception e)
			{
				return false;
			}
			finally
			{
				if(db != null)
					db.close();
			}
			return true;
		}
	}
	
	/**
	 * @param databasePath
	 * @return
	 */
	public static boolean fileExists(File path) {
		return path.exists();
	}

	/**
	 * ɾ��uuid��һϵ���ļ�
	 * @param uuid
	 */
	public static void deleteFiles(Context context, String uuid) {
		File file = new File(Util.getSharedPreference(context, "FileDir") + uuid + "_sign.png");
		file.delete();
		for(int i =1; i<6; i++)
		{
			file = new File(Util.getSharedPreference(context, "FileDir") + uuid + "_" + i + ".jpg");
			file.delete();
		}
	}
	
		/**
		 * ������
		 */
	public static void ClearBit(Context context, int mask, String paperId) {
			SQLiteDatabase db = context.openOrCreateDatabase(Util.getDBName(context),
					Context.MODE_PRIVATE, null);
			String sql = "update T_IC_SAFECHECK_PAPAER set CONDITION=CAST (CAST (CONDITION as INTEGER) & " + (~mask)
					+ " as TEXT) where id=?";
			db.execSQL(sql, new Object[] { paperId });
			db.close();
		}

		/**
		 * ���ñ��
		 */
	public static void SetBit(Context context, int mask, String paperId) {
		//�Ѽ졢�ܼ졢���� һ��
		if(mask == Vault.INSPECT_FLAG)
			ClearBit(context, Vault.DENIED_FLAG+Vault.NOANSWER_FLAG  + Vault.UPLOAD_FLAG, paperId);
		else if(mask == Vault.DENIED_FLAG)
			ClearBit(context, Vault.INSPECT_FLAG+Vault.NOANSWER_FLAG + Vault.UPLOAD_FLAG, paperId);
		else if(mask == Vault.NOANSWER_FLAG)
			ClearBit(context, Vault.INSPECT_FLAG+Vault.DENIED_FLAG + Vault.UPLOAD_FLAG, paperId);

		SQLiteDatabase db = context.openOrCreateDatabase(Util.getDBName(context),
				Context.MODE_PRIVATE, null);
		String sql = "update T_IC_SAFECHECK_PAPAER set CONDITION=CAST (CAST(CONDITION as INTEGER) | " + mask
				+ "  as TEXT) where id=?";
		db.execSQL(sql, new Object[] { paperId });
		db.close();
	}
	
	public static  Bitmap getLocalBitmap(String url) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally
		{
			if(fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * ���������ʱ�洢
	 */
public static void ClearCache(Context context, String uuid) {
		SQLiteDatabase db = context.openOrCreateDatabase(Util.getDBName(context),
				Context.MODE_PRIVATE, null);
		String sql = "delete from T_INP where id=?";
		db.execSQL(sql, new Object[] { uuid });
		sql = "delete from T_INP_LINE where id=?";
		db.execSQL(sql, new Object[] { uuid });
		db.close();
	}

/**
 * �Ƿ��л���
 */
public static boolean IsCached(Context context, String uuid) {
	Boolean result= false;
	SQLiteDatabase db = context.openOrCreateDatabase(Util.getDBName(context),
			Context.MODE_PRIVATE, null);
	String sql = "select id from T_INP where id=?";
	Cursor c = db.rawQuery(sql, new String[] { uuid });
	if(c.moveToNext())
		result = true;
	else
		result = false;
	db.close();
	return result;
}

/**
 * �õ�����ά��ѡ��
 * @param context
 * @return
 */
public static ArrayList<String> getRepairList(Context context)
{
	ArrayList<String> list = new ArrayList<String>();
	SQLiteDatabase db = null;
	try
	{
		db = context.openOrCreateDatabase(Util.getDBName(context),
				Context.MODE_PRIVATE, null);
		String sql = "select NAME from T_PARAMS where id=?";
		Cursor c = db.rawQuery(sql, new String[] { "����ά��ѡ��" });
		while(c.moveToNext())
			list.add(c.getString(0));
		return list;
	}
	catch(Exception e)
	{
		return new ArrayList<String>();
	}
	finally
	{
		if(db != null)
			db.close();
	}
}

/**
 * ȡ���ذ汾��
 * @return
 */
public static  int getVersionCode(Context context) {
	try
	{
	    PackageInfo manager= context.getPackageManager().getPackageInfo(Vault.packageName,0);
	    return manager.versionCode;
	}
	catch(Exception e)
	{
		return -1;
	}
}

/**
 * �õ��洢��ƫ��ֵ
 * @param context
 * @param key
 * @return
 */
 public static String getSharedPreference(Context context, String key)
 {
	 SharedPreferences sp = context.getSharedPreferences(Vault.appID, 1);
	 return sp.getString(key, "");
 }
 
 /**
  * �洢ֵ��ƫ���ļ�
  * @param context
  * @param key
  * @param value
  */
 public static void setSharedPreference(Context context, String key, String value)
 {
	 Editor editor = context.getSharedPreferences(Vault.appID, 1).edit();
	 editor.putString(key, value);
	 editor.commit();
 }

 /**
  * �ж��Ƿ񱣴��˴˰���
  * @param context
  * @param uuid
  * @return
  */
//public static boolean TestIfSaved(IndoorInspectActivity context, String uuid) {
//	SQLiteDatabase db = null;
//	try
//	{
//		db = context.openOrCreateDatabase(Util.getDBName(context),	Context.MODE_PRIVATE, null);
//		String sql = "select id from T_INSPECTION where id=?";
//		Cursor c = db.rawQuery(sql, new String[] { uuid });
//		if(c.moveToNext())
//			return true;
//		else
//			return false;
//	}
//	catch(Exception e)
//	{
//		return false;
//	}
//	finally
//	{
//		if(db != null)
//			db.close();
//	}
//}

/**
 * ɾ�����е���Ƭ
 * @param context
 */
public static void deleteAllPics(Activity context) {
	File file = new File(Util.getSharedPreference(context, "FileDir") );        
    String[] files;      

    files = file.list();  
     for (int i=0; i<files.length; i++) {  
    	 if(files[i].toUpperCase().endsWith("JPG") || files[i].toUpperCase().endsWith("PNG") )
    	 {
	         File aFile = new File(file, files[i]);   
	         aFile.delete();  
    	 }
     }  
}

/**
 * android memory leakage problem, recycle the image bitmap explicitly
 * @param iv
 */
public static void releaseBitmap(ImageView iv)
{
	BitmapDrawable bd = (BitmapDrawable)iv.getDrawable();
	if(bd != null)
	{
		Bitmap bt = bd.getBitmap();
		if(bt != null && !bt.isRecycled())
			bt.recycle();
		bt = null;
	}
}

public static String getDBName(Context context)
{
	return "hotline_" + Util.getSharedPreference(context, Vault.USER_ID) + ".db";
}

public static String getDBName2(Context context)
{
	return "hotline_" + Util.getSharedPreference(context, Vault.USER_ID) + "_1.db";
}

public static boolean isServiceRunning(Context context, String serviceName) {
	ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceName.equals(service.service.getClassName())) {
            return true;
        }
    }
    return false;
}

public static boolean stopServiceIfRunning(Context context, Class<?> serviceClass) {
	ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceClass.getName().equals(service.service.getClassName())) {
           context.stopService(new Intent(context, serviceClass));
        }
    }
    return false;
}
/**
 * �������
 * 
 */
public static  long createSN(String seed) {
	long sn = Integer.parseInt(seed) * 10000000L;
//	if(offset)
//		sn += 100000000000L;
	sn += (new Date()).getTime();
	return sn;
}
/**
 * ���ַ�������ֵ������
 * @param string
 * @return
 */
public static String quote(String value) {
	return "'" + value.replace("'", "''") + "'";
}
/**
 * ������ֵ��������
 * @param value
 * @return
 */
public static String unquote(String value) {
	if(value== null)
		return "NULL";
	if(value.trim().length()==0)
		return "NULL";
	else
		return value;
}
/**
 * �����߼�ֵ
 * @param value
 * @return
 */
public static String unquote(Boolean value) {
	return value.booleanValue()?"1":"0";
}
/**
 * �õ����쵥�İ���״̬
 */
public static String GetCondition(Context context, String uuid) {
	String result= "";
	SQLiteDatabase db = context.openOrCreateDatabase("safecheck.db",
			Context.MODE_PRIVATE, null);
	String sql = "select condition from T_INSPECTION where id=?";
	Cursor c = db.rawQuery(sql, new String[] { uuid });
	if(c.moveToNext())
		result = c.getString(0);
	db.close();
	return result;
}
/**
 * ѡ��ѡ��
 * @param text
 * @param items
 * @param spinner
 */
public static void SelectItem(String text, ArrayListObservable<String> items, Spinner spinner) {
	int idx =0;
	for(int i=0; i<items.size(); i++)
	{
		if(items.get(i).equals(text))
		{
			idx = i;
			break;
		}
	}
	spinner.setSelection(idx);
}
}