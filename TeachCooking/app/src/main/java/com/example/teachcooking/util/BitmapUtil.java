package com.example.teachcooking.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;

/**
 * 操作Bitmap的工具类
 * 保存Bitmap至Sd卡,从Sd卡获取Bitmap,根据url判断Sd卡中是否存在对应的Bitmap
 * 
 * @author Ligang
 *
 */
public class BitmapUtil {
	//图片保存路径
	public static final String CACHES_PATH = Environment.getExternalStorageDirectory().toString() +
			File.separator +"teachcooking" + File.separator + "cache" + File.separator + "img" + File.separator;
	//图片保存的品质，100为保存原始图片不压缩
	public static final int QUALITY = 100; 
	//图片保存的后缀名---如".jpg",".png",".xx"这个可以随便定义，设置为""也可以
	public static final String EXTENSION_NAME = ".jpg";
	static File fileDir = new File(CACHES_PATH);
	/**
	 * 创建缓存图片文件夹
	 */
	public static void createCacheDir() {
		if(!fileDir.exists()){
			fileDir.mkdirs();
		}
	} 

	public static void saveBitmap(InputStream is,String imgUrl){
		createCacheDir();
		File dstFile = new File(fileDir, getFileName(imgUrl));
		try {
			if(!dstFile.exists()){
				dstFile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(dstFile);
			byte[] buff = new byte[1024 * 10];
			int len;
			while((len = is.read(buff)) != -1){
				fos.write(buff,0,len);
			}
			is.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 将Bitmap保存到Sd卡
	 * @param bitmap
	 * @param imgUrl
	 */
	public static void saveBitmap(Bitmap bitmap,String imgUrl){
		File file  =new File(fileDir, getFileName(imgUrl));
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			//保存bitmap
			bitmap.compress(CompressFormat.JPEG, QUALITY, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取图片文件名
	 * @param imgUrl
	 * @return
	 */
	public static String getFileName(String imgUrl){
		return MD5(imgUrl) + EXTENSION_NAME;
	}
	/**
	 * 根据url判断Sd卡中是否存在对应的Bitmap
	 * @param imgUrl
	 * @return
	 */
	public static boolean bitmapExists(String imgUrl){
		File file = new File(CACHES_PATH + MD5(imgUrl) + EXTENSION_NAME);
		return file.exists();

	}
	public static void createFile(File file){
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static File getFile(String imgUrl){
		File file = new File(CACHES_PATH + MD5(imgUrl) + EXTENSION_NAME);
		return file;
	}
	
	/**
	 * 获取一张压缩的Bitmap
	 * @param imgUrl
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getBitmap(String imgUrl,int width,int height){
		Options options = new Options();
		//将options的inJustDecodeBounds设置为true的话，加载Bitmap时，不会加入内存，此时获取到的Bitmap是null
		//但是 可以获取到bitmap的宽高信息
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(CACHES_PATH + getFileName(imgUrl), options);
		int bitmapWidth = options.outWidth;
//		log("原始Bitmap的宽="+bitmapWidth);
		
		int inSampleSize = 1; //缩放比例  如果等于4，那么宽和高都变为 1/4
		if(bitmapWidth > width){
			inSampleSize = bitmapWidth/width;
		}
		
		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(CACHES_PATH + getFileName(imgUrl), options);
	}
	
	public static Bitmap getBitmapFromSDCard(String imageUrl){
		return BitmapFactory.decodeFile(getFile(imageUrl).getPath());
		
	}
	/**
	 * MD5加密算法
	 * 在这里主要是为了格式化保存的图片的文件名（将Http://.........jpg 转化成不含特殊字符的文件名）
	 * 加密后得到的文件名是唯一的
	 * @param s
	 * @return
	 */
	public static String MD5(String s) {
		try {
			byte[] btInput = s.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < md.length; i++) {
				int val = ((int) md[i]) & 0xff;
				if (val < 16)
					sb.append("0");
				sb.append(Integer.toHexString(val));
			}
			return sb.toString();
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 清空本地图片缓存
	 */
	public static void clearImageCache(){
		File fileDir = new File(CACHES_PATH);
		File[] files = fileDir.listFiles();
		for (File file : files) {
			file.delete();
		}
	}
	
//	public static  void log(Object o){
//		Log.d(BitmapUtil.class.getName(), ""+o);
//	}
}
