package com.example.teachcooking.base;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;

/**
 * ��Ϊ���࣬��װ�� Fragment+CheckedTextView���ʵ��ѡ��Ĺ���
 * ʹ�õ�ʱ����̳и�Activity������onCreate������
 * ����init(int containerId,List<Fragment> fragments,CheckedTextView[] checkedTvs)����
 * @author Li-gang
 *
 */
public abstract class FragmentTabActivity extends TitleActivity{

	/**Fragment����**/
	protected List<Fragment> mFragments;
	protected FragmentManager mFragmentManager;
	protected CheckedTextView[] mCheckedTvs;
	protected int mContainerId;
	private CheckedTextView mCurrentCheckedTv = null;
	private Fragment mCurrentFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFragmentManager = getSupportFragmentManager();
	}
	
	/**
	 * ��ʼ��
	 * @param containerId
	 * @param fragments
	 * @param checkedTvs
	 */
	protected void init(int containerId,List<Fragment> fragments,int[] CheckedTextViewIds) {
		this.mFragments = fragments;
		this.mContainerId = containerId;
		mCheckedTvs = new CheckedTextView[CheckedTextViewIds.length];
		for (int i = 0; i < CheckedTextViewIds.length; i++) {
			mCheckedTvs[i] = (CheckedTextView) findViewById(CheckedTextViewIds[i]);
		}
		//ѭ�����ü���
		for (CheckedTextView checkedTextView : mCheckedTvs) {
			checkedTextView.setOnClickListener(onClickListener);
		}
		mCurrentCheckedTv = mCheckedTvs[0];
		mCheckedTvs[0].setChecked(true);
		mCurrentFragment = fragments.get(0);
		changeContent(0);
	}
	
	private OnClickListener onClickListener = new View.OnClickListener() {

		private int index;
		@Override
		public void onClick(View v) {
			for (int i = 0; i < mCheckedTvs.length; i++) {
				if(v == mCheckedTvs[i]){
					index = i;
					switchTab(index,(CheckedTextView) v);
					break;
				}
			}
			
		}
	};
	
	/**
	 * �л�ѡ�
	 * @param index
	 * @param v
	 */
	protected void switchTab(int index, CheckedTextView checkedTv) {
		if(checkedTv != mCurrentCheckedTv){
			changeTab(checkedTv);
			changeContent(index);
			if(onTabChangeListener != null){
				onTabChangeListener.onTabChange(index);
			}
		}
	}
	/**
	 * ����������ʾ����
	 * @param index
	 */
	protected void changeContent(int index){
		
		Fragment fragment = mFragments.get(index);
		
		FragmentTransaction fTransaction = mFragmentManager.beginTransaction();
		if(!fragment.isAdded()){
			fTransaction.add(mContainerId, fragment);
			if(fragment != mCurrentFragment){
				fTransaction.hide(mCurrentFragment);
			}
			mCurrentFragment = fragment;
			fTransaction.commit();
		}else{
			fTransaction.show(fragment);
			fTransaction.hide(mCurrentFragment);
			mCurrentFragment = fragment;
			fTransaction.commit();
		}
		
	}
	
	/**
	 * ����ѡ�CheckedTextView��ѡ��
	 * @param checkedTv
	 */
	protected void changeTab(CheckedTextView checkedTv) {
		mCurrentCheckedTv.toggle();
		checkedTv.toggle();
		mCurrentCheckedTv = checkedTv;
	}
	
	public interface OnTabChangeListener{
		void onTabChange(int currentSelected);
	}
	
	private OnTabChangeListener onTabChangeListener;
	public void setOnTabChangeListener(OnTabChangeListener onTabChangeListener) {
		this.onTabChangeListener = onTabChangeListener;
	}
	
}
