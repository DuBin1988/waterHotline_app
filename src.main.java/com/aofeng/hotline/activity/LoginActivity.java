package com.aofeng.hotline.activity;

import gueei.binding.Binder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.aofeng.hotline.R;
import com.aofeng.hotline.modelview.LoginModel;
import com.aofeng.utils.Util;
import com.aofeng.utils.Vault;

public class LoginActivity extends Activity {
	private LoginModel model;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		model = new LoginModel(this);
		Binder.setAndBindContentView(this, R.layout.login, model);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// ���õ�¼��ť����
		try{
		((Button) findViewById(R.id.buttons)).setEnabled(true);
		}catch(Exception e){
			System.out.println("1111");
		}
		// ����û���Ϣ
		SharedPreferences sp = getSharedPreferences(Vault.appID,LoginModel.SAFE_CHECK_MODEL);
		if(sp.getString(Vault.USER_NAME, "").length()>0)
			model.Name.set(sp.getString(Vault.USER_NAME, ""));
		((TextView)findViewById(R.id.appVersion)).setText("��ǰ�汾�ţ�" +Util.getVersionCode(this));
	}
}
