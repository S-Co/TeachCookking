package com.example.teachcooking.view;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * ���㲢����ListView�ĸ߶�
 * ���㲢����GridView�ĸ߶�
 * @author Li
 *
 */
public class ViewUtil {
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		//��ȡListView��Ӧ��Adapter
		ListAdapter listAdapter = listView.getAdapter(); 
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { //listAdapter.getCount()�������������Ŀ
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); //��������View �Ŀ��
			totalHeight += listItem.getMeasuredHeight(); //ͳ������������ܸ߶�
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		//listView.getDividerHeight()��ȡ�����ָ���ռ�õĸ߶�
		//params.height���õ�����ListView������ʾ��Ҫ�ĸ߶�
		listView.setLayoutParams(params);
	}
	@SuppressLint("NewApi")
	public static void setGridViewHeightBasedOnChildren(GridView gridView,int numColumns) {
		//��ȡListView��Ӧ��Adapter
		ListAdapter listAdapter = gridView.getAdapter(); 
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { //listAdapter.getCount()�������������Ŀ
			if(i % numColumns == 0){
				View listItem = listAdapter.getView(i, null, gridView);
				listItem.measure(0, 0); //��������View �Ŀ��
				totalHeight += listItem.getMeasuredHeight(); //ͳ������������ܸ߶�
			}
		}
		
		//������ֱ���
		
		int lines = listAdapter.getCount()/numColumns;
		if(listAdapter.getCount() % numColumns != 0){
			lines++;
		}
		totalHeight += lines*gridView.getVerticalSpacing();
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight;
		//listView.getDividerHeight()��ȡ�����ָ���ռ�õĸ߶�
		//params.height���õ�����ListView������ʾ��Ҫ�ĸ߶�
		gridView.setLayoutParams(params);
	}


}
