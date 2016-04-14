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
			nextBtn.setText("完成");
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
		if(user.getSex().equals("男")){
			factoryMap = FactorControl.calculate(scoreMap, 1);
		}else{
			factoryMap = FactorControl.calculate(scoreMap, 2);
		}
		Factory fac = null;
		int s = factoryMap.get("A");
		String des = "";
		if (s == 1 || s == 2) {
			des = "孤独、缄默，不愿与人来往，喜欢独立工作；为人自省，做事非常严谨而不苟且，不轻易放弃自己的主见。";
		} else if (s == 3 || s == 4) {
			des = "比较孤独、缄默，不太愿意多与人来往，喜欢独立工作；做事比较严谨，不轻易放弃自己的主见。";
		} else if (s == 7 || s == 8) {
			des = "为人比较热情、乐群，喜欢与人合作共事且适应能力较强；遇事 比较容易接受别人的批评和建议。";
		} else if (s == 9 || s == 10) {
			des = "为人非常热情、乐群；合作与适应能力特强，喜欢参加集体活动 ；不斤斤计较，容易接受别人的批评；甚至萍水相逢也可一见如故。";
		} else {
			des = "5-6分为中性";
		}
		fac = new Factory();
		fac.setItem("乐群性");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("B");
		if (s == 1 || s == 2) {
			des = "理解力不强，抽象思维能力弱，情绪不稳定。";
		} else if (s == 3 || s == 4) {
			des = "学习与了解能力不太强，不太善于抽象思考，有时情绪不太稳定。";
		} else if (s == 7 || s == 8) {
			des = "比较善于抽象思考，学习能力较强，思维敏捷准确。";
		} else if (s == 9 || s == 10) {
			des = "聪明、富有才智，遇事机警，善于抽象思考，思维敏捷正确，能举一而反三。";
		} else {
			des = "5-6分为中性";
		}
		fac = new Factory();
		fac.setItem("聪慧性");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("C");
		if (s == 1 || s == 2) {
			des = "情绪不稳定、易生烦恼、易受环境支配而心神动摇不定；不能面对现实而时时会急躁不安，身心疲惫，甚至失眠、多梦。";
		} else if (s == 3 || s == 4) {
			des = "有时情绪不太稳定，不能很好地应付生活中遇到的各种挫折打击，通常不能逆来顺受。";
		} else if (s == 7 || s == 8) {
			des = "情绪比较稳定、成熟，能面对现实；通常能以沉着的态度应付现实中的各种问题，行动有魄力，能振作勇气。";
		} else if (s == 9 || s == 10) {
			des = "情绪稳定、成熟，能面对现实；能以沉着的态度应付现实中的各种问题，行动充满魄力，对于不能彻底解决的难题，也善于强自 宽解。";
		} else {
			des = "5-6分为中性";
		}
		fac = new Factory();
		fac.setItem("稳定性");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("E");
		if (s == 1 || s == 2) {
			des = "行为温顺、谦逊、顺从，迎合别人的意旨行事，且常有事事不如人的消极情绪。";
		} else if (s == 3 || s == 4) {
			des = "通常比较温顺、顺从；有时没有自己的主见，而迎合别人的意旨 行事。";
		} else if (s == 7 || s == 8) {
			des = "性格比较好强、做事比较独立积极，有时自视甚高，自以为是。";
		} else if (s == 9 || s == 10) {
			des = "好强固执、独立积极，通常自视甚高，自以为是；可能行事武断而时常指使不及他的人，和反抗有权势者。";
		} else {
			des = "5-6分为中性";
		}
		fac = new Factory();
		fac.setItem("恃强性");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("F");
		if (s == 1 || s == 2) {
			des = "严肃、审慎、冷静、寡言；行动拘谨，内省而不轻易发言，较消极沉稳；有时可能过分深思熟虑，又近乎骄傲自满；在工作上常是认真可靠的工作人员。";
		} else if (s == 3 || s == 4) {
			des = "比较严肃、审慎、冷静、寡言；行动比较拘谨，不轻易发言，较消极沉稳；通常是认真可靠的工作人员。";
		} else if (s == 7 || s == 8) {
			des = "比较轻松兴奋、活泼健谈；对人对事比较热心。";
		} else if (s == 9 || s == 10) {
			des = "轻松兴奋、活泼健谈、随遇而安；对人对事非常热心；有时可能 过分冲动，以致行为变幻莫测。";
		} else {
			des = "5-6分为中性";
		}
		fac = new Factory();
		fac.setItem("兴奋性");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("G");
		if (s == 1 || s == 2) {
			des = "苟且敷衍，缺乏奉公守法的精神；对社会没有绝对的责任感，甚至有时不惜知法犯法，不择手段以达到某一目的；因此，常能有效地解决实际问题，而无须浪费时间与精力。";
		} else if (s == 3 || s == 4) {
			des = "做事不够认真负责，没有绝对的责任感；有时粗心大意，不能有 始有终。";
		} else if (s == 7 || s == 8) {
			des = "做事比较认真负责，细心周到，有始有终。";
		} else if (s == 9 || s == 10) {
			des = "做事认真负责，细心周到，有始有终；遵循是非善恶的行为标准，所结交的朋友也多系努力苦干的人。";
		} else {
			des = "5-6分为中性";
		}
		fac = new Factory();
		fac.setItem("有恒性");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("H");
		if (s == 1 || s == 2) {
			des = "畏怯退缩，缺乏自信心，有强烈的自卑感；在人群中羞怯、不自然，拙于发言，更不愿与陌生人交谈；凡事观望，由于过分的自我意识，往往忽视社会上的重要事物与活动。";
		} else if (s == 3 || s == 4) {
			des = "做事比较畏怯退缩，缺乏足够的自信心；拙于发言，不太愿意与陌生人交谈；有时有自卑感，自我意识较强。";
		} else if (s == 7 || s == 8) {
			des = "做事比较冒险敢为、少有顾忌。";
		} else if (s == 9 || s == 10) {
			des = "冒险敢为、少有顾忌；通常不掩饰，不畏缩，有敢作敢为的精神，能历经艰辛而保持刚毅的毅力；有时可能粗心大意，忽视细节。";
		} else {
			des = "5-6分为中性";
		}
		fac = new Factory();
		fac.setItem("敢为性");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("I");
		if (s == 1 || s == 2) {
			des = "理智、着重现实、自恃其力，多以客观、坚强、独立的态度处理现实问题；不重感情也不感情用事。";
		} else if (s == 3 || s == 4) {
			des = "做事比较理智、客观、现实；不容易感情用事。";
		} else if (s == 7 || s == 8) {
			des = "比较重感情，心肠软；可能爱好文艺；但有时不够务实，缺乏耐性与恒心。";
		} else if (s == 9 || s == 10) {
			des = "通常心肠软、易受感动；可能爱好文艺；有时不太务实，缺乏耐心与恒心，其不着实际的看法与行动常降低集体的工作效率。";
		} else {
			des = "5-6分为中性";
		}
		fac = new Factory();
		fac.setItem("敏感性");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		s = factoryMap.get("L");
		if (s == 1 || s == 2) {
			des = "信赖随和，易与人相处；无猜忌，不与人角逐竞争；顺应合作，善于体贴人。";
		} else if (s == 3 || s == 4) {
			des = "为人比较信赖随和，容易相处；通常无猜忌；与人相处，比较合作，能体贴别人。";
		} else if (s == 7 || s == 8) {
			des = "与人相处，常常多心、怀疑、不相信人；有时斤斤计较，固执己见。";
		} else if (s == 9 || s == 10) {
			des = "怀疑、刚愎、固执己见，不信任别人；与人相处，常斤斤计较，不顾念别人的利益。";
		} else {
			des = "5-6分为中性";
		}
		fac = new Factory();
		fac.setItem("怀疑性");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		s = factoryMap.get("M");
		if (s == 1 || s == 2) {
			des = "现实，合乎成规，力求妥善合理，从不鲁莽从事；在紧要关键时刻，也能保持镇静；有时可能过分重视现实，为人索然寡趣。";
		} else if (s == 3 || s == 4) {
			des = "为人比较现实，通常先斟酌现实条件，而后决定取舍，一般不鲁莽从事。";
		} else if (s == 7 || s == 8) {
			des = "想象力较丰富，常忽视生活细节，处事多以本身动机、兴趣等主观因素为出发点；可能富有创作力，但有时不太务实。";
		} else if (s == 9 || s == 10) {
			des = "想象力极丰富，爱幻想，狂放不羁；常忽视生活细节，只以本身动机、兴趣等主观因素为行为的出发点；可能富有创作力，但有时过分不务实际，近乎冲动。";
		} else {
			des = "5-6分为中性";
		}
		fac = new Factory();
		fac.setItem("幻想性");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("N");
		if (s == 1 || s == 2) {
			des = "坦白、直率、天真；与人无争，与世无忤；通常思想简单，显得幼稚、天真、笨拙，似乎缺乏教养。";
		} else if (s == 3 || s == 4) {
			des = "为人比较坦白、直率、天真；通常与世无争，心满意足；但有时思想简单，显得幼稚。";
		} else if (s == 7 || s == 8) {
			des = "为人比较精明、世故，行为较得体；能理智、客观地看待事物，并冷静地进行分析。";
		} else if (s == 9 || s == 10) {
			des = "精明能干、处事老辣、世故，行为得体；能冷静的分析一切，近乎狡猾；对一切事物的看法是理智的、客观的。";
		} else {
			des = "5-6分为中性";
		}
		fac = new Factory();
		fac.setItem("世故性");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("O");
		if (s == 1 || s == 2) {
			des = "安详沉着、有自信心、不轻易动摇，相信自己有应付问题的能力；有安全感，能适应裕如。";
		} else if (s == 3 || s == 4) {
			des = "比较自信、安详、沉着；比较有安全感，能适应当前环境。";
		} else if (s == 7 || s == 8) {
			des = "比较忧虑抑郁、易生烦恼，有时沮丧悲观，缺乏和人接近的勇气。";
		} else if (s == 9 || s == 10) {
			des = "忧虑抑郁、烦恼自扰，常觉得世道艰辛，人生不如意事常八九，甚至沮丧悲观；时时有患得患失之感，自觉不容与人，也缺乏和人接近的勇气。";
		} else {
			des = "5-6分为中性";
		}
		fac = new Factory();
		fac.setItem("忧虑性");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("Q1");
		if (s == 1 || s == 2) {
			des = "思想非常保守、传统，墨守成规，不愿尝试探求新的境界；常激烈反对新思潮及新变动；可能被称为老顽固或时代的落伍者。";
		} else if (s == 3 || s == 4) {
			des = "思想比较保守、尊重传统观念与行为标准，不愿尝试探求新的境界。";
		} else if (s == 7 || s == 8) {
			des = "思想观念比较自由开放，不拘泥于现实，不轻易判断是非，容易接受更先进的思想与行为。";
		} else if (s == 9 || s == 10) {
			des = "自由激进的，常常不拘泥于现实，喜欢考验一切理论与事实；不轻易判断是非，希望了解更先进的思想与行为；可能广见多闻，不断丰富充实自身的生活经验。";
		} else {
			des = "5-6分为中性";
		}
		fac = new Factory();
		fac.setItem("实验性");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("Q2");
		if (s == 1 || s == 2) {
			des = "依赖，随众附合，不愿独立孤行；常放弃个人主见，附合众议，以取得别人的好感；需要集体的支持以维持其自信心，却并非真正的乐群者。";
		} else if (s == 3 || s == 4) {
			des = "依赖性较强，与人共同工作时，常听从众人意见，而不独立孤行；需要集体的支持以维持其自信心。";
		} else if (s == 7 || s == 8) {
			des = "独立性较强，能独自完成自己的工作计划，不依赖别人；一般不受社会舆论的约束。";
		} else if (s == 9 || s == 10) {
			des = "自强自立，当机立断；能自作主张，独自完成自己的工作计划，不依赖别人，也不受社会舆论的约束，同样也无意控制或支配别人；不嫌恶人，也不需要别人的好感。";
		} else {
			des = "5-6分为中性";
		}
		fac = new Factory();
		fac.setItem("独立性");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("Q3");
		if (s == 1 || s == 2) {
			des = "矛盾冲突时，不能克制自己，不尊重礼俗，不考虑别人的需要，充满矛盾，却无法解脱；在生活适应上常有问题。";
		} else if (s == 3 || s == 4) {
			des = "矛盾冲突时，自我克制能力较差，容易冲动。";
		} else if (s == 7 || s == 8) {
			des = "自我克制能力较强，通常能合理的支配自己的感情行动；为人处事，能保持其自尊，并赢得别人的尊重。";
		} else if (s == 9 || s == 10) {
			des = "知己知彼、自律严谨；通常言行一致，能够合理的支配自己的感情行动；为人处事，总能保持其自尊，并赢得别人的尊重，有时不免太固执成见。";
		} else {
			des = "5-6分为中性";
		}
		fac = new Factory();
		fac.setItem("自律性");
		fac.setScore(String.valueOf(s));
		fac.setDesc(des);
		factorys.add(fac);
		des="";
		s = factoryMap.get("Q4");
		if (s == 1 || s == 2) {
			des = "心平气和、闲散宁静、知足常乐；也可能过分疏懒，缺乏进取心。";
		} else if (s == 3 || s == 4) {
			des = "闲散宁静，知足常乐，能保持内心的平衡。";
		} else if (s == 7 || s == 8) {
			des = "有时情绪紧张、激动；做事容易急躁，缺乏耐心；有时感觉疲乏，心神不定，无法摆脱烦恼以求平静。";
		} else if (s == 9 || s == 10) {
			des = "情绪紧张困扰、缺乏耐心、心神不定，常常过度兴奋而感觉疲乏，又无法彻底摆脱以求平静；在社会中，对一切事物都缺乏信念，每日生活战战兢兢，而不能自已。";
		} else {
			des = "5-6分为中性";
		}
		fac = new Factory();
		fac.setItem("紧张性");
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
		map.put("技术型","软件架构师，软件设计师，软件开发工程师，软件测试工程师，软件文档工程师；");
		map.put("艺术型","软件美工师，软件音效师；");
		map.put("教育型","软件培训讲师；");
		map.put("科研型","算法分析师，系统架构师，系统分析师；");
		map.put("服务型","软件客户经理，软件项目顾问，Call Center工程师，软件维护工程师；");
		map.put("管理型","软件项目经理，项目管理工程师；");
		map.put("社交型","软件销售工程师，需求调研工程师，软件维护工程师；");
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
