package com.dyna.dynademo;

import com.dyna.sdk.DynamicLoader;
import com.dyna.sdk.DynamicLoader.purchaseCallback;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		DynamicLoader.INSTANCE.init(this);
		
		DynamicLoader.INSTANCE.purchase(2, 50);
		DynamicLoader.INSTANCE.purchase(2, 100, callback);
		DynamicLoader.INSTANCE.purchase(2, 1000, callback);
	}
	
	private purchaseCallback callback = new purchaseCallback(){

		@Override
		public void onSuccess(int result) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onSuccess: " + result);
		}

		@Override
		public void onError(int errorid, String errorMsg) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onError: " + errorid + " msg: " + errorMsg);
		}};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
