package com.jov.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jov.common.ResApplication;
import com.jov.common.StringUtil;
import com.jov.db.AppAbility;
import com.jov.db.DBOpenHelper;
import com.jov.db.Factory;
import com.jov.db.SoftAdd;
import com.jov.db.TestPoJo;
import com.jov.db.User;

public class MainActivity extends Activity implements OnClickListener,OnLongClickListener {

	public static MainActivity instance = null;

	private ViewPager mTabPager;// 声明对象
	private int currIndex = 0;// 当前页卡编号
	private String[] titles;
	private TextView tab_tittle;
	private ImageView[] pointImgs;
	private LinearLayout pointLLayout;
	private long exitTime = 0;
	private View mainView;
	private View myTestView;
	private DBOpenHelper dbHelper;
	private TestPoJo testPojo;
	private TextView tests_choose_str;
	private TextView tests_choose_des;
	private View tests_des_linear;
	private TextView tests_time;
	private TextView tests_choose_num;
	private Button begin_test_btn;
	private TextView personal;
	private TextView ability_report;
	private List<Factory> factorys = new ArrayList<Factory>();
	private List<AppAbility> appAbilitys = new ArrayList<AppAbility>();
	private String jobReport = "";
	private TextView job_report;
	private ImageView soft_infor_add;
	private View user_create_linear;
	private View user_tests_linear;
	private TextView user_name;
	private TextView mytest_username;
	private String userName = "";
	private String doneUser="";
	private String sex = "";
	private SoftAdd add;
	private TextView simple_test_result_report;
	private int test_type=1;
	private View tests_choose_linear;
	private View tests_numbers_linear;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		instance = this;
		ResApplication.addActivity(instance);
		initDBData();
		initView();
		initOther();
	}

	public void initDBData() {
		dbHelper = new DBOpenHelper(this);
		testPojo = dbHelper.getTest(null);
		if (testPojo == null) {
			testPojo = new TestPoJo();
		}
	}

	public void initView() {
		mTabPager = (ViewPager) findViewById(R.id.tabpager);
		pointLLayout = (LinearLayout) findViewById(R.id.main_bottom);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
		LayoutInflater mLi = LayoutInflater.from(this);
		mainView = mLi.inflate(R.layout.tab_main, null);
		myTestView = mLi.inflate(R.layout.tab_mytest, null);
		tests_choose_str = (TextView) mainView
				.findViewById(R.id.tests_choose_str);
		tests_choose_des = (TextView) mainView
				.findViewById(R.id.tests_choose_des);
		tests_des_linear = (View) mainView.findViewById(R.id.tests_des_linear);
		tests_des_linear.setOnLongClickListener(this);
		tests_time = (TextView) mainView.findViewById(R.id.tests_time);
		tests_choose_num = (TextView) mainView
				.findViewById(R.id.tests_choose_num);
		tests_choose_linear =  mainView.findViewById(R.id.tests_choose_linear);
		tests_choose_linear.setOnClickListener(this);
		initTestSetting();
		begin_test_btn = (Button) mainView.findViewById(R.id.begin_test_btn);
		soft_infor_add = (ImageView) mainView.findViewById(R.id.soft_infor_add);
		initAddInfor();
		begin_test_btn.setOnClickListener(this);
		user_create_linear = (View) mainView.findViewById(R.id.user_create_linear);
		user_create_linear.setOnClickListener(this);
		tests_numbers_linear = (View) mainView.findViewById(R.id.tests_numbers_linear);
		tests_numbers_linear.setOnClickListener(this);
		user_name = (TextView) mainView.findViewById(R.id.user_name);
		user_name.setOnClickListener(this);
		personal = (TextView) myTestView.findViewById(R.id.personal_report);
		ability_report = (TextView) myTestView
				.findViewById(R.id.ability_report);
		job_report = (TextView) myTestView.findViewById(R.id.job_report);
		mytest_username = (TextView) myTestView
				.findViewById(R.id.mytest_username);
		user_tests_linear =  (View) myTestView
				.findViewById(R.id.user_tests_linear);
		user_tests_linear.setOnClickListener(this);
		simple_test_result_report = (TextView) myTestView.findViewById(R.id.simple_test_result_report);
		// 每个页面的view数据
		final ArrayList<View> views = new ArrayList<View>();
		views.add(mainView);
		views.add(myTestView);
		titles = new String[] { "吾测", "我的测试" };
		tab_tittle = (TextView) findViewById(R.id.tab_tittle);
		pointImgs = new ImageView[2];
		for (int i = 0; i < 2; i++) {
			pointImgs[i] = (ImageView) pointLLayout.getChildAt(i);
			pointImgs[i].setEnabled(true);
			pointImgs[i].setTag(i);
		}
		PagerAdapter mPagerAdapter = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};
		mTabPager.setAdapter(mPagerAdapter);
		initContent();
	}
	public void initTestSetting(){
		test_type = testPojo.getTid();
		tests_choose_str.setText(testPojo.getName());
		tests_choose_des.setText(testPojo.getDescription());
		tests_time.setText(testPojo.getTimes());
		tests_choose_num.setText(testPojo.getTestNum());
	}
	public void initContent() {
		pointImgs[currIndex].setEnabled(true);
		mTabPager.setCurrentItem(currIndex);
		tab_tittle.setText(titles[currIndex]);
	}
	public void initAddInfor(){
		add = dbHelper.getAddStatus();
		if(add!=null&&"n".equals(add.getStatus())){
			soft_infor_add.setVisibility(View.GONE);
		}else{
			soft_infor_add.setVisibility(View.VISIBLE);
			if(add!=null&&!StringUtil.isEmpty(add.getPath())){
				//图片路径
     			File img = new File(StringUtil.IMAGE_PATH, add.getPath());
     			if (!img.exists()) {
     				soft_infor_add.setImageResource(R.drawable.add);
     			} else {
     				soft_infor_add.setImageDrawable(BitmapDrawable
     						.createFromPath(img.getPath()));
     			}
			}
			soft_infor_add.setOnLongClickListener(this);
		}
	}
	public void initOther() {
		Intent intent = getIntent();
		doneUser = intent.getStringExtra("user.name");
		if(!StringUtil.isEmpty(userName)){
			user_name.setText(userName);
		}
		if (StringUtil.isEmpty(doneUser)) {
			User user = dbHelper.getDefaultUser();
			if (user != null) {
				doneUser = user.getName();
			}
		}
		if (StringUtil.isEmpty(doneUser)) {
			currIndex = 0;
		} else {
			factorys = dbHelper.getFactoryByUId(doneUser);
			mytest_username.setText(doneUser);
			if(factorys.size() > 0){
				appAbilitys = dbHelper.getAppAbilityByUId(doneUser);
				jobReport = dbHelper.getAdaptionByUId(doneUser);
				if (factorys.size() > 0) {
					StringBuffer personalReport = new StringBuffer();
					for (Factory fac : factorys) {
						personalReport.append(fac.toString() + "\n");
					}
					personalReport.replace(personalReport.lastIndexOf("\n"),
							personalReport.length(), "");
					personal.setText(personalReport);
				}
				if (appAbilitys.size() > 0) {
					StringBuffer abilityReport = new StringBuffer();
					for (AppAbility ab : appAbilitys) {
						abilityReport.append(ab.toString() + "\n");
					}
					abilityReport.replace(abilityReport.lastIndexOf("\n"),
							abilityReport.length(), "");
					ability_report.setText(abilityReport);
				}
				job_report.setText(jobReport);
			}
			String simple_test = dbHelper.getTestResult("simple_test", doneUser);
			simple_test_result_report.setText(simple_test);
			currIndex = 1;
		}
		initContent();
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		public void onPageSelected(int position) {
			tab_tittle.setText(titles[position]);
			pointImgs[currIndex].setEnabled(true);
			pointImgs[position].setEnabled(false);
			currIndex = position;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	public void backTo() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			dbHelper.insertSetting(doneUser,"n");
			ResApplication.finish();
			System.exit(0);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			backTo();
			return true;
		}
		if(keyCode == KeyEvent.KEYCODE_MENU&& event.getAction() == KeyEvent.ACTION_DOWN){
			if(add==null){
				dbHelper.updateAddSetting("", "y");
			}else{
				dbHelper.updateAddSetting(add.getPath(), "y");
			}
			initAddInfor();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void switchTo(Class clazz) {
		Intent intent = new Intent();
		intent.setClass(this, clazz);
		startActivity(intent);
		finish();
	}

	public void switchTo(Class clazz, String userName) {
		Intent intent = new Intent();
		intent.setClass(this, clazz);
		Bundle bundle = new Bundle();
		bundle.putString("user.name", userName);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		if (v == begin_test_btn) {
			if (StringUtil.isEmpty(userName)) {
				Toast.makeText(getApplicationContext(), "请创建用户",
						Toast.LENGTH_SHORT).show();
				return;
			}
			chooseTest();
		}
		if (v == user_create_linear) {
			showUserInputMenu();
		}
		if(v==user_tests_linear){
			switchTo(UserListActivity.class, doneUser);
		}
		if(v==tests_choose_linear){
			showTestDialog();
		}
		if(v==tests_numbers_linear){
			switchTo(TestInforActivity.class, doneUser);
		}
	}

	public void showUserInputMenu() {
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.setView(LayoutInflater.from(this).inflate(
				R.layout.create_user_dialog, null));
		dlg.show();
		final Window window = dlg.getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setContentView(R.layout.create_user_dialog);
		WindowManager.LayoutParams layparam = window.getAttributes();
		layparam.width = getWindowManager().getDefaultDisplay().getWidth();
		window.setAttributes(layparam);
		final TextView uName = (TextView) window.findViewById(R.id.user_id);
		final RadioGroup group = (RadioGroup) window
				.findViewById(R.id.sexGroup);
		window.findViewById(R.id.ok_begin_test_btn).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dlg.dismiss();
						if (uName.getText() == null
								|| uName.getText().equals("")) {
							return;
						}
						userName = uName.getText().toString();
						if(StringUtil.isEmpty(userName)){
							return;
						}
						if (dbHelper.getUserByName(userName) != null) {
							user_name.setText("这个名字已经存在了");
							user_name.setTextColor(Color.RED);
							return;
						}
						user_name.setText(userName);
						int cid = group.getCheckedRadioButtonId();
						RadioButton rb = (RadioButton) window.findViewById(cid);
						sex = rb.getText().toString();
						dbHelper.insertUser(userName, sex);
						chooseTest();
					}
				});
	}
	public void chooseTest(){
		switch(test_type){
		case 1:
			switchTo(DoMentalHealthTestActivity.class, userName);
			break;
		case 2:
			switchTo(DoSimpleTestActivity.class, userName);
			break;
		}
	}
	@Override
	public boolean onLongClick(View v) {
		if (v == soft_infor_add) {
			switchTo(SettingActivity.class, doneUser);
			return false;
		}
		if(v==tests_des_linear){
			showInforDialog();
		}
		return false;
	}
	public void showInforDialog(){
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.setView(LayoutInflater.from(this).inflate(
				R.layout.test_infor_dialog, null));
		dlg.show();
		final Window window = dlg.getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setContentView(R.layout.test_infor_dialog);
		WindowManager.LayoutParams layparam = window.getAttributes();
		layparam.width = getWindowManager().getDefaultDisplay().getWidth();
		window.setAttributes(layparam);
		TextView inforName = (TextView)window.findViewById(R.id.test_infor_name);
		inforName.setText(testPojo.getName());
		TextView tesr_infor_content = (TextView)window.findViewById(R.id.tesr_infor_content);
		tesr_infor_content.setText(testPojo.getDescription());
		window.findViewById(R.id.tesr_infor_btn).setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dlg.dismiss();
				}
		});
	}
	public void showTestDialog(){
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.setView(LayoutInflater.from(this).inflate(
				R.layout.test_choose_dialog, null));
		dlg.show();
		final Window window = dlg.getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setContentView(R.layout.test_choose_dialog);
		WindowManager.LayoutParams layparam = window.getAttributes();
		layparam.width = getWindowManager().getDefaultDisplay().getWidth();
		window.setAttributes(layparam);
		window.findViewById(R.id.mental_test_linear).setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dlg.dismiss();
					testPojo = dbHelper.getTest("1");
					initTestSetting();
				}
		});
		window.findViewById(R.id.simple_test_linear).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dlg.dismiss();
						testPojo = dbHelper.getTest("2");
						initTestSetting();
					}
			});
	}
}
