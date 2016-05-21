package com.example.teachcooking;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.teachcooking.entity.BasketInfo;
import com.example.teachcooking.entity.BreakfastInfo;
import com.example.teachcooking.entity.CookBookInfo;
import com.example.teachcooking.entity.DinnerInfo;
import com.example.teachcooking.entity.LunchInfo;
import com.example.teachcooking.entity.UsuerInfo;
import com.example.teachcooking.view.CircleImageView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class TeachDetailActivity extends Activity implements OnClickListener {
	private ImageView detail_teaching_cookBookPic;
	private TextView detail_teaching_cookbookname;
	private TextView detail_teaching_cookbookintroduce;
	private CircleImageView detail_teaching_publish_pic;
	private TextView detail_teaching_publish_nick;
	private TextView detail_teaching_publish_address;
	private TextView detail_teaching_publish_createtime;
	private TextView detail_teaching_material_preview;
	private TextView detail_teaching_step1, detail_teaching_step2,
			detail_teaching_step3, detail_teaching_step4;
	private ImageView detail_teaching_step1_pic, detail_teaching_step2_pic,
			detail_teaching_step3_pic, detail_teaching_step4_pic;
	private ImageView return_teaching;
	private RadioButton collect_cookbook;
	private Button join_basket;
	private CookBookInfo cookBookInfo;
	private UsuerInfo athor;
	private TextView activity_detail_tips;
	private UsuerInfo user;
	private UsuerInfo user1;
	private UsuerInfo user2;
	private UsuerInfo user3;
	private BreakfastInfo breakfastInfo;
	private DinnerInfo dinnerInfo;
	private LunchInfo lunchInfo;
	private boolean isCookBookInfo = false;
	private boolean isCollection = false;
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
		setContentView(R.layout.activity_detail_teaching);
		user = BmobUser.getCurrentUser(this, UsuerInfo.class);
		init();
		if (cookBookInfo != null) {
			athor = cookBookInfo.getAuthor();
			isCookBookInfo = true;
		}
		if (isCookBookInfo) {
			qurreyIsCollect();
		}
		if (breakfastInfo != null) {
			user1 = breakfastInfo.getAuthor();
		}
		if (lunchInfo != null) {
			user2 = lunchInfo.getAuthor();
		}
		if (dinnerInfo != null) {
			user3 = dinnerInfo.getAuthor();
		}
		if (athor != null) {
			athor.getPic().loadImage(this, detail_teaching_publish_pic);
			cookBookInfo.getStep_pics().get(0)
					.loadImage(this, detail_teaching_cookBookPic);
			cookBookInfo.getStep_pics().get(1)
					.loadImage(this, detail_teaching_step1_pic);
			cookBookInfo.getStep_pics().get(2)
					.loadImage(this, detail_teaching_step2_pic);
			cookBookInfo.getStep_pics().get(3)
					.loadImage(this, detail_teaching_step3_pic);
			cookBookInfo.getStep_pics().get(4)
					.loadImage(this, detail_teaching_step4_pic);
			detail_teaching_step1.setText(cookBookInfo.getSteps().get(0));
			detail_teaching_step2.setText(cookBookInfo.getSteps().get(1));
			detail_teaching_step3.setText(cookBookInfo.getSteps().get(2));
			detail_teaching_step4.setText(cookBookInfo.getSteps().get(3));
			activity_detail_tips.setText(cookBookInfo.getTip());
			detail_teaching_cookbookname
					.setText(cookBookInfo.getCookBookName());
			detail_teaching_cookbookintroduce.setText(cookBookInfo
					.getCookBookIntroduce());
			detail_teaching_publish_nick.setText(athor.getNick() + ",发布于");
			detail_teaching_publish_address.setText(cookBookInfo.getAdrress());
			detail_teaching_material_preview.setText(cookBookInfo
					.getCookBookMaterial());
			detail_teaching_publish_createtime.setText(cookBookInfo
					.getCreatedAt());
		} else if (user2 != null) {
			user2.getPic().loadImage(this, detail_teaching_publish_pic);
			lunchInfo.getStep_pics().get(0)
					.loadImage(this, detail_teaching_cookBookPic);
			lunchInfo.getStep_pics().get(1)
					.loadImage(this, detail_teaching_step1_pic);
			lunchInfo.getStep_pics().get(2)
					.loadImage(this, detail_teaching_step2_pic);
			lunchInfo.getStep_pics().get(3)
					.loadImage(this, detail_teaching_step3_pic);
			lunchInfo.getStep_pics().get(4)
					.loadImage(this, detail_teaching_step4_pic);
			detail_teaching_step1.setText(lunchInfo.getSteps().get(0));
			detail_teaching_step2.setText(lunchInfo.getSteps().get(1));
			detail_teaching_step3.setText(lunchInfo.getSteps().get(2));
			detail_teaching_step4.setText(lunchInfo.getSteps().get(3));
			activity_detail_tips.setText(lunchInfo.getTip());
			detail_teaching_cookbookname.setText(lunchInfo.getCookBookName());
			detail_teaching_cookbookintroduce.setText(lunchInfo
					.getCookBookIntroduce());
			detail_teaching_publish_nick.setText(user2.getNick());
			detail_teaching_material_preview.setText(lunchInfo
					.getCookBookMaterial());
			detail_teaching_publish_createtime
					.setText(lunchInfo.getCreatedAt());
			join_basket.setVisibility(View.GONE);
			collect_cookbook.setVisibility(View.GONE);
		} else if (user1 != null) {
			user1.getPic().loadImage(this, detail_teaching_publish_pic);
			breakfastInfo.getStep_pics().get(0)
					.loadImage(this, detail_teaching_cookBookPic);
			breakfastInfo.getStep_pics().get(1)
					.loadImage(this, detail_teaching_step1_pic);
			breakfastInfo.getStep_pics().get(2)
					.loadImage(this, detail_teaching_step2_pic);
			breakfastInfo.getStep_pics().get(3)
					.loadImage(this, detail_teaching_step3_pic);
			breakfastInfo.getStep_pics().get(4)
					.loadImage(this, detail_teaching_step4_pic);
			detail_teaching_step1.setText(breakfastInfo.getSteps().get(0));
			detail_teaching_step2.setText(breakfastInfo.getSteps().get(1));
			detail_teaching_step3.setText(breakfastInfo.getSteps().get(2));
			detail_teaching_step4.setText(breakfastInfo.getSteps().get(3));
			activity_detail_tips.setText(breakfastInfo.getTip());
			detail_teaching_cookbookname.setText(breakfastInfo
					.getCookBookName());
			detail_teaching_cookbookintroduce.setText(breakfastInfo
					.getCookBookIntroduce());
			detail_teaching_publish_nick.setText(user1.getNick());
			detail_teaching_material_preview.setText(breakfastInfo
					.getCookBookMaterial());
			detail_teaching_publish_createtime.setText(breakfastInfo
					.getCreatedAt());
			join_basket.setVisibility(View.GONE);
			collect_cookbook.setVisibility(View.GONE);
		} else if (user3 != null) {
			user3.getPic().loadImage(this, detail_teaching_publish_pic);
			dinnerInfo.getStep_pics().get(0)
					.loadImage(this, detail_teaching_cookBookPic);
			dinnerInfo.getStep_pics().get(1)
					.loadImage(this, detail_teaching_step1_pic);
			dinnerInfo.getStep_pics().get(2)
					.loadImage(this, detail_teaching_step2_pic);
			dinnerInfo.getStep_pics().get(3)
					.loadImage(this, detail_teaching_step3_pic);
			dinnerInfo.getStep_pics().get(4)
					.loadImage(this, detail_teaching_step4_pic);
			detail_teaching_step1.setText(dinnerInfo.getSteps().get(0));
			detail_teaching_step2.setText(dinnerInfo.getSteps().get(1));
			detail_teaching_step3.setText(dinnerInfo.getSteps().get(2));
			detail_teaching_step4.setText(dinnerInfo.getSteps().get(3));
			activity_detail_tips.setText(dinnerInfo.getTip());
			detail_teaching_cookbookname.setText(dinnerInfo.getCookBookName());
			detail_teaching_cookbookintroduce.setText(dinnerInfo
					.getCookBookIntroduce());
			detail_teaching_publish_nick.setText(user3.getNick());
			detail_teaching_material_preview.setText(dinnerInfo
					.getCookBookMaterial());
			detail_teaching_publish_createtime.setText(dinnerInfo
					.getCreatedAt());
			join_basket.setVisibility(View.GONE);
			collect_cookbook.setVisibility(View.GONE);
		}

	}

	private void init() {
		cookBookInfo = (CookBookInfo) getIntent().getSerializableExtra(
				"cookBookInfo");
		dinnerInfo = (DinnerInfo) getIntent()
				.getSerializableExtra("dinnerInfo");
		lunchInfo = (LunchInfo) getIntent().getSerializableExtra("lunchInfo");
		breakfastInfo = (BreakfastInfo) getIntent().getSerializableExtra(
				"breakfastInfo");
		return_teaching = (ImageView) findViewById(R.id.return_teaching);
		detail_teaching_step1_pic = (ImageView) findViewById(R.id.detail_teaching_step1_pic);
		detail_teaching_step2_pic = (ImageView) findViewById(R.id.detail_teaching_step2_pic);
		detail_teaching_step3_pic = (ImageView) findViewById(R.id.detail_teaching_step3_pic);
		detail_teaching_step4_pic = (ImageView) findViewById(R.id.detail_teaching_step4_pic);
		detail_teaching_cookBookPic = (ImageView) findViewById(R.id.detail_teaching_cookBookPic);
		detail_teaching_cookbookname = (TextView) findViewById(R.id.detail_teaching_cookbookname);
		detail_teaching_cookbookintroduce = (TextView) findViewById(R.id.detail_teaching_cookbookintroduce);
		detail_teaching_publish_pic = (CircleImageView) findViewById(R.id.detail_teaching_publish_pic);
		detail_teaching_publish_nick = (TextView) findViewById(R.id.detail_teaching_publish_nick);
		detail_teaching_publish_address = (TextView) findViewById(R.id.detail_teaching_publish_address);
		detail_teaching_publish_createtime = (TextView) findViewById(R.id.detail_teaching_publish_createtime);
		detail_teaching_material_preview = (TextView) findViewById(R.id.detail_teaching_material_preview);
		activity_detail_tips = (TextView) findViewById(R.id.activity_detail_tips);
		detail_teaching_step1 = (TextView) findViewById(R.id.detail_teaching_step1);
		detail_teaching_step2 = (TextView) findViewById(R.id.detail_teaching_step2);
		detail_teaching_step3 = (TextView) findViewById(R.id.detail_teaching_step3);
		detail_teaching_step4 = (TextView) findViewById(R.id.detail_teaching_step4);
		collect_cookbook = (RadioButton) findViewById(R.id.collect_cookbook);
		collect_cookbook.setOnClickListener(this);
		return_teaching.setOnClickListener(this);
		join_basket = (Button) findViewById(R.id.join_basket);
		join_basket.setOnClickListener(this);
	}

	public void qurreyIsCollect() {
		BmobQuery<UsuerInfo> query = new BmobQuery<UsuerInfo>();
		CookBookInfo cookBookInfo = new CookBookInfo();
		cookBookInfo.setObjectId(this.cookBookInfo.getObjectId());
		query.addWhereRelatedTo("likes", new BmobPointer(cookBookInfo));
		query.findObjects(this, new FindListener<UsuerInfo>() {
			@Override
			public void onSuccess(List<UsuerInfo> arg0) {
				for (UsuerInfo usuerInfo : arg0) {
					if (usuerInfo.getObjectId().equals(user.getObjectId())) {
						collect_cookbook.setChecked(true);
						collect_cookbook.setText("取消收藏");
						isCollection = true;
						return;
					}
				}
			}
			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(TeachDetailActivity.this, "请检查网络",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.return_teaching:
			finish();
			break;
		case R.id.join_basket:
			if (user != null) {
				BasketInfo basketInfo = new BasketInfo();
				basketInfo.setUser(user);
				basketInfo.setCookBookInfo(cookBookInfo);
				basketInfo.setAuthor(athor);
				basketInfo.save(this, new SaveListener() {
					@Override
					public void onSuccess() {
						Toast.makeText(TeachDetailActivity.this, "加入菜篮成功",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(int code, String arg1) {
						Toast.makeText(TeachDetailActivity.this, "加入失败，请检查网络",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
			break;
		case R.id.collect_cookbook:
			if (user != null) {
				if (isCookBookInfo) {
					if (isCollection) {
						UsuerInfo usuerInfo = new UsuerInfo();
						usuerInfo.setObjectId(user.getObjectId());
						BmobRelation relation = new BmobRelation();
						CookBookInfo cookBookInfo = new CookBookInfo();
						cookBookInfo.setObjectId(this.cookBookInfo.getObjectId());
						relation.remove(cookBookInfo);
						usuerInfo.setLikes(relation);
						usuerInfo.update(this, new UpdateListener() {
							
							@Override
							public void onSuccess() {
								CookBookInfo cookBookInfo = new CookBookInfo();
								cookBookInfo.setObjectId(TeachDetailActivity.this.cookBookInfo.getObjectId());
								BmobRelation relation = new BmobRelation();
								UsuerInfo usuerInfo = new UsuerInfo();
								usuerInfo.setObjectId(user.getObjectId());
								relation.remove(usuerInfo);
								cookBookInfo.setLikes(relation);
								cookBookInfo.update(TeachDetailActivity.this, new UpdateListener() {
									
									@Override
									public void onSuccess() {
										isCollection =false;
										collect_cookbook.setChecked(false);
										collect_cookbook.setText("收藏");
										Toast.makeText(TeachDetailActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
									}
									
									@Override
									public void onFailure(int arg0, String arg1) {
										Toast.makeText(TeachDetailActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
									}
								});
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								Toast.makeText(TeachDetailActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
							}
						});
					}else {
						UsuerInfo usuerInfo = new UsuerInfo();
						usuerInfo.setObjectId(user.getObjectId());
						BmobRelation relation = new BmobRelation();
						CookBookInfo cookBookInfo = new CookBookInfo();
						cookBookInfo.setObjectId(this.cookBookInfo.getObjectId());
						relation.add(cookBookInfo);
						usuerInfo.setLikes(relation);
						usuerInfo.update(this, new UpdateListener() {
							
							@Override
							public void onSuccess() {
								CookBookInfo cookBookInfo = new CookBookInfo();
								cookBookInfo.setObjectId(TeachDetailActivity.this.cookBookInfo.getObjectId());
								//将用户user添加到CookBookInfo表中的likes字段值中，表明用户B喜欢该帖
								BmobRelation relation = new BmobRelation();
								UsuerInfo user = new UsuerInfo();
								user.setObjectId(TeachDetailActivity.this.user.getObjectId());
								relation.add(user);
								cookBookInfo.setLikes(relation);
								cookBookInfo.update(TeachDetailActivity.this, new UpdateListener() {
									
									@Override
									public void onSuccess() {
										Toast.makeText(TeachDetailActivity.this, "收藏成功",
												Toast.LENGTH_SHORT).show();
										collect_cookbook.setChecked(true);
										collect_cookbook.setText("取消收藏");
										isCollection =true;
									}
									@Override
									public void onFailure(int arg0, String arg1) {
										Toast.makeText(TeachDetailActivity.this, "请检查网络",
												Toast.LENGTH_SHORT).show();
									}
								});
							}		
							@Override
							public void onFailure(int arg0, String arg1) {
								Toast.makeText(TeachDetailActivity.this, "请检查网络",
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				}
			}
			break;
		}
	}
}
