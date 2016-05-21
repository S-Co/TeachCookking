package com.example.teachcooking.fragment;

import java.util.List;

import com.example.teachcooking.CookDetailsActivity;
import com.example.teachcooking.R;
import com.example.teachcooking.adpter.CookListAdapter;
import com.example.teachcooking.adpter.CookListAdapter.OnItemSelectedChangeListener;
import com.example.teachcooking.db.DbHelper;
import com.example.teachcooking.entity.CookBook;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

public class FragmentCollect extends Fragment{
	private View mContentView;
	private ListView mListView;
	private CookListAdapter mListAdapter;
	private DbHelper mHelper;
	private View mHideView;
	private View mDeleteOk;
	private View mDeleteCancel;
	private CheckedTextView mAllSelectCheckBox;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_collect, null);
		return mContentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mHelper = new DbHelper(getActivity());
		initListView();
		
		mHideView = mContentView.findViewById(R.id.layout_hide);
		mDeleteOk = mContentView.findViewById(R.id.btn_ok);
		mDeleteCancel = mContentView.findViewById(R.id.btn_exit);
		mDeleteOk.setOnClickListener(onClickListener);
		mDeleteCancel.setOnClickListener(onClickListener);
		mAllSelectCheckBox = (CheckedTextView) mContentView.findViewById(R.id.checkbox);
		mAllSelectCheckBox.setOnClickListener(onClickListener);
	}
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v == mDeleteCancel){
				editCancel();
			}else if(v == mDeleteOk){
				if(mListAdapter.getSelectedPositions().size() == 0){
					Toast.makeText(getActivity(), "��ѡ��Ҫɾ������Ŀ", Toast.LENGTH_SHORT).show();
					return;
				}
				showDeleteDialog();
			 }else if(v == mAllSelectCheckBox){
				 if(mAllSelectCheckBox.isChecked()){
					 mAllSelectCheckBox.setChecked(false);
					 mListAdapter.unSelectAll();
				 }else{
					 mAllSelectCheckBox.setChecked(true);
					 mListAdapter.selectAll();
				 }
			 }
		}

	};
	/**
	 * �˳��༭ģʽ
	 */
	private void editCancel() {
		mHideView.setVisibility(View.GONE);
		mListAdapter.setmIdModeEdit(false);
		mListAdapter.unSelectAll();
	}
	/**
	 * ��ʼ��ListView
	 */
	private void initListView() {
		mListView = (ListView) mContentView.findViewById(R.id.listView);
		cookBooks = mHelper.getCollectCookBooks(null);
		mListAdapter = new CookListAdapter(cookBooks , getActivity());
		mListAdapter.setOnItemSelectedChangeListener(onItemSelectedChangeListener);
		View emptyView = mContentView.findViewById(R.id.empty_view);
		mListView.setEmptyView(emptyView );
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(onItemClickListener);
		mListView.setOnItemLongClickListener(onItemLongClickListener);
	}
	OnItemSelectedChangeListener onItemSelectedChangeListener = new OnItemSelectedChangeListener() {

		@Override
		public void onItemAllSelectedChange(boolean allSelected) {
			mAllSelectCheckBox.setChecked(allSelected);
		}
		
	};
	private OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			mHideView.setVisibility(View.VISIBLE);
			mListAdapter.setmIdModeEdit(true);
			return true;
		}
	};
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(!mListAdapter.ismIdModeEdit()){
				CookBook cookBook = mListAdapter.getItem(position);
				Intent intent = new Intent(getActivity(), CookDetailsActivity.class);
				intent.putExtra("cookBook", cookBook);
				getActivity().startActivity(intent);
			}else{
				mListAdapter.toggleItem(position);
			}
		}
	};
	
	private AlertDialog mDialog;
	private void showDeleteDialog(){
		if(mDialog == null){
			Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("ɾ���ղ���ʾ");
			builder.setMessage("ȷ��Ҫɾ����");
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//TODO
					mHelper.deleteCollectCookBook(mListAdapter.getCookBookIds());
					editCancel();
					mListAdapter.updateDatas(mHelper.getCollectCookBooks(null));
				}
			});
			builder.setNegativeButton("ȡ��", null);
			mDialog = builder.create();
		}
		mDialog.show();
	}
	private List<CookBook> cookBooks;
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(!hidden){
			List<CookBook> nearByCookBooks = mHelper.getCollectCookBooks(null);
			if(cookBooks.size() != nearByCookBooks.size()){
				mListAdapter.updateDatas(nearByCookBooks);
				mListView.setSelection(0);
			}
		}
	}


	@Override
	public void onResume() {
		super.onResume();
		List<CookBook> nearByCookBooks = mHelper.getCollectCookBooks(null);
		if(cookBooks.size() != nearByCookBooks.size()){
			mListAdapter.updateDatas(nearByCookBooks);
			mListView.setSelection(0);
		}
	}
}
