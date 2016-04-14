package com.jov.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.jov.common.ResApplication;
import com.jov.common.StringUtil;
import com.jov.db.DBOpenHelper;
import com.jov.db.SimpleTest;

public class DoSimpleTestActivity extends Activity implements OnClickListener {
	private Chronometer timer;
	private DBOpenHelper dbHelper = new DBOpenHelper(this);;
	public static DoSimpleTestActivity instance = null;
	private SimpleTest sTest;
	private int pageNo = 1;
	private int total = 0;
	private TextView do_test_name;
	private RadioGroup answerGroup;
	private RadioButton answerABtn;
	private RadioButton answerBBtn;
	private RadioButton answerCBtn;
	private RadioButton answerDBtn;
	private RadioButton answerEBtn;
	private RadioButton answerFBtn;
	private RadioButton answerGBtn;
	private Button nextBtn;
	private int result=0; 
	private String userName = "";
	private AlertDialog menuDialog ;
	private TextView the_testing;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.do_simple_testing);
		instance = this;
		ResApplication.addActivity(instance);
		initData();
		initView();
	}

	public void initView() {
		timer = (Chronometer) this.findViewById(R.id.simple_chronometer);
		timer.setBase(SystemClock.elapsedRealtime());
		timer.start();
		do_test_name = (TextView) findViewById(R.id.do_simple_test_name);
		answerGroup = (RadioGroup) findViewById(R.id.simple_answerGroup);
		answerGroup.setOnCheckedChangeListener(new AnswerGroupChangeListener());
		answerABtn = (RadioButton) findViewById(R.id.simple_answerA);
		answerBBtn = (RadioButton) findViewById(R.id.simple_answerB);
		answerCBtn = (RadioButton) findViewById(R.id.simple_answerC);
		answerDBtn = (RadioButton) findViewById(R.id.simple_answerD);
		answerEBtn = (RadioButton) findViewById(R.id.simple_answerE);
		answerFBtn = (RadioButton) findViewById(R.id.simple_answerF);
		answerGBtn = (RadioButton) findViewById(R.id.simple_answerG);
		nextBtn = (Button) findViewById(R.id.simple_next_btn);
		initAll();
		nextBtn.setOnClickListener(this);
		if(total==1){
			nextBtn.setText("完成");
		}
		the_testing = (TextView) findViewById(R.id.simple_the_testing);
		the_testing.setText(userName);
	}

	public void initData() {
		total = dbHelper.getTotalST();
		sTest = dbHelper.getSimpleTest(String.valueOf((pageNo-1)));
		if (sTest == null) {
			sTest = new SimpleTest();
		}
		Intent intent = getIntent();
		userName = intent.getStringExtra("user.name");
		if(StringUtil.isEmpty(userName)){
			switchTo(MainActivity.class);
			return;
		}
	}
	public void initOther(){
		switch(sTest.getFlag()){
		case 3:
			answerDBtn.setVisibility(View.GONE);
			answerEBtn.setVisibility(View.GONE);
			answerFBtn.setVisibility(View.GONE);
			answerGBtn.setVisibility(View.GONE);
			break;
		case 4:
			answerDBtn.setVisibility(View.VISIBLE);
			answerEBtn.setVisibility(View.GONE);
			answerFBtn.setVisibility(View.GONE);
			answerGBtn.setVisibility(View.GONE);
			break;
		case 5:
			answerDBtn.setVisibility(View.VISIBLE);
			answerEBtn.setVisibility(View.VISIBLE);
			answerFBtn.setVisibility(View.GONE);
			answerGBtn.setVisibility(View.GONE);
			break;
		case 6:
			answerDBtn.setVisibility(View.VISIBLE);
			answerEBtn.setVisibility(View.VISIBLE);
			answerFBtn.setVisibility(View.VISIBLE);
			answerGBtn.setVisibility(View.GONE);
			break;
		case 7:
			answerDBtn.setVisibility(View.VISIBLE);
			answerEBtn.setVisibility(View.VISIBLE);
			answerFBtn.setVisibility(View.VISIBLE);
			answerGBtn.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
	public void initAll() {
		do_test_name.setText(sTest.getSubject());
		answerABtn.setText(sTest.getAnswerA());
		answerBBtn.setText(sTest.getAnswerB());
		answerCBtn.setText(sTest.getAnswerC());
		answerDBtn.setText(sTest.getAnswerD());
		answerEBtn.setText(sTest.getAnswerE());
		answerFBtn.setText(sTest.getAnswerF());
		answerGBtn.setText(sTest.getAnswerG());
		initOther();
	}
	@Override
	public void onClick(View v) {
		if (v == nextBtn) {
			nextTest();
		}
	}
	public void switchTo(Class clazz) {
		Intent intent = new Intent();
		intent.setClass(this, clazz);
		startActivity(intent);
		finish();
	}
	public void switchTo(Class clazz,String userName){
		Intent intent = new Intent();
		intent.setClass(this, clazz);
		Bundle bundle = new Bundle();
		bundle.putString("user.name",userName);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	public void nextTest() {
		// 如果当前页与总数相等，表示完成时候的提交
		if (pageNo == total) {
			calculatorResult();
			dbHelper.insertSetting(userName, "y");
        	switchTo(MainActivity.class,userName);
		} else {
			pageNo = (pageNo + 1) >= total ? total : (pageNo + 1);
			initData();
			initAll();
			answerGroup.clearCheck();
			nextBtn.setEnabled(false);
			if (pageNo == total) {
				nextBtn.setText("完成");
			}
		}
	}
	class AnswerGroupChangeListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup group, int arg1) {
			nextBtn.setEnabled(true);
			switch (group.getCheckedRadioButtonId()) {
			case R.id.simple_answerA:
				result +=sTest.getAnswerAScore();
				break;
			case R.id.simple_answerB:
				result +=sTest.getAnswerBScore();
				break;
			case R.id.simple_answerC:
				result +=sTest.getAnswerCScore();
				break;
			case R.id.simple_answerD:
				result +=sTest.getAnswerDScore();
				break;
			case R.id.simple_answerE:
				result +=sTest.getAnswerEScore();
				break;
			case R.id.simple_answerF:
				result +=sTest.getAnswerFScore();
				break;
			case R.id.simple_answerG:
				result +=sTest.getAnswerGScore();
				break;
			default:
				break;
			}
		}
	}
	public void showMenu(){
    	menuDialog.show();
		Window window = menuDialog.getWindow();  
		window.setGravity(Gravity.BOTTOM); 
		window.setContentView(R.layout.exit_dialog);
		WindowManager.LayoutParams  layparam= window.getAttributes();
		layparam.width = getWindowManager().getDefaultDisplay().getWidth(); 
		window.setAttributes(layparam);
		window.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {  
	        @Override  
	        public void onClick(View v) {  
	        	menuDialog.dismiss();
	     }});  
		window.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {  
	        @Override  
	        public void onClick(View v) {  
	        	menuDialog.dismiss();
	        	dbHelper.insertSetting(userName, "n");
	        	switchTo(MainActivity.class,userName);
	     }});  
    }
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			menuDialog = new AlertDialog.Builder(this).create();
			showMenu();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void calculatorResult() {
		String temp = "";
		if(result>0&&result<21){
			temp ="人们认为你是一个害羞的、神经质的、优柔寡断的，是须人照顾、永远要别人为你做决定、不想与任何事或任何人有关。他们认为你是一个杞人忧天者，一个永远看到不存在的问题的人。有些人认为你令人乏味，只有那些深知你的人知道你不是这样的人。";
		}else if(result>=21&&result<=30){
			temp ="你的朋友认为你勤勉刻苦、很挑剔。他们认为你是一个谨慎的、十分小心的人，一个缓慢而稳定辛勤工作的人。如果你做任何冲动的事或无准备的事，你会令他们大吃一惊。他们认为你会从各个角度仔细地检查一切之后仍经常决定不做。他们认为对你的这种反应一部分是因为你的小心的天性所引起的。";
		}else if(result>=31&&result<=40){
			temp ="别人认为你是一个明智、谨慎、注重实效的人。也认为你是一个伶俐、有天赋有才干且谦虚的人。你不会很快、很容易和人成为朋友，但是是一个对朋友非常忠诚的人，同时要求朋友对你也有忠诚的回报。那些真正有机会了解你的人会知道要动摇你对朋友的信任是很难的，但相等的，一旦这信任被破坏，会使你很难熬过。";
		}else if(result>=41&&result<=50){
			temp ="别人认为你是一个令人兴奋的、高度活泼的、相当易冲动的个性；你是一个天生的领袖、一个做决定会很快的人，虽然你的决定不总是对的。 他们认为你是大胆的和冒险的，会愿意试做任何事至少一次；是一个愿意尝试机会而欣赏冒险的人。因为你散发的刺激，他们喜欢跟你在一起。";
		}else if(result>60){
			temp ="别人认为对你必须「小心处理」。在别人的眼中，你是自负的、自我中心的、是个极端有支配欲、统治欲的。别人可能钦佩你，希望能多像你一点，但不会永远相信你，会对与你更深入的来往有所踌躇及犹豫.世界本来就是层层嵌套，周而复始；不以任何的意志而改变。";
		}
		dbHelper.insertTestResult("simple_test", temp, userName);
	}
}
