package com.example.teachcooking.base;

import com.example.teachcooking.R;
import com.example.teachcooking.view.LoadingImgView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public abstract class LoadFragment extends Fragment{

	protected View mContentView;
	private View mLoadingView;
	protected com.example.teachcooking.view.LoadingImgView mLoadingImg;
	protected TextView mLoadingText;
	protected Button mReloadBtn;

	protected boolean isFinish = false;
	protected abstract void loadDataFirst();
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mLoadingView = mContentView.findViewById(R.id.loading_layout);
		mLoadingImg = (LoadingImgView) mContentView.findViewById(R.id.loading_img);
		mLoadingText = (TextView) mContentView.findViewById(R.id.loading_tv);
		mReloadBtn = (Button) mContentView.findViewById(R.id.loading_reload_btn);
		mReloadBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadDataFirst();
			}
		});
	}
	
	protected void loading() {
		mLoadingImg.loading();
		mReloadBtn.setVisibility(View.INVISIBLE);
		mLoadingText.setText(getResources().getString(R.string.loading));
	}
	
	protected void loadSuccess() {
		mLoadingImg.stopAnim();
		mLoadingView.setVisibility(View.GONE);
	}
	protected void  loadFailue() {
		mLoadingImg.failed();
		mLoadingText.setText(getResources().getString(R.string.load_failed_please_retry));
		mReloadBtn.setVisibility(View.VISIBLE);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		isFinish = true;
	}
}
