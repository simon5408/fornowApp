/**
 * 
 */
package com.fornow.app.ui.shopcart;

import com.fornow.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * @author Simon Lv
 * 
 */
public class JieSuanSuccessActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jiesuan_success);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public void softBack(View v) {
		this.finish();
	}

}
