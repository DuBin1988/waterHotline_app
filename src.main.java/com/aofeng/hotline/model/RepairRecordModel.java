package com.aofeng.hotline.model;

import com.aofeng.hotline.R;
import com.aofeng.hotline.activity.RepairRecordActivity;

import gueei.binding.Command;
import android.app.Activity;
import android.view.View;

public class RepairRecordModel  extends ResultRowModel{
	public RepairRecordModel(Activity context) {
		super(context);
	}

	public Command save = new Command()
	{
		@Override
		public void Invoke(View view, Object... arg1) {
			RepairRecordActivity act = (RepairRecordActivity)mContext;
			act.findViewById(R.id.btnSave).setEnabled(false);
			if(!act.validate())
				return;
			act.doSave();
			act.findViewById(R.id.btnSave).setEnabled(true);
		}
		
	};
}
