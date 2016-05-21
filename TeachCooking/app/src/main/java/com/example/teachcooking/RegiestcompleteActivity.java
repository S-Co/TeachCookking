package com.example.teachcooking;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.example.teachcooking.entity.UsuerInfo;
import com.example.teachcooking.view.CircleImageView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class RegiestcompleteActivity extends Activity implements
		OnClickListener {
	private File tempFile;

	private Bitmap bitmap;

	/* 头像名称 */
	private static final String PHOTO_FILE_NAME = "temp_photo.jpg";

	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	private CircleImageView usuer_image;
	/**
	 * 手机号
	 * */
	private String phone;
	private Button complete;
	/**
	 * 用户名
	 * */
	private EditText regiest_complete_usuer_name;
	/**
	 * 密码
	 * */
	private EditText regiest_complete_usuer_password;
	private ProgressDialog dialog;

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
		setContentView(R.layout.regiest_complete);
		dialog = new ProgressDialog(this);
		dialog.setIndeterminate(false);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		phone = getIntent().getStringExtra("phone");
		regiest_complete_usuer_name = (EditText) findViewById(R.id.regiest_complete_usuer_name);
		regiest_complete_usuer_password = (EditText) findViewById(R.id.regiest_complete_usuer_password);
		usuer_image = (CircleImageView) findViewById(R.id.usuer_image);
		complete = (Button) findViewById(R.id.complete);
		usuer_image.setOnClickListener(this);
		complete.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.usuer_image:
			new PopupWindows(RegiestcompleteActivity.this, usuer_image);
			break;
		case R.id.complete:
			dialog.show();
			BmobProFile.getInstance(this).upload(picFile.getAbsolutePath(),
					new UploadListener() {

						@Override
						public void onError(int statuscode, String erromsg) {
							dialog.dismiss();
						}

						@Override
						public void onSuccess(String fileName, String url,
								BmobFile file) {
							UsuerInfo usuerInfo = new UsuerInfo();
							usuerInfo.setUsername(phone);
							usuerInfo.setPassword(regiest_complete_usuer_password
									.getText().toString());
							usuerInfo.setNick(regiest_complete_usuer_name.getText()
									.toString());
							usuerInfo.setMobilePhoneNumber(phone);
							usuerInfo.setMobilePhoneNumberVerified(true);
							usuerInfo.setBirthday("");
							usuerInfo.setSelfIntroduction("");
							usuerInfo.setSex("");
							usuerInfo.setHometown("");
							usuerInfo.setPic(file);
							insertUser(usuerInfo);
							dialog.dismiss();
						}

						@Override
						public void onProgress(int arg0) {

						}
					});
			break;
		}
	}

	private void insertUser(UsuerInfo usuerInfo) {
		usuerInfo.signUp(this, new SaveListener() {

			@Override
			public void onSuccess() {
				Toast.makeText(RegiestcompleteActivity.this, "注册成功",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(RegiestcompleteActivity.this,MenuActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(RegiestcompleteActivity.this, "注册失败",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 本地相册选择
	 */
	public static File picFile = null;

	public Intent getCropImageIntent() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/*");
		setIntentParams(intent);
		return intent;
	}

	private void setIntentParams(Intent intent) {

		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 600);
		intent.putExtra("outputY", 600);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
	}

	// 打开本地相册或打开相机
	private Uri photoUri;

	public class PopupWindows extends PopupWindow {

		@SuppressWarnings("deprecation")
		public PopupWindows(Context mContext, View parent) {

			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					Intent intent = new Intent(
							"android.media.action.IMAGE_CAPTURE");
					// 判断存储卡是否可以用，可用进行存储
					if (hasSdcard()) {
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								.fromFile(new File(Environment
										.getExternalStorageDirectory(),
										PHOTO_FILE_NAME)));
					}
					startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					// 激活系统图库，选择一张图片
					Intent intent = new Intent(Intent.ACTION_PICK);
					intent.setType("image/*");
					startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
				crop(uri);
			}

		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (hasSdcard()) {
				tempFile = new File(Environment.getExternalStorageDirectory(),
						PHOTO_FILE_NAME);
				crop(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(RegiestcompleteActivity.this, "未找到存储卡，无法存储照片！",
						Toast.LENGTH_SHORT).show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			bitmap = data.getParcelableExtra("data");
			this.usuer_image.setImageBitmap(bitmap);
			// boolean delete = tempFile.delete();
			// getUptoken();
			File picureFileDir = new File(
					Environment.getExternalStorageDirectory(), "/upload");
			if (!picureFileDir.exists()) {
				picureFileDir.mkdirs();
			}
			picFile = new File(picureFileDir, "upload.jpeg");
			if (!picFile.exists()) {
				try {
					picFile.createNewFile();
					FileOutputStream fos = new FileOutputStream(picFile);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				picFile.delete();
				FileOutputStream fos;
				try {
					picFile.createNewFile();
					fos = new FileOutputStream(picFile);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			photoUri = Uri.fromFile(picFile);
			// getUptoken();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void crop(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		// 图片格式
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);// true:不返回uri，false：返回uri

		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}
}
