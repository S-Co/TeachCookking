package com.example.teachcooking;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class EditPublishCookbookActivity extends Activity implements OnClickListener {
	private TextView edit_pulish_cook_title;
	private TextView edit_publish_cookbook_cancle;
	private TextView edit_publish_cookbook_sure_change;
	private EditText editext_text;
	private String title;
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
		setContentView(R.layout.activity_edit_publish_cookbook);
		editext_text = (EditText) findViewById(R.id.editext_text);
		editext_text.requestFocus();
		edit_pulish_cook_title = (TextView) findViewById(R.id.edit_pulish_cook_title);
		edit_publish_cookbook_cancle = (TextView) findViewById(R.id.edit_publish_cookbook_cancle);
		edit_publish_cookbook_sure_change = (TextView) findViewById(R.id.edit_publish_cookbook_sure_change);
		title = getIntent().getStringExtra("title");
		edit_pulish_cook_title.setText(title);
		edit_publish_cookbook_cancle.setOnClickListener(this);
		edit_publish_cookbook_sure_change.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edit_publish_cookbook_cancle:
			finish();
			break;
		case R.id.edit_publish_cookbook_sure_change:
			Intent intent = new Intent();
			intent.putExtra("content", editext_text.getText().toString());
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		}
	}

}
