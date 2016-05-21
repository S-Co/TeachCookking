package com.example.teachcooking;

import cn.bmob.v3.BmobUser;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.example.teachcooking.entity.UsuerInfo;
import com.example.teachcooking.util.DataCleanManager;
import com.example.teachcooking.view.CircleImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnClickListener {
	private UsuerInfo usuerInfo;
	private TextView setting_nick_name, setting_usuer_phone;
	private CircleImageView setting_pic;
	private LinearLayout usuer_information;
	private Button activity_setting_log_out;
	private ImageView back_mine;
	private TextView about_teachcooking;
	private TextView usuer_manager;
	private TextView change_password;
	private TextView clean_cache;
	private TextView share_soft;
	private TextView help_teachcooking;

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
		setContentView(R.layout.activity_setting);
		help_teachcooking = (TextView) findViewById(R.id.help_teachcooking);
		share_soft = (TextView) findViewById(R.id.share_soft_setting);
		about_teachcooking = (TextView) findViewById(R.id.about_teachcooking);
		back_mine = (ImageView) findViewById(R.id.back_mine);
		change_password = (TextView) findViewById(R.id.change_password);
		activity_setting_log_out = (Button) findViewById(R.id.activity_setting_log_out);
		setting_nick_name = (TextView) findViewById(R.id.setting_nick_name);
		setting_usuer_phone = (TextView) findViewById(R.id.setting_usuer_phone);
		setting_pic = (CircleImageView) findViewById(R.id.setting_pic);
		usuer_manager = (TextView) findViewById(R.id.usuer_manager);
		clean_cache = (TextView) findViewById(R.id.clean_cache);
		usuer_information = (LinearLayout) findViewById(R.id.usuer_information);
		usuer_information.setOnClickListener(this);
		help_teachcooking.setOnClickListener(this);
		change_password.setOnClickListener(this);
		share_soft.setOnClickListener(this);
		clean_cache.setOnClickListener(this);
		about_teachcooking.setOnClickListener(this);
		activity_setting_log_out.setOnClickListener(this);
		usuer_manager.setOnClickListener(this);
		back_mine.setOnClickListener(this);
		usuerInfo = new UsuerInfo();
		usuerInfo = BmobUser.getCurrentUser(this, UsuerInfo.class);
		if (usuerInfo != null) {
			setting_nick_name.setText(usuerInfo.getNick());
			setting_usuer_phone.setText(usuerInfo.getMobilePhoneNumber());
			usuerInfo.getPic().loadImage(this, setting_pic);
		}
	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("教做菜");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://www.baidu.com");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我在教做菜里面做了一美味的菜，赶紧来试试吧！");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://www.baidu.com");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我在教做菜里面做了一美味的菜，赶紧来试试吧！");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://www.baidu.com");

		// 启动分享GUI
		oks.show(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.help_teachcooking:
			startActivity(new Intent(SettingActivity.this, HelpActivity.class));
			finish();
			break;
		case R.id.share_soft_setting:
			showShare();
			break;
		case R.id.change_password:
			Intent intent_change_password = new Intent(SettingActivity.this,
					ChangePasswordActivity.class);
			startActivity(intent_change_password);
			break;
		case R.id.about_teachcooking:
			Intent intent_about = new Intent(this, AboutActivity.class);
			startActivity(intent_about);
			break;
		case R.id.usuer_information:
			Intent intent = new Intent(this, EditUsuerInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.activity_setting_log_out:
			AlertDialog.Builder dialg = new Builder(this);
			dialg.setTitle("退出登录");
			dialg.setMessage("确定退出？");
			dialg.setPositiveButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialg.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (usuerInfo != null) {
								BmobUser.logOut(SettingActivity.this);
								Toast.makeText(SettingActivity.this, "退出成功",
										Toast.LENGTH_SHORT).show();
								finish();
							}
						}
					});
			dialg.create().show();
			break;
		case R.id.back_mine:
			finish();
			break;
		case R.id.usuer_manager:
			Intent intent_manager = new Intent(this,
					ManageAccountActivity.class);
			startActivity(intent_manager);
			break;
		case R.id.clean_cache:
			AlertDialog.Builder dialog = new Builder(this);
			dialog.setTitle("清除缓存");
			dialog.setMessage("确定清除?");
			dialog.setPositiveButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							DataCleanManager.cleanDatabaseByName(
									SettingActivity.this, "jiao_zuo_cai.db");
							DataCleanManager.cleanApplicationData(
									SettingActivity.this, Environment
											.getExternalStorageDirectory()
											.getAbsolutePath()
											+ "/teachcooking/cache/img");
							dialog.dismiss();
						}
					});
			dialog.create().show();
			break;
		}
	}
}
