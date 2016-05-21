package com.example.teachcooking;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.teachcooking.entity.UsuerInfo;
import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity implements OnClickListener {
	private EditText input_usuer_password;
	private EditText input_usuer_password_agin;
	private EditText input_after_password;
	private Button sure_change_password;
	private String objectId;
	private String newPassWord;
	private String phone;
	private String nowPassWord;
	private ImageView chang_password_back;

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
		setContentView(R.layout.activity_change_password);
		phone = getIntent().getStringExtra("phone");
		input_after_password = (EditText) findViewById(R.id.input_after_password);
		chang_password_back = (ImageView) findViewById(R.id.chang_password_back);
		input_usuer_password = (EditText) findViewById(R.id.input_usuer_password);
		input_usuer_password_agin = (EditText) findViewById(R.id.input_usuer_password_agin);
		sure_change_password = (Button) findViewById(R.id.sure_change_password);
		sure_change_password.setOnClickListener(this);
		chang_password_back.setOnClickListener(this);
		if (phone != null) {
			input_after_password.setVisibility(View.GONE);
			BmobQuery<UsuerInfo> query = new BmobQuery<UsuerInfo>();
			query.addWhereEqualTo("username", phone);
			query.findObjects(this, new FindListener<UsuerInfo>() {

				@Override
				public void onSuccess(List<UsuerInfo> arg0) {
					UsuerInfo usuerInfo = arg0.get(0);
					objectId = usuerInfo.getObjectId();
					System.out.println(objectId);
				}

				@Override
				public void onError(int arg0, String arg1) {

				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sure_change_password:
			if (phone != null) {
				if (input_usuer_password_agin.getText().toString()
						.equals(input_usuer_password.getText().toString())
						&& !input_usuer_password.getText().toString()
								.equals("")
						&& !input_usuer_password_agin.getText().toString()
								.equals("")) {
					newPassWord = input_usuer_password.getText().toString();
					// test对应你刚刚创建的云端代码名称
					String clounCodeName = "changePassword";
					fastRequst(clounCodeName);
					// Request();
				}
			} else {
				nowPassWord = input_after_password.getText().toString();
				if (!nowPassWord.equals("")
						&& !input_usuer_password.getText().toString()
								.equals("")
						&& !input_usuer_password_agin.getText().toString()
								.equals("")
						&& input_usuer_password_agin
								.getText()
								.toString()
								.equals(input_usuer_password.getText()
										.toString())) {
					BmobUser.updateCurrentUserPassword(
							ChangePasswordActivity.this, nowPassWord,
							input_usuer_password_agin.getText().toString(),
							new UpdateListener() {

								@Override
								public void onSuccess() {
									Toast.makeText(ChangePasswordActivity.this,
											"修改密码成功，请重新登录", Toast.LENGTH_SHORT)
											.show();
									finish();
								}

								@Override
								public void onFailure(int arg0, String arg1) {
									Toast.makeText(ChangePasswordActivity.this,
											"修改密码失败，请检查网络", Toast.LENGTH_SHORT)
											.show();
								}
							});
				}
			}
			break;
		case R.id.chang_password_back:
			finish();
			break;
		}

	}

	private void Request() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpPost httpPost = new HttpPost(
						"http://cloud.bmob.cn/ca6e8917ca36cc13/changePassword");
				HttpClient httpClient = new DefaultHttpClient();
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("objectId", objectId));
				list.add(new BasicNameValuePair("password", newPassWord));
				try {
					httpPost.setEntity(new UrlEncodedFormEntity(list,
							HTTP.UTF_8));
					HttpResponse response = httpClient.execute(httpPost);
					if (response.getStatusLine().getStatusCode() == 200) {
						final String s = EntityUtils.toString(
								response.getEntity(), "UTF-8");
						if (s.contains("updateAt")) {
							Toast.makeText(ChangePasswordActivity.this, "修改成功",
									Toast.LENGTH_SHORT).show();
							UsuerInfo usuerInfo = new UsuerInfo();
							usuerInfo.setUsername(phone);
							usuerInfo.setPassword(newPassWord);
							usuerInfo.login(ChangePasswordActivity.this,
									new SaveListener() {
										@Override
										public void onSuccess() {
											Toast.makeText(
													ChangePasswordActivity.this,
													"登录成功", Toast.LENGTH_SHORT)
													.show();
											Intent intent = new Intent(
													ChangePasswordActivity.this,
													MenuActivity.class);
											startActivity(intent);
											finish();
										}

										@Override
										public void onFailure(int arg0,
												String arg1) {

										}
									});
						}
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private void fastRequst(String clounCodeName) {
		JSONObject params = new JSONObject();
		try {
			// name是上传到云端的参数名称，值是bmob，云端代码可以通过调用request.body.name获取这个值
			params.put("password", newPassWord);
			params.put("objectId", objectId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// 创建云端代码对象
		AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
		// 异步调用云端代码
		cloudCode.callEndpoint(this, clounCodeName, params,
				new CloudCodeListener() {

					@Override
					public void onSuccess(Object arg0) {
						Toast.makeText(ChangePasswordActivity.this, "修改成功",
								Toast.LENGTH_SHORT).show();
						System.out.println(arg0 + toString());
						UsuerInfo usuerInfo = new UsuerInfo();
						usuerInfo.setUsername(phone);
						usuerInfo.setPassword(newPassWord);
						usuerInfo.login(ChangePasswordActivity.this,
								new SaveListener() {
									@Override
									public void onSuccess() {
										Toast.makeText(
												ChangePasswordActivity.this,
												"登录成功", Toast.LENGTH_SHORT)
												.show();
										Intent intent = new Intent(
												ChangePasswordActivity.this,
												MenuActivity.class);
										startActivity(intent);
										finish();
									}

									@Override
									public void onFailure(int arg0, String arg1) {

									}
								});
					}

					@Override
					public void onFailure(int arg0, String arg1) {

					}
				});
	}
}
