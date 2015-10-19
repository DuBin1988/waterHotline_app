package com.aofeng.utils;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class Player {
	
	SoundPool sp;
	private HashMap<Integer, Integer> soundsMap;
	
	/**
	 * 
	 * @param context
	 * @param rid 资源ID
	 */
	public Player(Context context, int rid)
	{
		sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		soundsMap = new HashMap<Integer, Integer>();
		soundsMap.put(1, sp.load(context, rid, 1));
	}

	/**
	 * 播放
	 * @param loop -1：一直连续播放
	 */
	public void play(int loop)
	{
		int soundID = soundsMap.get(1);
		sp.stop(soundID);
		int t = sp.play(soundID, 1, 1, 1, loop, 1f);
		
	}

	public void stop()
	{
		int soundID = soundsMap.get(1);
		sp.stop(soundID);
	}

}
