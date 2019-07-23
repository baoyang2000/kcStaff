package com.ctrl.android.kcetong.listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorsManager {
	/* 线程池 */
	private static ExecutorService pool = null;

	public static  ExecutorService getInsence(){
		if(pool==null){
			pool =  Executors.newFixedThreadPool(8);
		}
		return pool;
	}
}
