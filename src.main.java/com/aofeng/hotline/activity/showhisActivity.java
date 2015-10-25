package com.aofeng.hotline.activity;

import gueei.binding.Binder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aofeng.hotline.R;
import com.aofeng.hotline.modelview.MainModel;
import com.aofeng.hotline.modelview.showhisModel;
import com.aofeng.hotline.service.AlarmService;
import com.aofeng.hotline.service.ServiceManager;

public class showhisActivity extends Activity{
	showhisModel model;
    public ServiceManager service;
    Button exitserver;
    
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState){
		model = new showhisModel(this);
		Binder.setAndBindContentView(this, R.layout.showhismain, model);
		super.onCreate(savedInstanceState);
		/*
		if(service == null)
		{
			service = new ServiceManager(this, AlarmService.class, new Handler() {
	        	@Override
	        	public void handleMessage(Message msg) {
		            switch (msg.what) {
			            case AlarmService.MSG_TO_ACTIVITY_ALARM:           
			            	if(msg.arg1==-1)
			            	{
			            		Toast.makeText(showhisActivity.this, "后台运行服务未发现新工单。", Toast.LENGTH_LONG).show();
			            	}
			            	else
			            	{
			            		model.fillItem();
			            	}
			                break;
			            case AlarmService.MSG_TO_ACTIVITY_NETWORK_ERROR:      
		            		Toast.makeText(showhisActivity.this, "后台服务提取工单错误，请检查网络是否正常。", Toast.LENGTH_LONG).show();			            	
			            default:
			                super.handleMessage(msg);
		            } 
	        	}
	        });
		}
		exitserver =(Button)findViewById(R.id.exitserver);
		exitserver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				exitserver();
			}
		});
		*/
	}

	@Override
	protected void onResume() {
		super.onResume();
		//service.start();
		model.fillItem();
	}
	
	
    @Override
	protected void onPause() {
		super.onPause();
		//service.unbind();
	}

    /* */
	public void sendMessageToService(int msg) {
        try {          
            service.send(Message.obtain(null, msg, 0, 0));
        } 
        catch (RemoteException e) {
        }
    }
    
	@Override
	protected void onStop() {
		super.onStop();
		this.finish();
	}
	
	/**
	 * 退出程序,停止服务
	 */
		private void exitserver() {
			// 停止服务
			service.stop();
			//返回Home
			Intent startMaIn= new Intent(Intent.ACTION_MAIN);
			startMaIn.addCategory(Intent.CATEGORY_HOME);
			startMaIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMaIn);
			System.exit(0);
		}
	
}
