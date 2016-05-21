package com.example.teachcooking;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpActivity extends Activity implements OnClickListener {
	private TextView around_help;
	private TextView basket_help;
	private TextView teachcooking_help;
	private TextView mine_help;
	private ImageView help_activity_back;
	private boolean flag1 = true, flag2 = true, flag3 = true, flag4 = true;

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
		setContentView(R.layout.activity_help);
		help_activity_back = (ImageView) findViewById(R.id.help_activity_back);
		mine_help = (TextView) findViewById(R.id.mine_help);
		around_help = (TextView) findViewById(R.id.around_help);
		basket_help = (TextView) findViewById(R.id.basket_help);
		teachcooking_help = (TextView) findViewById(R.id.teachcooking_help);
		mine_help.setOnClickListener(this);
		around_help.setOnClickListener(this);
		basket_help.setOnClickListener(this);
		teachcooking_help.setOnClickListener(this);
		help_activity_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.help_activity_back:
			finish();
			break;
		case R.id.basket_help:
			if (flag1) {
				basket_help.setEllipsize(null);
				basket_help.setSingleLine(!flag1);
			} else {
				basket_help.setEllipsize(TextUtils.TruncateAt.END);
				basket_help.setSingleLine(!flag1);
			}
			flag1 = !flag1;
			break;
		case R.id.mine_help:
			if (flag2) {
				mine_help.setEllipsize(null);
				mine_help.setSingleLine(!flag2);
			} else {
				mine_help.setEllipsize(TextUtils.TruncateAt.END);
				mine_help.setSingleLine(!flag2);
			}
			flag2 = !flag2;
			break;
		case R.id.around_help:
			if (flag3) {
				around_help.setEllipsize(null);
				around_help.setSingleLine(!flag3);
			} else {
				around_help.setEllipsize(TextUtils.TruncateAt.END);
				around_help.setSingleLine(!flag3);
			}
			flag3 = !flag3;
			break;
		case R.id.teachcooking_help:
			if (flag4) {
				teachcooking_help.setEllipsize(null);
				teachcooking_help.setSingleLine(!flag4);
			} else {
				teachcooking_help.setEllipsize(TextUtils.TruncateAt.END);
				teachcooking_help.setSingleLine(!flag4);
			}
			flag4 = !flag4;
			break;
		}
	}

}
