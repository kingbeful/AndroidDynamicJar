package com.dyna.ali.sdk;

import android.content.Context;
import android.util.Log;

public class Alipay {
	private static final Alipay INSTANCE = new Alipay();
	
	private static final String TAG = "ALI";
	
	public static Alipay getInstance(Context context) {
		return INSTANCE;
	}
	public String getInfo() {
        return TAG;
    }
	
	public boolean pay(int rmb) {
		Log.i(TAG, "Pay RMB = " + rmb);
		return true;
	}
	
	public boolean payWithListener(int rmb, payListener listener) {
		Log.i(TAG, "Pay RMB = " + rmb);
		if (rmb > 1000) {
			listener.onError(rmb, "Error Message from 3rd-party sdk");
		} else {
			listener.onSuccess(rmb);
		}
		return true;
	}
	
	public interface payListener {
		public void onSuccess(int result);
		public void onError(int errorid, String errorMsg);
	}
}
