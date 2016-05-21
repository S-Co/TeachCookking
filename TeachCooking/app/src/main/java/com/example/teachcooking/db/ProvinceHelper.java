package com.example.teachcooking.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.teachcooking.entity.Province;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class ProvinceHelper {
	/**
	 * 包名
	 * */
	private static final String PACKAGE_NAME = "com.example.teachcooking";
	/**
	 * 
	 * */
	private static final String FILE_NAME = "provice.db";

	private static final String FIlE_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME;
	/**
	 * 保存数据
	 * @param context 上下文
	 */
	public void savaDate(Context context) {
		File file = new File(FIlE_PATH + "/" + FILE_NAME);
		if (file.exists()) {
			return;
		}
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			file.createNewFile();
			// 利用resources获取assets文件夹下源文件的文件的流
			is = context.getAssets().open("province.db");
			fos = new FileOutputStream(file);
			byte b[] = new byte[1024];
			int len;
			while ((len = is.read(b)) != -1) {
				fos.write(b, 0, len);
				fos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 获取省份和城市列表
	 * @return
	 */
	public ArrayList<Province> getProvince(){
			ArrayList<Province> list = new ArrayList<Province>();
			SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(FIlE_PATH + "/" + FILE_NAME, null);
			String  title = "select * from province";
			Cursor cursor = database.rawQuery(title, null);
			while (cursor.moveToNext()) {
				//获取到省份名字
				String province = cursor.getString(cursor.getColumnIndex("provincename"));
				int  provincecode = cursor.getInt(cursor.getColumnIndex("provincecode"));
				String  sql = "select * from citys where provincecode ="+provincecode;
				ArrayList<String> citys = new ArrayList<String>();
 				Cursor cursor2 = database.rawQuery(sql, null);
 				while (cursor2.moveToNext()) {
					String city= cursor2.getString(cursor2.getColumnIndex("cityname"));
					citys.add(city);
				}
 				list.add(new Province(province, citys));
			}
		return list;
	}
}
