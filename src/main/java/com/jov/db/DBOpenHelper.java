package com.jov.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jov.common.StringUtil;

public class DBOpenHelper extends SQLiteOpenHelper {
	private Context context;
	/**
	 * table
	 * */
	public static String TABLE_NAME_TEST = "tb_test";
	public static String TABLE_NAME_MENTAL_HEALTH = "tb_mental_health";
	public static String TABLE_ADAPTION = "tb_adaption";
	public static String TABLE_FACTORY = "tb_factory";
	public static String TABLE_APPABILITY = "tb_app_ability";
	public static String TABLE_USER = "tb_user";
	public static String TABLE_SETTING = "tb_setting";
	/**
	 * column
	 * */
	public static String TEST_ID = "tid";
	public static String TEST_NAME = "name";
	public static String TEST_DES = "description";
	public static String TEST_TIMES = "times";
	public static String TEST_NUM = "testnum";

	public static String MENTAL_HEALTH_ID = "mid";
	public static String MENTAL_HEALTH_SUBJECT = "subject";
	public static String MENTAL_HEALTH_ANSWERA = "answerA";
	public static String MENTAL_HEALTH_ANSWERB = "answerB";
	public static String MENTAL_HEALTH_ANSWERC = "answerC";
	public static String MENTAL_HEALTH_ANSWERASCORE = "answerAScore";
	public static String MENTAL_HEALTH_ANSWERBSCORE = "answerBScore";
	public static String MENTAL_HEALTH_ANSWERCSCORE = "answerCScore";
	public static String MENTAL_HEALTH_FACTORID = "factorid";
	public static String SIMPLE_TEST_ANSWERD = "answerD";
	public static String SIMPLE_TEST_ANSWERE = "answerE";
	public static String SIMPLE_TEST_ANSWERF = "answerF";
	public static String SIMPLE_TEST_ANSWERG = "answerG";
	public static String SIMPLE_TEST_ANSWERDSCORE = "answerDScore";
	public static String SIMPLE_TEST_ANSWERESCORE = "answerEScore";
	public static String SIMPLE_TEST_ANSWERFSCORE = "answerFScore";
	public static String SIMPLE_TEST_ANSWERGSCORE = "answerGScore";
	public static String SIMPLE_TEST_FLAG = "flag";

	public static String FACTORY_ITEM = "item";
	public static String FACTORY_SCORE = "score";
	public static String FACTORY_DESC = "desc";

	public static String USER_NAME = "name";
	public static String USER_SEX = "sex";
	public static String USER_ID = "uid";

	public static String SETTING_CONTENT = "content";
	public static String SETTING_STATUS = "status";

	private static final String DB_NAME = "mytest.db";
	/**
	 * version
	 * */
	private static final int VERSION = 1;
	/**
	 * SQL for create table
	 * */
	private static final String CREATE_TABLE_TEST = "create table IF NOT EXISTS tb_test(tid integer primary key autoincrement,"
			+ "name varchar(100),description varchar(500),times varchar(40),testnum varchar(5))";
	private static final String CREATE_TABLE_MENTAL_HEALTH = "create table IF NOT EXISTS tb_mental_health(mid integer primary key autoincrement,"
			+ "subject varchar(200),answerA varchar(100),answerB varchar(100),answerC varchar(100),answerAScore integer,"
			+ "answerBScore integer,answerCScore integer,factorid varchar(4),remark varchar(2))";
	private static final String CREATE_TABLE_FACTORY = "create table IF NOT EXISTS tb_factory(fid integer primary key autoincrement,"
			+ "item varchar(20),score varchar(4),desc varchar(200),userId varchar(20))";
	private static final String CREATE_TABLE_APPABILITY = "create table IF NOT EXISTS tb_app_ability(aid integer primary key autoincrement,"
			+ "item varchar(20),score varchar(4),userId varchar(20))";
	private static final String CREATE_TABLE_ADAPTION = "create table IF NOT EXISTS tb_adaption(aid integer primary key autoincrement,"
			+ "item varchar(20),userId varchar(20))";
	private static final String CREATE_TABLE_USER = "create table IF NOT EXISTS tb_user(uid integer primary key autoincrement,"
			+ "name varchar(40),sex varchar(4))";
	private static final String CREATE_TABLE_SETTING = "create table IF NOT EXISTS tb_setting(ukey varchar(40) primary key ,"
			+ "content varchar(40),status varchar(2))";
	private static final String CREATE_TABLE_SIMPLE_TEST = "create table IF NOT EXISTS tb_simple_test(sid integer primary key autoincrement,"
			+ "subject varchar(200),answerA varchar(100),answerB varchar(100),answerC varchar(100),answerD varchar(100),answerE varchar(100),"
			+ "answerF varchar(100),answerG varchar(100),answerAScore integer,answerBScore integer,answerCScore integer,answerDScore integer,"
			+ "answerEScore integer,answerFScore integer,answerGScore integer,flag integer)";
	private static final String CREATE_TABLE_TEST_RESULT = "create table IF NOT EXISTS tb_test_result(tid integer primary key autoincrement,"
			+ "name varchar(40),content varchar(400),tester varchar(40))";
	private static final String CREATE_TABLE_COLOR_TEST = "create table IF NOT EXISTS tb_color_test(tid integer primary key autoincrement,"
			+ "name varchar(40),content varchar(400),tester varchar(40))";

	/**
	 * SQL for drop table
	 * */
	private static final String DROP_TABLE_TEST = "DROP TABLE IF EXISTS tb_test";

	public DBOpenHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		this.context = context;
	}

	public DBOpenHelper(Context context, String name, int version) {
		super(context, name, null, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_TEST);
		db.execSQL(CREATE_TABLE_MENTAL_HEALTH);
		db.execSQL(CREATE_TABLE_SIMPLE_TEST);
		db.execSQL(CREATE_TABLE_FACTORY);
		db.execSQL(CREATE_TABLE_APPABILITY);
		db.execSQL(CREATE_TABLE_ADAPTION);
		db.execSQL(CREATE_TABLE_USER);
		db.execSQL(CREATE_TABLE_SETTING);
		db.execSQL(CREATE_TABLE_TEST_RESULT);
		//db.execSQL(CREATE_TABLE_COLOR_TEST);
		insertCommonData(db);
		insertMHData(db);
		insertSTData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE_TEST);
		onCreate(db);
	}

	public void insertCommonData(SQLiteDatabase db) {
		String sql = "insert into tb_test values(?,?,?,?,?)";
		String name = "大学生心理素质健康测试";
		String times = "预计用时1小时";
		String testNum = "187";
		String simple_name = "职场性格测试";
		String simple_times = "预计用时10分钟";
		String simple_testNum = "10";
		try {
			InputStream is = context.getAssets().open("test_desc.db");
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			db.execSQL(sql, new String[] { "1",name, buffer.toString(), times,
					testNum });

			is = context.getAssets().open("simple_test_desc.db");
			reader = new BufferedReader(
					new InputStreamReader(is));
			buffer = new StringBuffer();
			line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			db.execSQL(sql, new String[] { "2",simple_name, buffer.toString(), simple_times,
					simple_testNum });
		} catch (IOException e) {
			Log.e("DBerror", e.getMessage());
		} catch (Exception e) {
			Log.e("DBerror", e.getMessage());
		}
	}

	public void insertMHData(SQLiteDatabase db) {
		try {
			InputStream is = context.getAssets().open("mental_health.db");
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line = "";
			while ((line = reader.readLine()) != null) {
				db.execSQL(line, new String[] {});
			}
		} catch (IOException e) {
			Log.e("DBerror", e.getMessage());
		} catch (Exception e) {
			Log.e("DBerror", e.getMessage());
		}
	}
	public void insertSTData(SQLiteDatabase db){
		try {
			InputStream is = context.getAssets().open("simple_test.db");
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line = "";
			while ((line = reader.readLine()) != null) {
				db.execSQL(line, new String[] {});
			}
		} catch (IOException e) {
			Log.e("DBerror", e.getMessage());
		} catch (Exception e) {
			Log.e("DBerror", e.getMessage());
		}
	}
	public TestPoJo getTest(String tid) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		TestPoJo test = null;
		if (StringUtil.isEmpty(tid)) {
			cursor = db.rawQuery("select * from tb_test limit 1 ",
					new String[] {});
		} else {
			cursor = db.rawQuery("select * from tb_test where  tid = ? ",
					new String[] { tid });
		}
		if (cursor != null && cursor.moveToNext()) {
			test = new TestPoJo();
			test.setTid(cursor.getInt(cursor.getColumnIndex(TEST_ID)));
			test.setName(cursor.getString(cursor.getColumnIndex(TEST_NAME)));
			test.setDescription(cursor.getString(cursor
					.getColumnIndex(TEST_DES)));
			test.setTimes(cursor.getString(cursor.getColumnIndex(TEST_TIMES)));
			test.setTestNum(cursor.getString(cursor.getColumnIndex(TEST_NUM)));
		}
		db.close();
		return test;
	}

	public int getTotalMH() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		int count = 0;
		cursor = db.rawQuery("select count(mid) from  tb_mental_health",
				new String[] {});
		if (cursor != null && cursor.moveToNext()) {
			count = cursor.getInt(0);
		}
		if (cursor != null) {
			cursor.close();
		}
		return count;
	}

	public MentalHealth getMentalHealth(String offset) {
		MentalHealth health = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery(
				"select * from tb_mental_health limit 1 offset ? ",
				new String[] { offset }

		);
		if (cursor != null && cursor.moveToNext()) {
			health = new MentalHealth();
			health.setId(cursor.getInt(cursor.getColumnIndex(MENTAL_HEALTH_ID)));
			health.setSubject(cursor.getString(cursor
					.getColumnIndex(MENTAL_HEALTH_SUBJECT)));
			health.setAnswerA(cursor.getString(cursor
					.getColumnIndex(MENTAL_HEALTH_ANSWERA)));
			health.setAnswerB(cursor.getString(cursor
					.getColumnIndex(MENTAL_HEALTH_ANSWERB)));
			health.setAnswerC(cursor.getString(cursor
					.getColumnIndex(MENTAL_HEALTH_ANSWERC)));
			health.setAnswerAScore(cursor.getInt(cursor
					.getColumnIndex(MENTAL_HEALTH_ANSWERASCORE)));
			health.setAnswerBScore(cursor.getInt(cursor
					.getColumnIndex(MENTAL_HEALTH_ANSWERBSCORE)));
			health.setAnswerCScore(cursor.getInt(cursor
					.getColumnIndex(MENTAL_HEALTH_ANSWERCSCORE)));
			health.setFactorId(cursor.getString(cursor
					.getColumnIndex(MENTAL_HEALTH_FACTORID)));
		}
		if (cursor != null) {
			cursor.close();
		}
		return health;
	}

	public int getTotalST() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		int count = 0;
		cursor = db.rawQuery("select count(sid) from  tb_simple_test",
				new String[] {});
		if (cursor != null && cursor.moveToNext()) {
			count = cursor.getInt(0);
		}
		if (cursor != null) {
			cursor.close();
		}
		return count;
	}
	public SimpleTest getSimpleTest(String offset) {
		SimpleTest simple = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery(
				"select * from tb_simple_test limit 1 offset ? ",
				new String[] { offset }

		);
		if (cursor != null && cursor.moveToNext()) {
			simple = new SimpleTest();
			simple.setId(cursor.getInt(cursor.getColumnIndex("sid")));
			simple.setSubject(cursor.getString(cursor
					.getColumnIndex(MENTAL_HEALTH_SUBJECT)));
			simple.setAnswerA(cursor.getString(cursor
					.getColumnIndex(MENTAL_HEALTH_ANSWERA)));
			simple.setAnswerB(cursor.getString(cursor
					.getColumnIndex(MENTAL_HEALTH_ANSWERB)));
			simple.setAnswerC(cursor.getString(cursor
					.getColumnIndex(MENTAL_HEALTH_ANSWERC)));
			simple.setAnswerD(cursor.getString(cursor
					.getColumnIndex(SIMPLE_TEST_ANSWERD)));
			simple.setAnswerE(cursor.getString(cursor
					.getColumnIndex(SIMPLE_TEST_ANSWERE)));
			simple.setAnswerF(cursor.getString(cursor
					.getColumnIndex(SIMPLE_TEST_ANSWERF)));
			simple.setAnswerG(cursor.getString(cursor
					.getColumnIndex(SIMPLE_TEST_ANSWERG)));
			simple.setAnswerAScore(cursor.getInt(cursor
					.getColumnIndex(MENTAL_HEALTH_ANSWERASCORE)));
			simple.setAnswerBScore(cursor.getInt(cursor
					.getColumnIndex(MENTAL_HEALTH_ANSWERBSCORE)));
			simple.setAnswerCScore(cursor.getInt(cursor
					.getColumnIndex(MENTAL_HEALTH_ANSWERCSCORE)));
			simple.setAnswerAScore(cursor.getInt(cursor
					.getColumnIndex(SIMPLE_TEST_ANSWERDSCORE)));
			simple.setAnswerBScore(cursor.getInt(cursor
					.getColumnIndex(SIMPLE_TEST_ANSWERESCORE)));
			simple.setAnswerCScore(cursor.getInt(cursor
					.getColumnIndex(SIMPLE_TEST_ANSWERFSCORE)));
			simple.setAnswerGScore(cursor.getInt(cursor
					.getColumnIndex(SIMPLE_TEST_ANSWERGSCORE)));
			simple.setFlag(cursor.getInt(cursor
					.getColumnIndex(SIMPLE_TEST_FLAG)));
		}
		if (cursor != null) {
			cursor.close();
		}
		return simple;
	}
	public void insertFactory(Factory fac, String userId) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "insert into tb_factory values(null,?,?,?,?)";
		db.execSQL(sql,
				new String[] { fac.getItem(), fac.getScore(), fac.getDesc(),
						userId });
	}

	public void deleteFactory(String userId) {
		SQLiteDatabase db = this.getReadableDatabase();
		db.delete(TABLE_FACTORY, "userId = ? ", new String[] { userId });
	}

	public List<Factory> getFactoryByUId(String userId) {
		List<Factory> list = new ArrayList<Factory>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery("select * from tb_factory where userId= ? ",
				new String[] { userId }

		);
		while (cursor != null && cursor.moveToNext()) {
			Factory fac = new Factory();
			fac.setItem(cursor.getString(cursor.getColumnIndex(FACTORY_ITEM)));
			fac.setScore(cursor.getString(cursor.getColumnIndex(FACTORY_SCORE)));
			fac.setDesc(cursor.getString(cursor.getColumnIndex(FACTORY_DESC)));
			list.add(fac);
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}

	public void insertAppAbility(String item, String score, String userId) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "insert into tb_app_ability values(null,?,?,?)";
		db.execSQL(sql, new String[] { item, score, userId });
	}

	public void deleteAppAbility(String userId) {
		SQLiteDatabase db = this.getReadableDatabase();
		db.delete(TABLE_APPABILITY, "userId = ? ", new String[] { userId });
	}

	public List<AppAbility> getAppAbilityByUId(String userId) {
		List<AppAbility> list = new ArrayList<AppAbility>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery("select * from tb_app_ability where userId= ? ",
				new String[] { userId });
		while (cursor != null && cursor.moveToNext()) {
			AppAbility app = new AppAbility();
			app.setItem(cursor.getString(cursor.getColumnIndex(FACTORY_ITEM)));
			app.setScore(cursor.getString(cursor.getColumnIndex(FACTORY_SCORE)));
			list.add(app);
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}

	public void insertAdaption(String item, String userId) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "insert into tb_adaption values(null,?,?)";
		db.execSQL(sql, new String[] { item, userId });
	}

	public String getAdaptionByUId(String userId) {
		String result = "";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery("select * from tb_adaption where userId= ? ",
				new String[] { userId }

		);
		if (cursor != null && cursor.moveToNext()) {
			result = cursor.getString(cursor.getColumnIndex(FACTORY_ITEM));
		}
		if (cursor != null) {
			cursor.close();
		}
		return result;
	}

	public void updateNote(String item, String userId) {
		ContentValues values = new ContentValues();
		values.put(FACTORY_ITEM, item);
		SQLiteDatabase db = this.getReadableDatabase();
		db.update(TABLE_ADAPTION, values, "userId =?", new String[] { userId });
	}

	public void insertUser(String name, String sex) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "insert into tb_user values(null,?,?)";
		db.execSQL(sql, new String[] { name, sex });
	}
	public User getUserByName(String name) {
		User result = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery("select * from tb_user where name= ? ",
				new String[] { name }

		);
		if (cursor != null && cursor.moveToNext()) {
			result = new User();
			result.setName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
			result.setSex(cursor.getString(cursor.getColumnIndex(USER_SEX)));
		}
		if (cursor != null) {
			cursor.close();
		}
		return result;
	}
	public List<User> getAllUser() {
		List<User> list = new ArrayList<User>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery("select * from tb_user ",new String[] {});
		while (cursor != null && cursor.moveToNext()) {
			User result = new User();
			result.setName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
			result.setSex(cursor.getString(cursor.getColumnIndex(USER_SEX)));
			list.add(result);
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}
	public List<User> getUserByKey(String keyw) {
		List<User> list = new ArrayList<User>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery("select * from tb_user where name like ? ",new String[] {"%"+keyw+"%"});
		while (cursor != null && cursor.moveToNext()) {
			User result = new User();
			result.setName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
			result.setSex(cursor.getString(cursor.getColumnIndex(USER_SEX)));
			list.add(result);
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}
	public void deleteUser(String name) {
		SQLiteDatabase db = this.getReadableDatabase();
		db.delete(TABLE_USER, "name = ? ", new String[] { name });
	}
	public void insertSetting(String name, String status) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "insert into tb_setting values(?,?,?)";
		db.delete(TABLE_SETTING, "ukey = ? ", new String[] { "defaultUser" });
		db.execSQL(sql, new String[] {"defaultUser",name, status });
	}
	public User getDefaultUser() {
		User result = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery("select * from tb_setting where ukey= ? ",
				new String[] { "defaultUser" }

		);
		if (cursor != null && cursor.moveToNext()) {
			result = new User();
			result.setName(cursor.getString(cursor.getColumnIndex(SETTING_CONTENT)));
			result.setStatus(cursor.getString(cursor.getColumnIndex(SETTING_STATUS)));
		}
		if (cursor != null) {
			cursor.close();
		}
		return result;
	}
	public SoftAdd getAddStatus(){
		SoftAdd result = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery("select * from tb_setting where ukey= ? ",
				new String[] { "softAdInfor" }

		);
		if (cursor != null && cursor.moveToNext()) {
			result = new SoftAdd();
			result.setPath(cursor.getString(cursor.getColumnIndex(SETTING_CONTENT)));
			result.setStatus(cursor.getString(cursor.getColumnIndex(SETTING_STATUS)));
		}
		if (cursor != null) {
			cursor.close();
		}
		return result;
	}
	public void updateAddSetting(String con,String stats){
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "insert into tb_setting values(?,?,?)";
		db.delete(TABLE_SETTING, "ukey = ? ", new String[] { "softAdInfor" });
		db.execSQL(sql, new String[] {"softAdInfor",con, stats });
	}
	public void insertTestResult(String name ,String conent,String tester) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "insert into tb_test_result values(null,?,?,?)";
		db.execSQL(sql, new String[] {name, conent,tester});
	}
	public String getTestResult(String name,String tester) {
		String result = "";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery("select * from tb_test_result where name=? and tester=? ",
				new String[] {name ,tester}
		);
		if (cursor != null && cursor.moveToNext()) {
			result= cursor.getString(cursor.getColumnIndex("content"));
		}
		if (cursor != null) {
			cursor.close();
		}
		return result;
	}
}
