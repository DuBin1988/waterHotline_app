package com.aofeng.hotline.activity;

import gueei.binding.Binder;
import gueei.binding.collections.ArrayListObservable;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.aofeng.hotline.R;
import com.aofeng.hotline.model.RepairSlipRowModel;
import com.aofeng.hotline.modelview.UploadModel;

public class UploadActivity extends Activity{
	public UploadModel model;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		model=new UploadModel(this);
		Binder.setAndBindContentView(this,R.layout.upload, model);
	}

	/**
	 *  on pressing the back button
	 */
	@Override
	public void onBackPressed() {
		//如果上传按钮未被按下，返回
		if(!model.cancelable)
			super.onBackPressed();
		else
			Toast.makeText(this, "上传进行中，请取消或等待上传结束。", Toast.LENGTH_SHORT).show();
	}
	@Override
	protected void onResume() {
		super.onResume();
		model.showNotUpload();
	}
	
}