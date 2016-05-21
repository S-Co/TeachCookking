package com.example.teachcooking;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.example.teachcooking.db.ProvinceHelper;
import com.example.teachcooking.entity.Province;
import com.example.teachcooking.entity.UsuerInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class EditUsuerInfoActivity extends Activity implements OnClickListener {
	private ImageView back_setting;
	private ImageView upload_new_user_pic;
	private TextView save_edit_information;
	private TextView edit_birthday;
	private TextView edit_home;
	private EditText edit_nick;
	private EditText edit_self_introduce;
	private RadioButton sex_man;
	private RadioButton sex_woman;
	private LinearLayout layout;
	private boolean scrolling = false;
	private boolean cscrolling = false;
	private Button surebButton;
	private Button cancleButton;
	private CountryAdapter adapter;
	private ProvinceHelper helper;
	private ArrayList<Province> mProvinces_List;
	private ArrayList<String> countys = new ArrayList<String>();
	private Dialog dialog = null;
	private String[][] citys;
	// 控制数组第几行
	private int j = 0;
	private int i = 0;
	private int countryindex;
	private int cityindex;
	// 年
	private int mYear;
	// 月
	private int mMonth;
	// 天
	private int mDay;
	private File tempFile;

	private Bitmap bitmap;

	/* 头像名称 */
	private static final String PHOTO_FILE_NAME = "upload_temp_photo.jpg";

	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	private ProgressDialog progressDialog;

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
		setContentView(R.layout.activity_edit_info);
		UsuerInfo usuerInfo = BmobUser.getCurrentUser(this, UsuerInfo.class);
		selectHome();

		init(usuerInfo);
	}

	private void init(UsuerInfo usuerInfo) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		upload_new_user_pic = (ImageView) findViewById(R.id.upload_new_user_pic);
		save_edit_information = (TextView) findViewById(R.id.save_edit_information);
		edit_birthday = (TextView) findViewById(R.id.edit_birthday);
		edit_home = (TextView) findViewById(R.id.edit_home);
		edit_nick = (EditText) findViewById(R.id.edit_nick);
		edit_self_introduce = (EditText) findViewById(R.id.edit_self_introduce);
		save_edit_information = (TextView) findViewById(R.id.save_edit_information);
		sex_man = (RadioButton) findViewById(R.id.sex_man);
		sex_woman = (RadioButton) findViewById(R.id.sex_woman);
		back_setting = (ImageView) findViewById(R.id.back_setting);
		upload_new_user_pic.setOnClickListener(this);
		edit_home.setOnClickListener(this);
		back_setting.setOnClickListener(this);
		edit_birthday.setOnClickListener(this);
		save_edit_information.setOnClickListener(this);
		if (usuerInfo != null) {
			usuerInfo.getPic().loadImage(this, upload_new_user_pic);
			edit_nick.setText(usuerInfo.getNick());
			if (usuerInfo.getBirthday() != null) {
				edit_birthday.setText(usuerInfo.getBirthday());
			}
			if (usuerInfo.getHometown() != null) {
				edit_home.setText(usuerInfo.getHometown());
			}
			if (usuerInfo.getSelfIntroduction() != null) {
				edit_self_introduce.setText(usuerInfo.getSelfIntroduction());
			}
			if (usuerInfo.getSex() != null) {
				if (usuerInfo.getSex().equals("男")) {
					sex_man.setChecked(true);
				} else if (usuerInfo.getSex().equals("女")) {
					sex_woman.setChecked(true);
				}
			}
		}
	}

	// 选择家乡
	private void selectHome() {
		helper = new ProvinceHelper();
		mProvinces_List = helper.getProvince();
		citys = new String[mProvinces_List.size()][];
		for (Province province : mProvinces_List) {
			countys.add(province.getProvice());
			String[] c = new String[province.getCity().size()];
			for (String string : province.getCity()) {
				c[i] = string;
				i++;
			}
			citys[j] = c;
			j++;
			i = 0;
		}
		adapter = new CountryAdapter(this, countys);
		back_setting = (ImageView) findViewById(R.id.back_setting);
		// 选择城市
		layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.cities_layout, null);
		dialog = new AlertDialog.Builder(EditUsuerInfoActivity.this).create();
		surebButton = (Button) layout.findViewById(R.id.sure_change);
		cancleButton = (Button) layout.findViewById(R.id.cancle);
		surebButton.setOnClickListener(EditUsuerInfoActivity.this);
		cancleButton.setOnClickListener(EditUsuerInfoActivity.this);
		final WheelView country = (WheelView) layout.findViewById(R.id.country);
		country.setVisibleItems(3);
		country.setViewAdapter(adapter);
		final WheelView city = (WheelView) layout.findViewById(R.id.city);
		city.setVisibleItems(3);
		city.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!cscrolling) {
				}
			}
		});
		city.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				cscrolling = true;
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				cscrolling = false;
				cityindex = city.getCurrentItem();
			}
		});
		country.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!scrolling) {
					updateCities(city, citys, newValue);
				}
			}
		});
		country.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				scrolling = true;
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				scrolling = false;
				countryindex = country.getCurrentItem();
				updateCities(city, citys, country.getCurrentItem());
			}
		});
		// 设置当前可见为第一个
		country.setCurrentItem(1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edit_home:
			if (dialog != null) {
				dialog.show();
				dialog.getWindow().setContentView(layout);
			}
			break;
		case R.id.cancle:
			dialog.dismiss();
			break;
		case R.id.sure_change:
			dialog.dismiss();
			edit_home.setText(countys.get(countryindex)
					+ citys[countryindex][cityindex]);
			break;
		case R.id.edit_birthday:
			Calendar calendar = Calendar.getInstance();
			new DatePickerDialog(
					EditUsuerInfoActivity.this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							mYear = year;
							mMonth = monthOfYear;
							mDay = dayOfMonth;
							edit_birthday
									.setText(new StringBuilder()
											.append(mYear)
											.append(" - ")
											.append((mMonth + 1) < 10 ? 0 + (mMonth + 1)
													: (mMonth + 1))
											.append(" - ")
											.append((mDay < 10) ? 0 + mDay
													: mDay));
						}
					}, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.back_setting:
			finish();
			break;
		case R.id.upload_new_user_pic:
			new PopupWindows(EditUsuerInfoActivity.this, upload_new_user_pic);
			break;
		case R.id.save_edit_information:
			progressDialog.show();
			if (picFile.exists()) {
				BmobProFile.getInstance(this).upload(picFile.getAbsolutePath(),
						new UploadListener() {

							@Override
							public void onError(int statuscode, String erromsg) {
								dialog.dismiss();
							}

							@Override
							public void onSuccess(String fileName, String url,
									BmobFile file) {
								UsuerInfo bmobUser = BmobUser.getCurrentUser(
										EditUsuerInfoActivity.this,
										UsuerInfo.class);
								bmobUser.setPic(file);
								bmobUser.setHometown(edit_home.getText()
										.toString());
								bmobUser.setNick(edit_nick.getText().toString());
								bmobUser.setSelfIntroduction(edit_self_introduce
										.getText().toString());
								bmobUser.setBirthday(edit_birthday.getText()
										.toString());
								if (sex_man.isChecked()) {
									bmobUser.setSex("男");
								} else if (sex_woman.isChecked()) {
									bmobUser.setSex("女");
								} else {
									bmobUser.setSex("");
								}
								bmobUser.update(EditUsuerInfoActivity.this,
										bmobUser.getObjectId(),
										new UpdateListener() {

											@Override
											public void onSuccess() {
												dialog.dismiss();
												Toast.makeText(
														EditUsuerInfoActivity.this,
														"更新成功",
														Toast.LENGTH_SHORT)
														.show();
											}

											@Override
											public void onFailure(int arg0,
													String arg1) {
												dialog.dismiss();
											}
										});
							}

							@Override
							public void onProgress(int arg0) {

							}
						});
			} else {
				UsuerInfo bmobUser = BmobUser.getCurrentUser(
						EditUsuerInfoActivity.this, UsuerInfo.class);
				bmobUser.setHometown(edit_home.getText().toString());
				bmobUser.setNick(edit_nick.getText().toString());
				bmobUser.setSelfIntroduction(edit_self_introduce.getText()
						.toString());
				bmobUser.setBirthday(edit_birthday.getText().toString());
				if (sex_man.isChecked()) {
					bmobUser.setSex("男");
				} else if (sex_woman.isChecked()) {
					bmobUser.setSex("女");
				} else {
					bmobUser.setSex("");
				}
				bmobUser.update(EditUsuerInfoActivity.this,
						bmobUser.getObjectId(), new UpdateListener() {

							@Override
							public void onSuccess() {
								dialog.dismiss();
								Toast.makeText(EditUsuerInfoActivity.this,
										"更新成功", Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onFailure(int arg0, String arg1) {
								dialog.dismiss();
							}
						});
			}
			break;
		}
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
				Toast.makeText(EditUsuerInfoActivity.this, "未找到存储卡，无法存储照片！",
						Toast.LENGTH_SHORT).show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			bitmap = data.getParcelableExtra("data");
			this.upload_new_user_pic.setImageBitmap(bitmap);
			// boolean delete = tempFile.delete();
			// getUptoken();
			File picureFileDir = new File(
					Environment.getExternalStorageDirectory(), "/upload");
			if (!picureFileDir.exists()) {
				picureFileDir.mkdirs();
			}
			picFile = new File(picureFileDir, "upload_upload.jpeg");
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

	// 更新城市的方法
	private void updateCities(WheelView city, String cities[][], int index) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
				cities[index]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(cities[index].length / 2);
		cityindex = city.getCurrentItem();
	}

	// 城市选择adapter
	public class CountryAdapter extends AbstractWheelTextAdapter {
		private ArrayList<String> countrys;

		protected CountryAdapter(Context context, ArrayList<String> countrys) {
			super(context, R.layout.country_layout, NO_RESOURCE);
			setItemTextResource(R.id.country_name);
			this.countrys = countrys;
		}

		@Override
		public View getItem(int index, View convertView, ViewGroup parent) {
			return super.getItem(index, convertView, parent);
		}

		@Override
		public int getItemsCount() {

			return countrys.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return countrys.get(index);
		}
	}

}
