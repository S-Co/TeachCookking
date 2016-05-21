package com.example.teachcooking;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

import com.example.teachcooking.adpter.CollectActivityAdapter;
import com.example.teachcooking.entity.CookBookInfo;
import com.example.teachcooking.entity.UsuerInfo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;

public class CollectActivity extends Activity implements OnItemClickListener {
	private ListView activity_collect_lv;
	private CollectActivityAdapter adapter;
	private UsuerInfo usuerInfo;
	private ImageView collect_back;
	private ArrayList<CookBookInfo> cookBookInfos;

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
		setContentView(R.layout.activity_collect);
		collect_back = (ImageView) findViewById(R.id.collect_back);
		activity_collect_lv = (ListView) findViewById(R.id.activity_collect_lv);
		activity_collect_lv.setDividerHeight(0);
		collect_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		activity_collect_lv.setOnItemClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter = new CollectActivityAdapter(this,
				new ArrayList<CookBookInfo>());
		activity_collect_lv.setAdapter(adapter);
		cookBookInfos = new ArrayList<CookBookInfo>();
		usuerInfo = BmobUser.getCurrentUser(this, UsuerInfo.class);
		if (usuerInfo != null) {
			BmobQuery<CookBookInfo> query = new BmobQuery<CookBookInfo>();
			UsuerInfo usuerInfo = new UsuerInfo();
			usuerInfo.setObjectId(this.usuerInfo.getObjectId());
			query.addWhereRelatedTo("likes", new BmobPointer(usuerInfo));
			query.order("-createdAt");
			query.include("author");
			query.findObjects(CollectActivity.this,
					new FindListener<CookBookInfo>() {
						@Override
						public void onSuccess(List<CookBookInfo> arg0) {
							List<CookBookInfo> list = arg0;
							for (CookBookInfo basketInfo : list) {
								UsuerInfo author = basketInfo.getAuthor();
								CookBookInfo cookBookInfo = basketInfo;
								cookBookInfo.setAuthor(author);
								cookBookInfos.add(cookBookInfo);
							}
							adapter.setCookBookInfos(cookBookInfos);
						}

						@Override
						public void onError(int arg0, String arg1) {
							Toast.makeText(CollectActivity.this,
									arg0 + "失败" + arg1, Toast.LENGTH_SHORT)
									.show();
						}
					});
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		UsuerInfo user = BmobUser.getCurrentUser(CollectActivity.this,
				UsuerInfo.class);
		if (user != null) {
			CookBookInfo cookBookInfo = adapter.getItem(position);
			Intent intent = new Intent(CollectActivity.this,
					TeachDetailActivity.class);
			intent.putExtra("cookBookInfo", cookBookInfo);
			startActivity(intent);
		}
	}
}
