package com.example.teachcooking;

import cn.bmob.v3.Bmob;
import cn.smssdk.SMSSDK;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

public class LogoActivity extends Activity implements Callback {
	private Handler handler = new Handler(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_logo);
		SMSSDK.initSDK(this,"102831f2220be" , "a67e94659c85e1073560310128cca31d");
		Bmob.initialize(this, "c6f218a1a947a8c622930a69e70f069f");
		handler.sendEmptyMessageDelayed(1, 2000);
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == 1) {
			Intent intent = new Intent(LogoActivity.this, MenuActivity.class);
			startActivity(intent);
			finish();
		}
		return false;
	}

}
