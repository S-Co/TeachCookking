package com.example.teachcooking.view;


import com.example.teachcooking.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 加载网络数据时使用的 带帧动画的ImageView
 * @author caoye
 *
 */
public class LoadingImgView extends ImageView{

	private AnimationDrawable animationDrawable;
	public LoadingImgView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * 开始加载
	 */
	public void loading(){
		setBackgroundResource(R.anim.loading);
		animationDrawable = (AnimationDrawable) getBackground();
		animationDrawable.start();
	}

	/**
	 * 加载失败
	 */
	public void failed(){
		if(animationDrawable != null){
			animationDrawable.stop();
			animationDrawable = null;
		}
		setBackgroundResource(R.drawable.loaded_cry);
	}
	/** 
	 * 停止帧动画
	 */
	public void stopAnim(){
		if(animationDrawable != null){
			animationDrawable.stop();
		}
	}
}
