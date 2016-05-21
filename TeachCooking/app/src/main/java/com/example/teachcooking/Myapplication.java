package com.example.teachcooking;

import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.example.teachcooking.entity.CookStep;

import android.annotation.SuppressLint;
import android.app.Application;
import android.widget.Toast;

public class Myapplication extends Application {
	public  static Myapplication myapplication;
	@SuppressLint("ShowToast")
	@Override
	public void onCreate() {
		super.onCreate();
		myapplication = this;
		toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
		SDKInitializer.initialize(getApplicationContext());
	}

	public List<CookStep> cookSteps;
	public int position;
	public Toast toast;

	public void toast(Object o) {
		toast.setText(o + "");
		toast.show();
	}
}
