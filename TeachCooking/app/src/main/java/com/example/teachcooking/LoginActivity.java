package com.example.teachcooking;

import java.util.HashMap;

import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import com.example.teachcooking.entity.UsuerInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 登录
 * @author victor
 *
 */
public class LoginActivity extends Activity implements OnClickListener {
	private EditText usuer_name;
	private EditText usuer_password;
	private Button button;
	private UsuerInfo usuerInfo;
	private ProgressDialog dialog;
	private TextView forget_password;
	private ImageView login_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 透明状态栏
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// 透明导航栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		setContentView(R.layout.login_activity);
		dialog = new ProgressDialog(this);
		dialog.setIndeterminate(false);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		login_back = (ImageView) findViewById(R.id.login_back);
		usuer_name = (EditText) findViewById(R.id.usuer_name);
		usuer_password = (EditText) findViewById(R.id.usuer_password);
		forget_password = (TextView) findViewById(R.id.forget_password);
		forget_password.setOnClickListener(this);
		login_back.setOnClickListener(this);
		usuerInfo = new UsuerInfo();
		button = (Button) findViewById(R.id.login_activity_login);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_back:
			finish();
			break;
		case R.id.login_activity_login:
			dialog.show();
			usuerInfo.setUsername(usuer_name.getText().toString());
			usuerInfo.setPassword(usuer_password.getText().toString());
			usuerInfo.login(this, new SaveListener() {
				@Override
				public void onSuccess() {
					dialog.dismiss();
					Toast.makeText(LoginActivity.this, "登录成功",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(LoginActivity.this,
							MenuActivity.class);
					startActivity(intent);
					finish();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					dialog.dismiss();
					Toast.makeText(LoginActivity.this, "登录失败",
							Toast.LENGTH_SHORT).show();
				}
			});
			break;
		case R.id.forget_password:
			RegisterPage registerPage = new RegisterPage();
			registerPage.setRegisterCallback(new EventHandler(){
				@Override
				public void afterEvent(int event, int result, Object data) {
					if (result == SMSSDK.RESULT_COMPLETE) {
						@SuppressWarnings("unchecked")
						HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
						String phone = (String) phoneMap.get("phone");
						Intent intent = new Intent(LoginActivity.this,ChangePasswordActivity.class);
						intent.putExtra("phone", phone);
						startActivity(intent);
					}
				}
			});
			registerPage.show(this);
			break;
		}
	}

}
