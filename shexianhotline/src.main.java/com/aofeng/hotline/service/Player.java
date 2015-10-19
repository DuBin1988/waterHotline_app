package com.aofeng.hotline.service;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class Player {

	SoundPool sp;
	private HashMap<Integer, Integer> soundsMap;
	boolean isLoaded = false;
	
	/**
	 * ²¥·Å
	 * awkward way to do playing, create a sound pool every time
	 */
	public void play(Context context, int rid)
	{
		if(sp == null)
		{
			isLoaded = false;
			sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
			soundsMap = new HashMap<Integer, Integer>();
			sp.setOnLoadCompleteListener(new OnLoadCompleteListener() {
				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
					isLoaded = true;
				}
			});
			soundsMap.put(1, sp.load(context, rid, 1));
		}
		//brutal way of waiting sound to load
		for(;!isLoaded;);				
		int soundID = soundsMap.get(1);
		sp.play(soundID, 1, 1, 1, -1, 1f);
	}

	public void stop()
	{
		if(sp != null)
		{
			sp.release();
			sp = null;
		}
	}

}
