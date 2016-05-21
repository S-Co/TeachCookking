package com.example.teachcooking;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UsuerActivity extends Activity implements OnClickListener {
	private Button login;
	private Button regist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usuer);
		login = (Button) findViewById(R.id.login);
		regist = (Button) findViewById(R.id.regist);
		login.setOnClickListener(this);
		regist.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			startLoginActivity();
			break;
		case R.id.regist:
			RegisterPage registerPage = new RegisterPage();
			registerPage.setRegisterCallback(new EventHandler() {
				@Override
				public void afterEvent(int event, int result, Object data) {
					if (result == SMSSDK.RESULT_COMPLETE) {
						@SuppressWarnings("unchecked")
						HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
						String phone = (String) phoneMap.get("phone");
						registerUser(phone);
					}
				}

			});
			registerPage.show(this);
			break;
		}
	}

	private void startLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}

	private void registerUser(String phone) {
		Intent intent = new Intent(this, RegiestcompleteActivity.class);
		intent.putExtra("phone", phone);
		startActivity(intent);
	}
}
