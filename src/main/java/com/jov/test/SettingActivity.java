package com.jov.test;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jov.common.StringUtil;
import com.jov.db.DBOpenHelper;
import com.jov.db.SoftAdd;

public class SettingActivity extends Activity implements OnClickListener {
	private String userName = "";
	private DBOpenHelper dbHelper;
	private boolean setting_state = true;
	private ImageView setting_icon_btn;
	private ImageView setting_soft_infor;
	private TextView setting_add_size_tip;
	private String imgPath;
	private View add_line;
	/**
	 * 头像设置用
	 * */
	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static final String IMAGE_UNSPECIFIED = "image/*";
	private Uri outputFileUri;
	private String fileName;
	private byte[] mContent;
	private Bitmap myBitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_setting);
		initData();
		initView();
	}

	public void initData() {
		dbHelper = new DBOpenHelper(this);
		Intent intent = getIntent();
		userName = intent.getStringExtra("user.name");
		SoftAdd add = dbHelper.getAddStatus();
		if (add != null && "n".equals(add.getStatus())) {
			setting_state = false;
		}
		if (add != null && !StringUtil.isEmpty(add.getPath())) {
			imgPath = add.getPath();
		}
	}

	public void initView() {
		setting_icon_btn = (ImageView) findViewById(R.id.setting_icon_btn);
		setting_icon_btn.setOnClickListener(this);
		setting_soft_infor = (ImageView) findViewById(R.id.setting_soft_infor);
		setting_soft_infor.setOnClickListener(this);
		setting_add_size_tip = (TextView) findViewById(R.id.setting_add_size_tip);
		add_line = (View) findViewById(R.id.add_line);
		if (setting_state) {
			setting_icon_btn
					.setImageResource(R.drawable.seekbar_scrubber_control_focused);
			setting_soft_infor.setVisibility(View.VISIBLE);
			setting_add_size_tip.setVisibility(View.VISIBLE);
			add_line.setVisibility(View.VISIBLE);
		} else {
			setting_icon_btn
					.setImageResource(R.drawable.seekbar_scrubber_control_disabled);
			setting_soft_infor.setVisibility(View.GONE);
			setting_add_size_tip.setVisibility(View.GONE);
			add_line.setVisibility(View.GONE);
		}
		if (!StringUtil.isEmpty(imgPath)) {
			// 图片路径
			File img = new File(StringUtil.IMAGE_PATH, imgPath);
			fileName = imgPath;
			if (img.exists()) {
				setting_soft_infor.setImageDrawable(BitmapDrawable
						.createFromPath(img.getPath()));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if(setting_state){
				dbHelper.updateAddSetting(imgPath, "y");
			}else{
				dbHelper.updateAddSetting(imgPath, "n");
			}
			switchTo(MainActivity.class, userName);
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
		if (v == setting_icon_btn) {
			stateChangeDialog();
		}
		if(v==setting_soft_infor){
			photoChangeDialog();
		}
	}
	private void stateChangeDialog() {
		setting_state = !setting_state;
		if (setting_state) {
			setting_icon_btn
					.setImageResource(R.drawable.seekbar_scrubber_control_focused);
			setting_soft_infor.setVisibility(View.VISIBLE);
			setting_add_size_tip.setVisibility(View.VISIBLE);
			add_line.setVisibility(View.VISIBLE);
			dbHelper.updateAddSetting(imgPath, "y");
		} else {
			setting_icon_btn
					.setImageResource(R.drawable.seekbar_scrubber_control_disabled);
			setting_soft_infor.setVisibility(View.GONE);
			setting_add_size_tip.setVisibility(View.GONE);
			add_line.setVisibility(View.GONE);
			dbHelper.updateAddSetting(imgPath, "n");
		}
	}

	/**
	 * 图片设置
	 * **/
	private void imageRead() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		/* 开启Pictures画面Type设定为image */
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType(IMAGE_UNSPECIFIED);
		startActivityForResult(intent, PHOTOZOOM);

	}

	private void cameraRead() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			outputFileUri = Uri.fromFile(fileOutPut());
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			startActivityForResult(intent, PHOTOHRAPH);
		} else {
			Toast.makeText(this, "( ⊙ o ⊙ )没有检测到SD卡！", Toast.LENGTH_SHORT)
					.show();
			return;
		}
	}

	private File fileOutPut() {
		deleteImage();
		fileName = "IMG" + StringUtil.dateTimeFormatNow() + ".jpg";
		File out = new File(StringUtil.IMAGE_PATH);
		if (!out.exists()) {
			out.mkdirs();
		}
		out = new File(StringUtil.IMAGE_PATH, fileName);
		return out;
	}

	private void deleteImage() {
		if (!StringUtil.isEmpty(fileName)) {
			File remove = new File(StringUtil.IMAGE_PATH, fileName);
			if (remove.exists()) {
				remove.delete();
			}
		}
		fileName = "";
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ContentResolver resolver = getContentResolver();
		if (resultCode == RESULT_OK) {
			if (requestCode == PHOTOZOOM) {
				Uri uri = data.getData();
				try {
					mContent = readStream(resolver.openInputStream(Uri
							.parse(uri.toString())));
					myBitmap = getPicFromBytes(mContent, null);
					// //把得到的图片绑定在控件上显示
					setting_soft_infor.setImageBitmap(myBitmap);
					// 拷贝到目录
					copyImageTolocal(myBitmap);
				} catch (Exception e) {
				}
			} else if (requestCode == PHOTOHRAPH) {
				setting_soft_infor.setImageDrawable(BitmapDrawable
						.createFromPath(outputFileUri.getPath()));
				//startPhotoZoom(outputFileUri);
			}
			if (!StringUtil.isEmpty(fileName)) {
				imgPath = fileName;
			}
		}
	}

	public static Bitmap getPicFromBytes(byte[] bytes,
										 BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;
	}

	private void copyImageTolocal(Bitmap bm) {
		if (StringUtil.isEmpty(fileName)) {
			fileName = "IMG" + StringUtil.dateTimeFormatNow() + ".jpg";
		}
		File out = new File(StringUtil.IMAGE_PATH);
		if (!out.exists()) {
			out.mkdirs();
		}
		out = new File(StringUtil.IMAGE_PATH, fileName);
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(out));
			bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void photoChangeDialog() {
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.show();// 需要放在前面
		Window window = dlg.getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setContentView(R.layout.setting_add_image_dialog);
		WindowManager.LayoutParams layparam = window.getAttributes();
		layparam.width = getWindowManager().getDefaultDisplay().getWidth();
		window.setAttributes(layparam);
		window.findViewById(R.id.tab_setting_photo_linear).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dlg.dismiss();
						deleteImage();
						imageRead();
					}
				});
		window.findViewById(R.id.tab_setting_camero_linear).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dlg.dismiss();
						deleteImage();
						cameraRead();
					}
				});
	}
}
