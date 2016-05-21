package com.example.teachcooking;

import cn.bmob.v3.BmobUser;

import com.example.teachcooking.entity.UsuerInfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class ManageAccountActivity extends Activity implements OnClickListener{
	private TextView managePhoneNumber;
	private ImageView manageaccount_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 透明状态栏
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// 透明导航栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manageaccount);
		managePhoneNumber = (TextView) findViewById(R.id.managePhoneNumber);
		manageaccount_back = (ImageView) findViewById(R.id.manageaccount_back);
		manageaccount_back.setOnClickListener(this);
		UsuerInfo user= BmobUser.getCurrentUser(this, UsuerInfo.class);
		if (user!=null) {
			managePhoneNumber.setText("+"+user.getMobilePhoneNumber());
		}
	}
	@Override
	public void onClick(View v) {
		finish();
	}
	
}
