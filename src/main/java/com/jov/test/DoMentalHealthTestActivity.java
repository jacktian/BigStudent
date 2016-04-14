package com.jov.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.jov.common.ApplicationControl;
import com.jov.common.FactorControl;
import com.jov.common.MapSortByValue;
import com.jov.common.ResApplication;
import com.jov.common.StringUtil;
import com.jov.common.TestResultCalculator;
import com.jov.db.DBOpenHelper;
import com.jov.db.Factory;
import com.jov.db.MentalHealth;
import com.jov.db.User;

public class DoMentalHealthTestActivity extends Activity implements OnClickListener {
	private Chronometer timer;
	private DBOpenHelper dbHelper = new DBOpenHelper(this);;
	public static DoMentalHealthTestActivity instance = null;
	private MentalHealth mHealth;
	private int pageNo = 1;
	private int total = 0;
	private TextView do_test_name;
	private RadioGroup answerGroup;
	private RadioButton answerABtn;
	private RadioButton answerBBtn;
	private RadioButton answerCBtn;
	private TextView test_done_num;
	private Button nextBtn;
	private Map<String, Integer> scoreMap = new HashMap<String, Integer>();
	private Map<String, Integer> factoryMap = new HashMap<String, Integer>();
	private Map<String, Integer> appAbilityMap = new HashMap<String, Integer>();
	private Map<String, Integer> adaptionMap = new HashMap<String, Integer>();
	private List<Factory> factorys = new ArrayList<Factory>();
	private String userName = "";
	private AlertDialog menuDialog ;
	private TextView the_testing;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.do_mental_testing);
		instance = this;
		ResApplication.addActivity(instance);
		initData();
		initView();
	}

	public void initView() {
		timer = (Chronometer) this.findViewById(R.id.chronometer);
		timer.setBase(SystemClock.elapsedRealtime());
		timer.start();
		do_test_name = (TextView) findViewById(R.id.do_test_name);
		do_test_name.setText(mHealth.getId() + "." + mHealth.getSubject());
		answerGroup = (RadioGroup) findViewById(R.id.answerGroup);
		answerGroup.setOnCheckedChangeListener(new AnswerGroupChangeListener());
		answerABtn = (RadioButton) findViewById(R.id.answerA);
		answerABtn.setText(mHealth.getAnswerA());
		answerBBtn = (RadioButton) findViewById(R.id.answerB);
		answerBBtn.setText(mHealth.getAnswerB());
		answerCBtn = (RadioButton) findViewById(R.id.answerC);
		answerCBtn.setText(mHealth.getAnswerC());
		test_done_num = (TextView) findViewById(R.id.test_done_num);
		test_done_num.setText("F" + (pageNo + 1) + "-S" + (total - pageNo - 1));
		nextBtn = (Button) findViewById(R.id.next_btn);
		nextBtn.setOnClickListener(this);
		if(total==1){
			nextBtn.setText("���");
		}
		the_testing = (TextView) findViewById(R.id.the_testing);
		the_testing.setText(userName);
	}

	public void initData() {
		total = dbHelper.getTotalMH();
		mHealth = dbHelper.getMentalHealth(String.valueOf((pageNo-1)));
		if (mHealth == null) {
			mHealth = new MentalHealth();
		}
		Intent intent = getIntent();
		userName = intent.getStringExtra("user.name");
		if(StringUtil.isEmpty(userName)){
			switchTo(MainActivity.class);
			return;
		}
	}

	public void initAll() {
		do_test_name.setText(mHealth.getId() + "." + mHealth.getSubject());
		answerABtn.setText(mHealth.getAnswerA());
		answerBBtn.setText(mHealth.getAnswerB());
		answerCBtn.setText(mHealth.getAnswerC());
		test_done_num.setText("F" + (pageNo) + "-S" + (total - pageNo ));
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
			int result = 0;
			switch (group.getCheckedRadioButtonId()) {
			case R.id.answerA:
				result = scoreMap.get(mHealth.getFactorId()) == null ? 0
						: (scoreMap.get(mHealth.getFactorId()) + mHealth
								.getAnswerAScore());
				scoreMap.put(mHealth.getFactorId(), result);
				break;
			case R.id.answerB:
				result = scoreMap.get(mHealth.getFactorId()) == null ? 0
						: (scoreMap.get(mHealth.getFactorId()) + mHealth
								.getAnswerBScore());
				scoreMap.put(mHealth.getFactorId(), result);
				break;
			case R.id.answerC:
				result = scoreMap.get(mHealth.getFactorId()) == null ? 0
						: (scoreMap.get(mHealth.getFactorId()) + mHealth
								.getAnswerCScore());
				scoreMap.put(mHealth.getFactorId(), result);
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
		User user = dbHelper.getUserByName(userName);
		if(user==null){
			switchTo(MainActivity.class);
			return;
		}
		if(user.getSex().equals("��")){
			factoryMap = FactorControl.calculate(scoreMap, 1);
		}else{
			factoryMap = FactorControl.calculate(scoreMap, 2);
		}
		Factory fac = null;
		int s = factoryMap.get("A");
		String des = "";
		if (s == 1 || s == 2) {
			des = "�¶�����Ĭ����Ը����������ϲ������������Ϊ����ʡ�����·ǳ��Ͻ��������ң������׷����Լ���������";
		} else if (s == 3 || s == 4) {
			des = "�ȽϹ¶�����Ĭ����̫Ը�������������ϲ���������������±Ƚ��Ͻ��������׷����Լ���������";
		} else if (s == 7 || s == 8) {
			des = "Ϊ�˱Ƚ����顢��Ⱥ��ϲ�����˺�����������Ӧ������ǿ������ �Ƚ����׽��ܱ��˵������ͽ��顣";
		} else if (s == 9 || s == 10) {
			des = "Ϊ�˷ǳ����顢��Ⱥ����������Ӧ������ǿ��ϲ���μӼ��� �������ƽϣ����׽��ܱ��˵�����������Ƽˮ���Ҳ��һ����ʡ�";
		} else {
			des = "5-6��Ϊ����";
		}
		fac = new Factory();
		fac.setItem("��Ⱥ��");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("B");
		if (s == 1 || s == 2) {
			des = "�������ǿ������˼ά���������������ȶ���";
		} else if (s == 3 || s == 4) {
			des = "ѧϰ���˽�������̫ǿ����̫���ڳ���˼������ʱ������̫�ȶ���";
		} else if (s == 7 || s == 8) {
			des = "�Ƚ����ڳ���˼����ѧϰ������ǿ��˼ά����׼ȷ��";
		} else if (s == 9 || s == 10) {
			des = "���������в��ǣ����»��������ڳ���˼����˼ά������ȷ���ܾ�һ��������";
		} else {
			des = "5-6��Ϊ����";
		}
		fac = new Factory();
		fac.setItem("�ϻ���");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("C");
		if (s == 1 || s == 2) {
			des = "�������ȶ����������ա����ܻ���֧�������ҡ���������������ʵ��ʱʱ�ἱ�겻��������ƣ��������ʧ�ߡ����Ρ�";
		} else if (s == 3 || s == 4) {
			des = "��ʱ������̫�ȶ������ܺܺõ�Ӧ�������������ĸ��ִ��۴����ͨ����������˳�ܡ�";
		} else if (s == 7 || s == 8) {
			des = "�����Ƚ��ȶ������죬�������ʵ��ͨ�����Գ��ŵ�̬��Ӧ����ʵ�еĸ������⣬�ж���������������������";
		} else if (s == 9 || s == 10) {
			des = "�����ȶ������죬�������ʵ�����Գ��ŵ�̬��Ӧ����ʵ�еĸ������⣬�ж��������������ڲ��ܳ��׽�������⣬Ҳ����ǿ�� ��⡣";
		} else {
			des = "5-6��Ϊ����";
		}
		fac = new Factory();
		fac.setItem("�ȶ���");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("E");
		if (s == 1 || s == 2) {
			des = "��Ϊ��˳��ǫѷ��˳�ӣ�ӭ�ϱ��˵���ּ���£��ҳ������²����˵�����������";
		} else if (s == 3 || s == 4) {
			des = "ͨ���Ƚ���˳��˳�ӣ���ʱû���Լ�����������ӭ�ϱ��˵���ּ ���¡�";
		} else if (s == 7 || s == 8) {
			des = "�Ը�ȽϺ�ǿ�����±Ƚ϶�����������ʱ�������ߣ�����Ϊ�ǡ�";
		} else if (s == 9 || s == 10) {
			des = "��ǿ��ִ������������ͨ���������ߣ�����Ϊ�ǣ�����������϶�ʱ��ָʹ���������ˣ��ͷ�����Ȩ���ߡ�";
		} else {
			des = "5-6��Ϊ����";
		}
		fac = new Factory();
		fac.setItem("��ǿ��");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("F");
		if (s == 1 || s == 2) {
			des = "���ࡢ�������侲�����ԣ��ж��н�����ʡ�������׷��ԣ����������ȣ���ʱ���ܹ�����˼���ǣ��ֽ��������������ڹ����ϳ�������ɿ��Ĺ�����Ա��";
		} else if (s == 3 || s == 4) {
			des = "�Ƚ����ࡢ�������侲�����ԣ��ж��ȽϾн��������׷��ԣ����������ȣ�ͨ��������ɿ��Ĺ�����Ա��";
		} else if (s == 7 || s == 8) {
			des = "�Ƚ������˷ܡ����ý�̸�����˶��±Ƚ����ġ�";
		} else if (s == 9 || s == 10) {
			des = "�����˷ܡ����ý�̸���������������˶��·ǳ����ģ���ʱ���� ���ֳ嶯��������Ϊ���Ī�⡣";
		} else {
			des = "5-6��Ϊ����";
		}
		fac = new Factory();
		fac.setItem("�˷���");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("G");
		if (s == 1 || s == 2) {
			des = "���ҷ��ܣ�ȱ����ط��ľ��񣻶����û�о��Ե����θУ�������ʱ��ϧ֪�������������ֶ��ԴﵽĳһĿ�ģ���ˣ�������Ч�ؽ��ʵ�����⣬�������˷�ʱ���뾫����";
		} else if (s == 3 || s == 4) {
			des = "���²������渺��û�о��Ե����θУ���ʱ���Ĵ��⣬������ ʼ���ա�";
		} else if (s == 7 || s == 8) {
			des = "���±Ƚ����渺��ϸ���ܵ�����ʼ���ա�";
		} else if (s == 9 || s == 10) {
			des = "�������渺��ϸ���ܵ�����ʼ���գ���ѭ�Ƿ��ƶ����Ϊ��׼�����ύ������Ҳ��ϵŬ����ɵ��ˡ�";
		} else {
			des = "5-6��Ϊ����";
		}
		fac = new Factory();
		fac.setItem("�к���");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("H");
		if (s == 1 || s == 2) {
			des = "η��������ȱ�������ģ���ǿ�ҵ��Ա��У�����Ⱥ�����ӡ�����Ȼ��׾�ڷ��ԣ�����Ը��İ���˽�̸�����¹��������ڹ��ֵ�������ʶ��������������ϵ���Ҫ��������";
		} else if (s == 3 || s == 4) {
			des = "���±Ƚ�η��������ȱ���㹻�������ģ�׾�ڷ��ԣ���̫Ը����İ���˽�̸����ʱ���Ա��У�������ʶ��ǿ��";
		} else if (s == 7 || s == 8) {
			des = "���±Ƚ�ð�ո�Ϊ�����й˼ɡ�";
		} else if (s == 9 || s == 10) {
			des = "ð�ո�Ϊ�����й˼ɣ�ͨ�������Σ���η�����и�����Ϊ�ľ������������������ָ������������ʱ���ܴ��Ĵ��⣬����ϸ�ڡ�";
		} else {
			des = "5-6��Ϊ����";
		}
		fac = new Factory();
		fac.setItem("��Ϊ��");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("I");
		if (s == 1 || s == 2) {
			des = "���ǡ�������ʵ���������������Կ͹ۡ���ǿ��������̬�ȴ�����ʵ���⣻���ظ���Ҳ���������¡�";
		} else if (s == 3 || s == 4) {
			des = "���±Ƚ����ǡ��͹ۡ���ʵ�������׸������¡�";
		} else if (s == 7 || s == 8) {
			des = "�Ƚ��ظ��飬�ĳ������ܰ������գ�����ʱ������ʵ��ȱ����������ġ�";
		} else if (s == 9 || s == 10) {
			des = "ͨ���ĳ������ܸж������ܰ������գ���ʱ��̫��ʵ��ȱ����������ģ��䲻��ʵ�ʵĿ������ж������ͼ���Ĺ���Ч�ʡ�";
		} else {
			des = "5-6��Ϊ����";
		}
		fac = new Factory();
		fac.setItem("������");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		s = factoryMap.get("L");
		if (s == 1 || s == 2) {
			des = "������ͣ��������ദ���޲¼ɣ������˽�������˳Ӧ���������������ˡ�";
		} else if (s == 3 || s == 4) {
			des = "Ϊ�˱Ƚ�������ͣ������ദ��ͨ���޲¼ɣ������ദ���ȽϺ��������������ˡ�";
		} else if (s == 7 || s == 8) {
			des = "�����ദ���������ġ����ɡ��������ˣ���ʱ���ƽϣ���ִ������";
		} else if (s == 9 || s == 10) {
			des = "���ɡ���㹡���ִ�����������α��ˣ������ദ�������ƽϣ���������˵����档";
		} else {
			des = "5-6��Ϊ����";
		}
		fac = new Factory();
		fac.setItem("������");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		s = factoryMap.get("M");
		if (s == 1 || s == 2) {
			des = "��ʵ���Ϻ��ɹ棬�������ƺ����Ӳ�³ç���£��ڽ�Ҫ�ؼ�ʱ�̣�Ҳ�ܱ����򾲣���ʱ���ܹ���������ʵ��Ϊ����Ȼ��Ȥ��";
		} else if (s == 3 || s == 4) {
			des = "Ϊ�˱Ƚ���ʵ��ͨ����������ʵ�������������ȡ�ᣬһ�㲻³ç���¡�";
		} else if (s == 7 || s == 8) {
			des = "�������Ϸḻ������������ϸ�ڣ����¶��Ա���������Ȥ����������Ϊ�����㣻���ܸ��д�����������ʱ��̫��ʵ��";
		} else if (s == 9 || s == 10) {
			des = "���������ḻ�������룬��Ų������������ϸ�ڣ�ֻ�Ա���������Ȥ����������Ϊ��Ϊ�ĳ����㣻���ܸ��д�����������ʱ���ֲ���ʵ�ʣ������嶯��";
		} else {
			des = "5-6��Ϊ����";
		}
		fac = new Factory();
		fac.setItem("������");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("N");
		if (s == 1 || s == 2) {
			des = "̹�ס�ֱ�ʡ����棻�����������������裻ͨ��˼��򵥣��Ե����ɡ����桢��׾���ƺ�ȱ��������";
		} else if (s == 3 || s == 4) {
			des = "Ϊ�˱Ƚ�̹�ס�ֱ�ʡ����棻ͨ�������������������㣻����ʱ˼��򵥣��Ե����ɡ�";
		} else if (s == 7 || s == 8) {
			des = "Ϊ�˱ȽϾ��������ʣ���Ϊ�ϵ��壻�����ǡ��͹۵ؿ���������侲�ؽ��з�����";
		} else if (s == 9 || s == 10) {
			des = "�����ܸɡ��������������ʣ���Ϊ���壻���侲�ķ���һ�У������ƻ�����һ������Ŀ��������ǵġ��͹۵ġ�";
		} else {
			des = "5-6��Ϊ����";
		}
		fac = new Factory();
		fac.setItem("������");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("O");
		if (s == 1 || s == 2) {
			des = "������š��������ġ������׶�ҡ�������Լ���Ӧ��������������а�ȫ�У�����Ӧԣ�硣";
		} else if (s == 3 || s == 4) {
			des = "�Ƚ����š����ꡢ���ţ��Ƚ��а�ȫ�У�����Ӧ��ǰ������";
		} else if (s == 7 || s == 8) {
			des = "�Ƚ������������������գ���ʱ��ɥ���ۣ�ȱ�����˽ӽ���������";
		} else if (s == 9 || s == 10) {
			des = "�����������������ţ����������������������������³��˾ţ�������ɥ���ۣ�ʱʱ�л��û�ʧ֮�У��Ծ��������ˣ�Ҳȱ�����˽ӽ���������";
		} else {
			des = "5-6��Ϊ����";
		}
		fac = new Factory();
		fac.setItem("������");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("Q1");
		if (s == 1 || s == 2) {
			des = "˼��ǳ����ء���ͳ��ī�سɹ棬��Ը����̽���µľ��磻�����ҷ�����˼�����±䶯�����ܱ���Ϊ����̻�ʱ���������ߡ�";
		} else if (s == 3 || s == 4) {
			des = "˼��Ƚϱ��ء����ش�ͳ��������Ϊ��׼����Ը����̽���µľ��硣";
		} else if (s == 7 || s == 8) {
			des = "˼�����Ƚ����ɿ��ţ�����������ʵ���������ж��Ƿǣ����׽��ܸ��Ƚ���˼������Ϊ��";
		} else if (s == 9 || s == 10) {
			des = "���ɼ����ģ���������������ʵ��ϲ������һ����������ʵ���������ж��Ƿǣ�ϣ���˽���Ƚ���˼������Ϊ�����ܹ�����ţ����Ϸḻ��ʵ���������顣";
		} else {
			des = "5-6��Ϊ����";
		}
		fac = new Factory();
		fac.setItem("ʵ����");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("Q2");
		if (s == 1 || s == 2) {
			des = "���������ڸ��ϣ���Ը�������У������������������������飬��ȡ�ñ��˵ĺøУ���Ҫ�����֧����ά���������ģ�ȴ������������Ⱥ�ߡ�";
		} else if (s == 3 || s == 4) {
			des = "�����Խ�ǿ�����˹�ͬ����ʱ����������������������������У���Ҫ�����֧����ά���������ġ�";
		} else if (s == 7 || s == 8) {
			des = "�����Խ�ǿ���ܶ�������Լ��Ĺ����ƻ������������ˣ�һ�㲻��������۵�Լ����";
		} else if (s == 9 || s == 10) {
			des = "��ǿ�������������ϣ����������ţ���������Լ��Ĺ����ƻ������������ˣ�Ҳ����������۵�Լ����ͬ��Ҳ������ƻ�֧����ˣ����Ӷ��ˣ�Ҳ����Ҫ���˵ĺøС�";
		} else {
			des = "5-6��Ϊ����";
		}
		fac = new Factory();
		fac.setItem("������");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("Q3");
		if (s == 1 || s == 2) {
			des = "ì�ܳ�ͻʱ�����ܿ����Լ������������ף������Ǳ��˵���Ҫ������ì�ܣ�ȴ�޷����ѣ���������Ӧ�ϳ������⡣";
		} else if (s == 3 || s == 4) {
			des = "ì�ܳ�ͻʱ�����ҿ��������ϲ���׳嶯��";
		} else if (s == 7 || s == 8) {
			des = "���ҿ���������ǿ��ͨ���ܺ����֧���Լ��ĸ����ж���Ϊ�˴��£��ܱ��������𣬲�Ӯ�ñ��˵����ء�";
		} else if (s == 9 || s == 10) {
			des = "֪��֪�ˡ������Ͻ���ͨ������һ�£��ܹ������֧���Լ��ĸ����ж���Ϊ�˴��£����ܱ��������𣬲�Ӯ�ñ��˵����أ���ʱ����̫��ִ�ɼ���";
		} else {
			des = "5-6��Ϊ����";
		}
		fac = new Factory();
		fac.setItem("������");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("Q4");
		if (s == 1 || s == 2) {
			des = "��ƽ���͡���ɢ������֪�㳣�֣�Ҳ���ܹ���������ȱ����ȡ�ġ�";
		} else if (s == 3 || s == 4) {
			des = "��ɢ������֪�㳣�֣��ܱ������ĵ�ƽ�⡣";
		} else if (s == 7 || s == 8) {
			des = "��ʱ�������š��������������׼��꣬ȱ�����ģ���ʱ�о�ƣ�������񲻶����޷����ѷ�������ƽ����";
		} else if (s == 9 || s == 10) {
			des = "�����������š�ȱ�����ġ����񲻶������������˷ܶ��о�ƣ�������޷����װ�������ƽ����������У���һ�����ﶼȱ�����ÿ������սս���������������ѡ�";
		} else {
			des = "5-6��Ϊ����";
		}
		fac = new Factory();
		fac.setItem("������");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		dbHelper.deleteFactory(userName);
		for (Factory temp : factorys) {
			dbHelper.insertFactory(temp, userName);
		}
		appAbilityMap = ApplicationControl.calculate(factoryMap);
		Set<String> applicationSet = appAbilityMap.keySet();
		Iterator<String> applicationIt = applicationSet.iterator();
		dbHelper.deleteAppAbility(userName);
		while (applicationIt.hasNext()) {
			String name = applicationIt.next();
			int score = appAbilityMap.get(name);
			dbHelper.insertAppAbility(name, String.valueOf(score), userName);
		}
		adaptionMap = TestResultCalculator.calculate(factoryMap);
		Set<String> adaptionSet = adaptionMap.keySet();
		Iterator<String> adaptionIt = adaptionSet.iterator();
		List arrayList = new ArrayList(adaptionMap.entrySet());
		Collections.sort(arrayList, new MapSortByValue());
		String job="";
		Map<String,String> map = new HashMap<String,String>();
		map.put("������","����ܹ�ʦ��������ʦ�������������ʦ��������Թ���ʦ������ĵ�����ʦ��");
		map.put("������","�������ʦ�������Чʦ��");
		map.put("������","�����ѵ��ʦ��");
		map.put("������","�㷨����ʦ��ϵͳ�ܹ�ʦ��ϵͳ����ʦ��");
		map.put("������","����ͻ����������Ŀ���ʣ�Call Center����ʦ�����ά������ʦ��");
		map.put("������","�����Ŀ������Ŀ������ʦ��");
		map.put("�罻��","������۹���ʦ��������й���ʦ�����ά������ʦ��");
		for(int i = 0;i<3;i++){
			Map.Entry entry = (Map.Entry)arrayList.get(i);
			job+=map.get(entry.getKey());
		}
		if(StringUtil.isEmpty(dbHelper.getAdaptionByUId(userName))){
			dbHelper.insertAdaption(job, userName);
		}else{
			dbHelper.updateNote(job, userName);
		}
	}
}
