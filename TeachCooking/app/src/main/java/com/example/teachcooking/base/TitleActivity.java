package com.example.teachcooking.base;

import com.example.teachcooking.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public abstract class TitleActivity extends FragmentActivity{

	private TextView mBack;
	private TextView mTitleTv;
	public abstract void initTitle();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initTitle();
	}
	protected void initBack(){
		mBack = (TextView) findViewById(R.id.back);
		mBack.setVisibility(View.VISIBLE);
		mBack.setOnClickListener(onClickListener);
	}
	
	protected void initTitleText(String text) {
		mTitleTv = (TextView) findViewById(R.id.title);
		mTitleTv.setVisibility(View.VISIBLE);
		mTitleTv.setText(text);
	}
	
	OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v == mBack){
				onBackClick();
			}
		}
	};
	
	public  void onBackClick(){
		super.onBackPressed();
	}
}
