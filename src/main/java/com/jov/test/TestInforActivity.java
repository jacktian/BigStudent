package com.jov.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public class TestInforActivity extends Activity {
	private String userName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testinfor_set);
		initData();
	}

	public void initData() {
		Intent intent = getIntent();
		userName = intent.getStringExtra("user.name");
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			backTo();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void backTo() {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("user.name", userName);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
}
