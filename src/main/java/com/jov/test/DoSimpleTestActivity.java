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
			nextBtn.setText("���");
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
		// �����ǰҳ��������ȣ���ʾ���ʱ����ύ
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
				nextBtn.setText("���");
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
			temp ="������Ϊ����һ�����ߵġ����ʵġ�����Ѷϵģ��������չˡ���ԶҪ����Ϊ�����������������κ��»��κ����йء�������Ϊ����һ����������ߣ�һ����Զ���������ڵ�������ˡ���Щ����Ϊ�����˷�ζ��ֻ����Щ��֪�����֪���㲻���������ˡ�";
		}else if(result>=21&&result<=30){
			temp ="���������Ϊ������̿ࡢ�����ޡ�������Ϊ����һ�������ġ�ʮ��С�ĵ��ˣ�һ���������ȶ����ڹ������ˡ���������κγ嶯���»���׼�����£���������Ǵ��һ����������Ϊ���Ӹ����Ƕ���ϸ�ؼ��һ��֮���Ծ�������������������Ϊ��������ַ�Ӧһ��������Ϊ���С�ĵ�����������ġ�";
		}else if(result>=31&&result<=40){
			temp ="������Ϊ����һ�����ǡ�������ע��ʵЧ���ˡ�Ҳ��Ϊ����һ�����������츳�вŸ���ǫ����ˡ��㲻��ܿ졢�����׺��˳�Ϊ���ѣ�������һ�������ѷǳ��ҳϵ��ˣ�ͬʱҪ�����Ѷ���Ҳ���ҳϵĻر�����Щ�����л����˽�����˻�֪��Ҫ��ҡ������ѵ������Ǻ��ѵģ�����ȵģ�һ�������α��ƻ�����ʹ����Ѱ�����";
		}else if(result>=41&&result<=50){
			temp ="������Ϊ����һ�������˷ܵġ��߶Ȼ��õġ��൱�׳嶯�ĸ��ԣ�����һ�����������䡢һ����������ܿ���ˣ���Ȼ��ľ��������ǶԵġ� ������Ϊ���Ǵ󵨵ĺ�ð�յģ���Ը�������κ�������һ�Σ���һ��Ը�Ⳣ�Ի��������ð�յ��ˡ���Ϊ��ɢ���Ĵ̼�������ϲ��������һ��";
		}else if(result>60){
			temp ="������Ϊ������롸С�Ĵ������ڱ��˵����У������Ը��ġ��������ĵġ��Ǹ�������֧������ͳ�����ġ����˿��������㣬ϣ���ܶ�����һ�㣬��������Զ�����㣬����������������������������ԥ.���籾�����ǲ��Ƕ�ף��ܶ���ʼ�������κε���־���ı䡣";
		}
		dbHelper.insertTestResult("simple_test", temp, userName);
	}
}
