package com.jov.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jov.common.StringUtil;
import com.jov.db.DBOpenHelper;
import com.jov.db.User;

public class UserListActivity extends Activity {
	private List<User> userList = new ArrayList<User>();
	private DBOpenHelper dbHelper;
	private String userName = "";
	private ListView usermanage_listview;
	private EditText usermanage_searchinput;
	private ImageButton ib_clear_text;
	private UserListAdapter uAdapter;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.usermanage_list); 
        initData();
        initView();
    }
    public void initData(){
    	Intent intent = getIntent();
    	userName = intent.getStringExtra("user.name");
		dbHelper = new DBOpenHelper(this);
		userList = dbHelper.getAllUser();
    }
    public void initView(){
    	usermanage_listview = (ListView)findViewById(R.id.usermanage_listview);
    	uAdapter = new UserListAdapter(this);
    	usermanage_listview.setAdapter(uAdapter);
    	setListViewHeightBasedOnChildren(usermanage_listview);
    	usermanage_searchinput = (EditText)findViewById(R.id.usermanage_searchinput);
    	ib_clear_text = (ImageButton)findViewById(R.id.ib_clear_text);
    	usermanage_searchinput.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start,
					int before, int count) {
				String name = s.toString();
				if(StringUtil.isEmpty(name)){
					userList = dbHelper.getAllUser();
					ib_clear_text.setVisibility(View.INVISIBLE);
				}else{
					userList = dbHelper.getUserByKey(name);
					ib_clear_text.setVisibility(View.VISIBLE);
				}
				uAdapter.notifyDataSetChanged();
				setListViewHeightBasedOnChildren(usermanage_listview);
			}
		});
    }
    public class UserListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public UserListAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return userList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}
		public final class ViewHolder {
			public TextView userinfor;
			public LinearLayout user_item_linear;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.usermanage_item, null);
				holder.userinfor = (TextView) convertView
						.findViewById(R.id.usermanage_name);
				convertView.setTag(holder);
				holder.user_item_linear  = (LinearLayout)convertView.findViewById(R.id.usermanage_item_linear);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final String uName = userList.get(position).getName();
			holder.userinfor.setText(uName);
			holder.user_item_linear.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switchTo(MainActivity.class,uName);
				}
			});
			return convertView;
		}
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
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			switchTo(MainActivity.class,userName);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
    public  void setListViewHeightBasedOnChildren(ListView listView) {
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
               return;
        }
       
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
               View listItem = listAdapter.getView(i, null, listView);
               listItem.measure(0, 0);  //计算子项View 的宽高
               totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
        }
       
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
