package com.example.teachcooking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.example.teachcooking.entity.BreakfastInfo;
import com.example.teachcooking.entity.CookBookInfo;
import com.example.teachcooking.entity.DinnerInfo;
import com.example.teachcooking.entity.LunchInfo;
import com.example.teachcooking.entity.UsuerInfo;
import com.example.teachcooking.util.BitmapTools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ImageView.ScaleType;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PublishCookbookActivity extends Activity implements
		OnClickListener {
	private UsuerInfo usuerInfo;
	private static final int PHOTO_WITH_DATA = 18; // 从SD卡中得到图片
	private static final int PHOTO_WITH_CAMERA = 37;// 拍摄照片
	// 菜谱封面图
	private ImageView cookBookphoto;
	// 菜谱名
	private TextView publish_name;
	private String publish_nameString;
	// 菜谱简介
	private TextView add_simple_intro;
	private String add_simple_introString;
	// 添加用料
	private TextView add_material;
	private String add_materialString;
	// 步骤一二三四图片
	private ImageView[] step_pics = new ImageView[4];
	// 步骤一二三四文字描述
	private TextView[] add_steps = new TextView[4];
	private String[] add_stepsStrings = new String[4];
	// 添加小贴士
	private TextView tips;
	private String tipString;
	// 发布按钮
	private Button pulish;
	private ImageView temp_iv;
	private String tempName;
	private ArrayList<String> steps = new ArrayList<String>();
	private ProgressDialog dialog;
	private ArrayList<BmobFile> step_pic = new ArrayList<BmobFile>();
	private LocationClient locationClient = null;
	// private ArrayList<String> tempSteps = new ArrayList<String>();
	// 发布者地址
	private String adrress;
	private Spinner spinner;
	// 发布类型
	private String timeType;
	private String content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.publish_cookbook);
		locationClient = new LocationClient(getApplicationContext());
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setOpenGps(true); // 是否打开GPS
		option.setCoorType("bd09ll"); // 设置返回值的坐标类型
		option.setProdName("teachcooking"); // 设置产品线名称。
		// option.setScanSpan(UPDATE_TIME); // 设置定时定位的时间间隔。单位毫秒
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		locationClient.setLocOption(option);
		// 注册位置监听器
		locationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				adrress = location.getAddrStr();
			}
		});
		locationClient.start();
		/**
		 * 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。 调用requestLocation(
		 * )后，每隔设定的时间，定位SDK就会进行一次定位。 如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，
		 * 返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
		 * 定时定位时，调用一次requestLocation，会定时监听到定位结果。
		 */
		locationClient.requestLocation();
		init();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.stop();
			locationClient = null;
		}
	}

	private void init() {
		usuerInfo = BmobUser.getCurrentUser(this, UsuerInfo.class);
		dialog = new ProgressDialog(this);
		dialog.setIndeterminate(false);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		spinner = (Spinner) findViewById(R.id.spinner);
		cookBookphoto = (ImageView) findViewById(R.id.cookBookphoto);
		publish_name = (TextView) findViewById(R.id.publish_name);
		add_simple_intro = (TextView) findViewById(R.id.add_simple_intro);
		add_material = (TextView) findViewById(R.id.add_material);
		tips = (TextView) findViewById(R.id.tips);
		pulish = (Button) findViewById(R.id.pulish);
		step_pics[0] = (ImageView) findViewById(R.id.step1_pic);
		step_pics[1] = (ImageView) findViewById(R.id.step2_pic);
		step_pics[2] = (ImageView) findViewById(R.id.step3_pic);
		step_pics[3] = (ImageView) findViewById(R.id.step4_pic);
		InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		add_steps[0] = (TextView) findViewById(R.id.add_step1);
		add_steps[1] = (TextView) findViewById(R.id.add_step2);
		add_steps[2] = (TextView) findViewById(R.id.add_step3);
		add_steps[3] = (TextView) findViewById(R.id.add_step4);
		manager.hideSoftInputFromWindow(add_steps[0].getWindowToken(), 0);
		manager.hideSoftInputFromWindow(add_steps[1].getWindowToken(), 0);
		manager.hideSoftInputFromWindow(add_steps[2].getWindowToken(), 0);
		manager.hideSoftInputFromWindow(add_steps[3].getWindowToken(), 0);
		manager.hideSoftInputFromWindow(publish_name.getWindowToken(), 0);
		manager.hideSoftInputFromWindow(add_simple_intro.getWindowToken(), 0);
		manager.hideSoftInputFromWindow(add_material.getWindowToken(), 0);
		manager.hideSoftInputFromWindow(tips.getWindowToken(), 0);
		cookBookphoto.setOnClickListener(this);
		publish_name.setOnClickListener(this);
		add_simple_intro.setOnClickListener(this);
		add_material.setOnClickListener(this);
		tips.setOnClickListener(this);
		add_steps[0].setOnClickListener(this);
		add_steps[1].setOnClickListener(this);
		add_steps[2].setOnClickListener(this);
		add_steps[3].setOnClickListener(this);
		step_pics[0].setOnClickListener(this);
		step_pics[1].setOnClickListener(this);
		step_pics[2].setOnClickListener(this);
		step_pics[3].setOnClickListener(this);
		pulish.setOnClickListener(this);
		if (usuerInfo.getObjectId().equals("dcb8e42eec")) {
			spinner.setVisibility(View.VISIBLE);
		}
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String[] languages = getResources().getStringArray(
						R.array.spingarr);
				timeType = languages[position];
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_step1:
			Intent intent_step1 = new Intent(PublishCookbookActivity.this,EditPublishCookbookActivity.class);
			intent_step1.putExtra("title", "步骤1");
			startActivityForResult(intent_step1, 2);
			break;
		case R.id.add_step2:
			Intent intent_step2 = new Intent(PublishCookbookActivity.this,EditPublishCookbookActivity.class);
			intent_step2.putExtra("title", "步骤2");
			startActivityForResult(intent_step2, 3);
			break;
		case R.id.add_step3:
			Intent intent_step3 = new Intent(PublishCookbookActivity.this,EditPublishCookbookActivity.class);
			intent_step3.putExtra("title", "步骤3");
			startActivityForResult(intent_step3,4);
			break;
		case R.id.add_step4:
			Intent intent_step4 = new Intent(PublishCookbookActivity.this,EditPublishCookbookActivity.class);
			intent_step4.putExtra("title", "步骤4");
			startActivityForResult(intent_step4,5);
			break;
		case R.id.tips:
			Intent intent_tips = new Intent(PublishCookbookActivity.this,EditPublishCookbookActivity.class);
			intent_tips.putExtra("title", "小贴士");
			startActivityForResult(intent_tips,6);
			break;
		case R.id.add_material:
			Intent intent_material= new Intent(PublishCookbookActivity.this,EditPublishCookbookActivity.class);
			intent_material.putExtra("title", "用料");
			startActivityForResult(intent_material,7);
			break;
		case R.id.publish_name:
			Intent intent_name= new Intent(PublishCookbookActivity.this,EditPublishCookbookActivity.class);
			intent_name.putExtra("title", "菜谱名称");
			startActivityForResult(intent_name,8);
			break;
		case R.id.add_simple_intro:
			Intent intent_simple_intro= new Intent(PublishCookbookActivity.this,EditPublishCookbookActivity.class);
			intent_simple_intro.putExtra("title", "简介");
			startActivityForResult(intent_simple_intro,9);
			break;
		case R.id.cookBookphoto:
			if (temp_iv != null) {
				temp_iv = null;
				temp_iv = cookBookphoto;
			} else {
				temp_iv = cookBookphoto;
			}
			tempName = "cookBookphoto";
			openPictureSelectDialog();
			break;
		case R.id.step1_pic:
			if (temp_iv != null) {
				temp_iv = null;
				temp_iv = step_pics[0];
			} else {
				temp_iv = step_pics[0];
			}
			tempName = "step1";
			openPictureSelectDialog();
			break;
		case R.id.step2_pic:
			if (temp_iv != null) {
				temp_iv = null;
				temp_iv = step_pics[1];
			} else {
				temp_iv = step_pics[1];
			}
			tempName = "step2";
			openPictureSelectDialog();
			break;
		case R.id.step3_pic:
			if (temp_iv != null) {
				temp_iv = null;
				temp_iv = step_pics[2];
			} else {
				temp_iv = step_pics[2];
			}
			tempName = "step3";
			openPictureSelectDialog();
			break;
		case R.id.step4_pic:
			if (temp_iv != null) {
				temp_iv = null;
				temp_iv = step_pics[3];
			} else {
				temp_iv = step_pics[3];
			}
			tempName = "step4";
			openPictureSelectDialog();
			break;
		case R.id.pulish:
			if (usuerInfo.getObjectId().equals("dcb8e42eec")) {
				getInputText();
				if (timeType.equals("早餐")) {
					if (publish_nameString.equals("")
							|| add_materialString.equals("")
							|| add_simple_introString.equals("")
							|| add_stepsStrings[0].equals("")
							|| add_stepsStrings[1].equals("")
							|| add_stepsStrings[2].equals("")
							|| add_stepsStrings[3].equals("")
							|| tipString.equals("")) {
						Toast.makeText(this, "你忘了输入什么步骤", Toast.LENGTH_SHORT).show();
						return;
					} else {
						dialog.show();
						System.out.println("========早餐=======");
						String[] filePaths = new String[5];
						String tempString = Environment.getExternalStorageDirectory().getAbsolutePath();
						filePaths[0] = tempString + "/teachcooking/steps/"+ "cookBookphoto" + ".jpg";
						for (int i = 1; i < filePaths.length; i++) {
							filePaths[i] = tempString + "/teachcooking/steps/"+ "step" + i + ".jpg";
							BmobProFile.getInstance(this).uploadBatch(filePaths, new UploadBatchListener() {
										@Override
										public void onError(int statuscode,
												String errormsg) {
											dialog.dismiss();
											Toast.makeText(PublishCookbookActivity.this,"错误码" + statuscode+ ",错误描述："+ errormsg,Toast.LENGTH_SHORT).show();
										}
										@Override
										public void onSuccess(boolean isFinish,
												String[] fileNames,
												String[] urls, BmobFile[] files) {
											if (isFinish) {
												for (int i = 0; i < files.length; i++) {
													step_pic.add(files[i]);
												}
												BreakfastInfo breakfastInfo = new BreakfastInfo();
												breakfastInfo.setAuthor(usuerInfo);
												breakfastInfo.setCookBookName(publish_nameString);
												breakfastInfo.setCookBookIntroduce(add_simple_introString);
												breakfastInfo.setCookBookMaterial(add_materialString);
												breakfastInfo.setTip(tipString);
												breakfastInfo.setSteps(steps);
												breakfastInfo.setStep_pics(step_pic);
												breakfastInfo.setAdrress("");
												breakfastInfo.save(PublishCookbookActivity.this,new SaveListener() {
																	@Override
																	public void onSuccess() {
																		dialog.dismiss();
																		Toast.makeText(PublishCookbookActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
																		Intent intent = new Intent(PublishCookbookActivity.this,MenuActivity.class);
																		startActivity(intent);
																		finish();
																	}
																	@Override
																	public void onFailure(int arg0,String arg1) {
																		System.out.println(arg0+"============="+arg1);
																		dialog.dismiss();
																	}
																});
											}
										}
										@Override
										public void onProgress(int curIndex,int curPercent, int total,int totalPercent) {
											System.out.println("bmob"
													+ "onProgress :" + curIndex
													+ "---" + curPercent
													+ "---" + total + "----"
													+ totalPercent);
										}
									});
						}
					}
				} else if (timeType.equals("午餐")) {
					if (publish_nameString.equals("")
							|| add_materialString.equals("")
							|| add_simple_introString.equals("")
							|| add_stepsStrings[0].equals("")
							|| add_stepsStrings[1].equals("")
							|| add_stepsStrings[2].equals("")
							|| add_stepsStrings[3].equals("")
							|| tipString.equals("")) {
						Toast.makeText(this, "你忘了输入什么步骤", Toast.LENGTH_SHORT).show();
						return;
					} else {
						dialog.show();
						String[] filePaths = new String[5];
						String tempString = Environment.getExternalStorageDirectory().getAbsolutePath();
						filePaths[0] = tempString + "/teachcooking/steps/"+ "cookBookphoto" + ".jpg";
						for (int i = 1; i < filePaths.length; i++) {
							filePaths[i] = tempString + "/teachcooking/steps/"	+ "step" + i + ".jpg";
							BmobProFile.getInstance(this).uploadBatch(filePaths, new UploadBatchListener() {
										@Override
										public void onError(int statuscode,
												String errormsg) {
											dialog.dismiss();
											Toast.makeText(PublishCookbookActivity.this,"错误码" + statuscode+ ",错误描述："+ errormsg,Toast.LENGTH_SHORT).show();
										}
										@Override
										public void onSuccess(boolean isFinish,
												String[] fileNames,
												String[] urls, BmobFile[] files) {
											if (isFinish) {
												for (int i = 0; i < files.length; i++) {
													step_pic.add(files[i]);
												}
												LunchInfo lunchInfo = new LunchInfo();
												lunchInfo.setAuthor(usuerInfo);
												lunchInfo.setCookBookName(publish_nameString);
												lunchInfo.setCookBookIntroduce(add_simple_introString);
												lunchInfo.setCookBookMaterial(add_materialString);
												lunchInfo.setTip(tipString);
												lunchInfo.setSteps(steps);
												lunchInfo.setStep_pics(step_pic);
												lunchInfo.setAdrress("");
												lunchInfo.save(PublishCookbookActivity.this,new SaveListener() {
																	@Override
																	public void onSuccess() {
																		Toast.makeText(PublishCookbookActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
																		Intent intent = new Intent(PublishCookbookActivity.this,MenuActivity.class);
																		startActivity(intent);
																		finish();
																		dialog.dismiss();
																	}
																	@Override
																	public void onFailure(int arg0,String arg1) {
																		dialog.dismiss();
																	}
																});
											}
										}
										@Override
										public void onProgress(int curIndex,int curPercent, int total,int totalPercent) {
											System.out.println("bmob"+ "onProgress :" + curIndex+ "---" + curPercent+ "---" + total + "----"+ totalPercent);
										}
									});
						}
					}
				} else if (timeType.endsWith("晚餐")) {
					if (publish_nameString.equals("")
							|| add_materialString.equals("")
							|| add_simple_introString.equals("")
							|| add_stepsStrings[0].equals("")
							|| add_stepsStrings[1].equals("")
							|| add_stepsStrings[2].equals("")
							|| add_stepsStrings[3].equals("")
							|| tipString.equals("")) {
						Toast.makeText(this, "你忘了输入什么步骤", Toast.LENGTH_SHORT).show();
						return;
					} else {
						dialog.show();
						String[] filePaths = new String[5];
						String tempString = Environment.getExternalStorageDirectory().getAbsolutePath();
						filePaths[0] = tempString + "/teachcooking/steps/"+ "cookBookphoto" + ".jpg";
						for (int i = 1; i < filePaths.length; i++) {
							filePaths[i] = tempString + "/teachcooking/steps/"+ "step" + i + ".jpg";
							BmobProFile.getInstance(this).uploadBatch(filePaths, new UploadBatchListener() {
										@Override
										public void onError(int statuscode,
												String errormsg) {
											dialog.dismiss();
											Toast.makeText(	PublishCookbookActivity.this,"错误码" + statuscode+ ",错误描述："+ errormsg,Toast.LENGTH_SHORT).show();
										}
										@Override
										public void onSuccess(boolean isFinish,String[] fileNames,String[] urls, BmobFile[] files) {
											if (isFinish) {
												for (int i = 0; i < files.length; i++) {
													step_pic.add(files[i]);
												}
												DinnerInfo dinnerInfo = new DinnerInfo();
												dinnerInfo.setAuthor(usuerInfo);
												dinnerInfo.setCookBookName(publish_nameString);
												dinnerInfo.setCookBookIntroduce(add_simple_introString);
												dinnerInfo.setCookBookMaterial(add_materialString);
												dinnerInfo.setTip(tipString);
												dinnerInfo.setSteps(steps);
												dinnerInfo.setStep_pics(step_pic);
												dinnerInfo.setAdrress("");
												dinnerInfo.save(PublishCookbookActivity.this,new SaveListener() {
																	@Override
																	public void onSuccess() {
																		Toast.makeText(PublishCookbookActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
																		Intent intent = new Intent(PublishCookbookActivity.this,MenuActivity.class);
																		startActivity(intent);
																		finish();
																		dialog.dismiss();
																	}
																	@Override
																	public void onFailure(int arg0,String arg1) {
																		dialog.dismiss();
																	}
																});
											}
										}
										@Override
										public void onProgress(int curIndex,
												int curPercent, int total,
												int totalPercent) {
											System.out.println("bmob"
													+ "onProgress :" + curIndex
													+ "---" + curPercent
													+ "---" + total + "----"
													+ totalPercent);
										}
									});
						}
					}
					// =============晚餐===============
				} else {
					Toast.makeText(PublishCookbookActivity.this, "请选择类型",
							Toast.LENGTH_SHORT);
				}
			} else {
				getInputText();
				if (adrress == null) {
					return;
				}
				if (publish_nameString.equals("")
						|| add_materialString.equals("")
						&& add_simple_introString.equals("")
						&& add_stepsStrings[0].equals("")
						&& add_stepsStrings[1].equals("")
						&& add_stepsStrings[2].equals("")
						&& add_stepsStrings[3].equals("")
						&& tipString.equals("")) {
					Toast.makeText(this, "你忘了输入什么步骤", Toast.LENGTH_SHORT)
							.show();
					return;
				} else {
					dialog.show();
					if (usuerInfo != null) {
						String[] filePaths = new String[5];
						String tempString = Environment.getExternalStorageDirectory().getAbsolutePath();
						filePaths[0] = tempString + "/teachcooking/steps/"+ "cookBookphoto" + ".jpg";
						for (int i = 1; i < filePaths.length; i++) {
							filePaths[i] = tempString + "/teachcooking/steps/"+ "step" + i + ".jpg";
						}
						BmobProFile.getInstance(this).uploadBatch(filePaths,new UploadBatchListener() {
									@Override
									public void onError(int statuscode,String errormsg) {
										dialog.dismiss();
										Toast.makeText(PublishCookbookActivity.this,"错误码" + statuscode + ",错误描述："+ errormsg,Toast.LENGTH_SHORT).show();
									}
									@Override
									public void onSuccess(boolean isFinish,String[] fileNames, String[] urls,BmobFile[] files) {
										if (isFinish) {
											for (int i = 0; i < files.length; i++) {
												step_pic.add(files[i]);
											}
											CookBookInfo cookBookInfo = new CookBookInfo();
											cookBookInfo.setAuthor(usuerInfo);
											cookBookInfo.setCookBookName(publish_nameString);
											cookBookInfo.setCookBookIntroduce(add_simple_introString);
											cookBookInfo.setCookBookMaterial(add_materialString);
											cookBookInfo.setTip(tipString);
											cookBookInfo.setSteps(steps);
											cookBookInfo.setStep_pics(step_pic);
											cookBookInfo.setAdrress(adrress);
											insertUser(cookBookInfo);
										}
									}

									@Override
									public void onProgress(int curIndex,
											int curPercent, int total,
											int totalPercent) {
										// curIndex :表示当前第几个文件正在上传
										// curPercent :表示当前上传文件的进度值（百分比）
										// total :表示总的上传文件数
										// totalPercent:表示总的上传进度（百分比）
										System.out.println("bmob"+ "onProgress :"+ curIndex + "---"+ curPercent + "---"+ total + "----"+ totalPercent);
									}
								});
					} else {
						dialog.dismiss();
						Toast.makeText(PublishCookbookActivity.this, "你没有登录",Toast.LENGTH_SHORT).show();
					}
				}
			}
			break;
		}
	}

	// 上传数据的方法
	private void insertUser(CookBookInfo cookBookInfo) {
		cookBookInfo.save(PublishCookbookActivity.this, new SaveListener() {
			@Override
			public void onSuccess() {
				Toast.makeText(PublishCookbookActivity.this, "发布成功",Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(PublishCookbookActivity.this,MenuActivity.class);
				startActivity(intent);
				finish();
				dialog.dismiss();
			}
			@Override
			public void onFailure(int code, String arg1) {
				Toast.makeText(PublishCookbookActivity.this, "发布失败",Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		});
	}

	/**
	 * 获取输入的文字
	 * */
	private void getInputText() {
		publish_nameString = publish_name.getText().toString();
		add_materialString = add_material.getText().toString();
		add_simple_introString = add_simple_intro.getText().toString();
		tipString = tips.getText().toString();
		if (steps.size() > 0) {
			steps.clear();
			for (int i = 0; i < add_steps.length; i++) {
				add_stepsStrings[i] = add_steps[i].getText().toString();
				steps.add(add_stepsStrings[i]);
			}
		} else {
			for (int i = 0; i < add_steps.length; i++) {
				add_stepsStrings[i] = add_steps[i].getText().toString();
				steps.add(add_stepsStrings[i]);
			}
		}
	}

	/** 打开对话框 **/
	private void openPictureSelectDialog() {
		// 自定义Context,添加主题
		Context dialogContext = new ContextThemeWrapper(
				PublishCookbookActivity.this, android.R.style.Theme_Light);
		String[] choiceItems = new String[2];
		choiceItems[0] = "相机拍摄"; // 拍照
		choiceItems[1] = "本地相册"; // 从相册中选择
		ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choiceItems);
		// 对话框建立在刚才定义好的上下文上
		AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
		builder.setTitle("添加图片");
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0: // 相机
							doTakePhoto();
							break;
						case 1: // 从图库相册中选取
							doPickPhotoFromGallery();
							break;
						}
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	/**
	 * You will receive this call immediately before onResume() when your
	 * activity is re-starting.
	 **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) { // 返回成功
			switch (requestCode) {
			case PHOTO_WITH_CAMERA: {// 拍照获取图片
				String status = Environment.getExternalStorageState();
				if (status.equals(Environment.MEDIA_MOUNTED)) { // 是否有SD卡
					// 写一个方法将此文件保存到本应用下面啦
					Bitmap bitmap;
					try {
						bitmap = BitmapTools.revitionImageSize(Environment
								.getExternalStorageDirectory() + "/image.jpg");
						savePicture(tempName, bitmap);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(PublishCookbookActivity.this, "没有SD卡",
							Toast.LENGTH_LONG).show();
				}
				break;
			}
			case PHOTO_WITH_DATA:
			{// 从图库中选择图片
				Uri originalUri = data.getData();
				try {
					String filepath = BitmapTools.getImageAbsolutePath(this,
							originalUri);
					Bitmap bitmap = BitmapTools.revitionImageSize(filepath);
					savePicture(tempName, bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case 2:
				if (data==null) {
					return;
				}
				content = data.getStringExtra("content");						
				add_steps[0].setText(content);
				break;
			case 3:
				if (data==null) {
					return;
				}
				content = data.getStringExtra("content");		
				add_steps[1].setText(content);
				break;
			case 4:
				if (data==null) {
					return;
				}
				content = data.getStringExtra("content");		
				add_steps[2].setText(content);
				break;
			case 5:
				if (data==null) {
					return;
				}
				content = data.getStringExtra("content");		
				add_steps[3].setText(content);
				break;
			case 6:
				if (data==null) {
					return;
				}
				content = data.getStringExtra("content");		
				tips.setText(content);
				break;
			case 7:
				if (data==null) {
					return;
				}
				content = data.getStringExtra("content");		
				add_material.setText(content);
				break;
			case 8:
				if (data==null) {
					return;
				}
				content = data.getStringExtra("content");		
				publish_name.setText(content);
				break;
			case 9:
				if (data==null) {
					return;
				}
				content = data.getStringExtra("content");		
				add_simple_intro.setText(content);
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/** 从相册获取图片 **/
	private void doPickPhotoFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*"); // 开启Pictures画面Type设定为image
		intent.setAction(Intent.ACTION_GET_CONTENT); // 使用Intent.ACTION_GET_CONTENT这个Action
		startActivityForResult(intent, PHOTO_WITH_DATA); // 取得相片后返回到本画面
	}

	/** 拍照获取相片 **/
	private void doTakePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统相机

		Uri imageUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "image.jpg"));
		// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		// 直接使用，没有缩小
		startActivityForResult(intent, PHOTO_WITH_CAMERA); // 用户点击了从相机获取
	}

	/** 保存图片到本应用下 **/
	private void savePicture(String fileName, Bitmap bitmap) {
		temp_iv.setScaleType(ScaleType.FIT_XY);
		temp_iv.setImageBitmap(bitmap);
		File picureFileDir = new File(Environment.getExternalStorageDirectory()
				+ "/teachcooking/steps");
		if (!picureFileDir.exists()) {
			picureFileDir.mkdirs();
		}
		File file = new File(picureFileDir, fileName + ".jpg");
		if (!file.exists()) {
			try {
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 把图片写入指定文件夹中
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			file.delete();
			try {
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 把图片写入指定文件夹中
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
