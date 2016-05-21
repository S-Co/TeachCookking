package com.example.teachcooking.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.Handler;
import android.os.Message;

public class HttpUtil {

	private static final int MSG_SUCCESS = 1;
	private static final int MSG_FAILURE = 2;
	
	private Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SUCCESS:
				callBack.onSuccess(msg.obj.toString());
				break;
			case MSG_FAILURE:
				callBack.onFailure();
				break;
			}
			return false;
		}
	});
	private CallBack callBack;
	public void get(String urlStr,CallBack callBack){
		this.callBack = callBack;
		HttpThread httpThread = new HttpThread(urlStr);
		httpThread.start();
	}
	public interface CallBack{
		void onSuccess(String result);
		void onFailure();
	}
	//请求数据
	private class HttpThread extends Thread{
		private String urlStr;
		public HttpThread(String urlStr) {
			super();
			this.urlStr = urlStr;
		}
		@Override
		public void run() {
			//Get请求数据
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(params, 8*1000);
			HttpConnectionParams.setConnectionTimeout(params, 8*1000);
			HttpClient httpClient = new DefaultHttpClient(params);
			HttpGet httpGet = new HttpGet(urlStr);
			BufferedReader br = null;
			try {
				HttpResponse response = httpClient.execute(httpGet);
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					InputStream is = response.getEntity().getContent();
					br = new BufferedReader(new InputStreamReader(is));
					StringBuffer sb = new StringBuffer();
					String line;
					while((line = br.readLine()) != null){
						sb.append(line);
					}
					handler.sendMessage(handler.obtainMessage(MSG_SUCCESS, sb.toString()));
					return;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(br != null){
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			handler.sendEmptyMessage(MSG_FAILURE);
			
		}
	}
	
	public static String readStream(InputStream is) throws IOException{
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String line;
		while((line = br.readLine()) != null){
			sb.append(line);
		}
		return sb.toString();
	}
}
