package com.aofeng.hotline.model;

import com.aofeng.hotline.modelview.UploadModel;

import gueei.binding.observables.StringObservable;


public class UploadRowModel {
	private UploadModel model;
	public UploadRowModel(UploadModel um) {
		this.model=um;
	}
	public StringObservable USERNAME = new StringObservable("");//用户姓名 
	public StringObservable PHONE = new StringObservable("");//用户地址 
	public StringObservable SENDER = new StringObservable("");//派单人
	public StringObservable CUCODE = new StringObservable("");//报修编号
	public StringObservable SENDTIME = new StringObservable("");//派单时间
}
