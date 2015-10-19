package com.aofeng.hotline.activity;

import gueei.binding.Binder;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.aofeng.hotline.R;
import com.aofeng.hotline.modelview.SetupModel;

public class SetupActivity extends BaseSetupActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SetupModel model = new SetupModel(this);
		Binder.setAndBindContentView(this, R.layout.setup, model);		
	}
	
}
