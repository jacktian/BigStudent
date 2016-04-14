package com.jov.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

public class InforActivity extends Activity {


    private final int SPLASH_DISPLAY_LENGHT = 2000;
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_infor);  
        new Handler().postDelayed(new Runnable() {  
            public void run() {  
                Intent mainIntent = new Intent(InforActivity.this,  
                        MainActivity.class);  
                InforActivity.this.startActivity(mainIntent);  
                InforActivity.this.finish();  
            }  
        }, SPLASH_DISPLAY_LENGHT);  
    }  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
