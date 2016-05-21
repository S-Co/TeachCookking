package com.example.teachcooking.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;
import android.widget.ImageView;
@SuppressLint("NewApi")
public class ImageLoader {

	/**线程池**/
	private ExecutorService mExecutorService;
	/**用于缓存图片**/
	private LruCache<String, Bitmap> mLrcCache;
	public ImageLoader() {
		mExecutorService = Executors.newFixedThreadPool(3);
		//		mLrcCache = new LruCache<String, Bitmap>(maxSize);
		initLrcCache();
	}

	private boolean mIsBusy = false;
	public void setmIsBusy(boolean mIsBusy) {
		this.mIsBusy = mIsBusy;
	}

	private void initLrcCache() {
		//获取系统分配给每个应用程序的最大内存，每个应用系统分配64M
		int maxMemory = (int) Runtime.getRuntime().maxMemory();  
//		log("maxMemory="+maxMemory);
		//		int mCacheSize = maxMemory / 8;
		int mCacheSize = 5000 * 1024;
		mLrcCache = new LruCache<String, Bitmap>(mCacheSize){

			@Override
			protected int sizeOf(String key, Bitmap value) {
				int size = 0;
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){
					size = value.getByteCount();
					//3.1版本之后，使用的是这个方法获取bitmap的内存大小
//					log("size = " + size);
					return size;
				}
				//3.1版本之前，使用的是这个方法获取bitmap的内存大小
				size = value.getRowBytes() * value.getHeight();
//				log("size = " + size);
				return size;
			}
		};
	}

	public void displayImg(String imgUrl,ImageView imageView){
		
		imageView.setTag(imgUrl);
//		log("进入 displayImg");
		//首先从lrc缓存中获取bitmap
		Bitmap bitmap = getBitmapFromCache(imgUrl);
		if(bitmap != null){
			//直接为ImageView设置Bitmap
			showBitmap(imageView, bitmap,imgUrl);
//			log("来自lru缓存中");
			return;
		}
//		log("lru缓存中不存在bitmap，开始从本地获取");
		//向线程池提交线程任务
		if(!mIsBusy){
			LoadBitmapRunnable runnable = new LoadBitmapRunnable(imageView, imgUrl);
			mExecutorService.submit(runnable);
//			log("提交一个网络请求");
		}
	}


	/**
	 * 加载显示bitmap的线程
	 * @author Administrator
	 *
	 */
	private class LoadBitmapRunnable implements Runnable{
		private ImageView imageView;
		private String imgUrl;
		public LoadBitmapRunnable(ImageView imageView, String imgUrl) {
			super();
			this.imageView = imageView;
			this.imgUrl = imgUrl;
		}

		@Override
		public void run() {
			//从本地获取Bitmap
			Bitmap bitmap = getBitmapFromLocal(imgUrl);
//			log("run--bitmap="+bitmap);
			if(bitmap != null){
				//为ImageView显示Bitmap
				showBitmap(imageView, bitmap,imgUrl);
//				log("来自本地");
				return;
			}
//			log("本地不存在bitmap，开始从网络获取");
			//从网络获取Bitmap
			bitmap = getBitmapFromNetwork(imgUrl);
			if(bitmap != null){
				//为ImageView显示Bitmap
				showBitmap(imageView, bitmap,imgUrl);
//				log("来自网络");
			}
		}

	}

	/**
	 * 为ImageView显示Bitmap
	 * @param imageView
	 * @param bitmap
	 */
	private void showBitmap(ImageView imageView,Bitmap bitmap,String imgUrl){
		//防止图片错乱显示
		if(!imgUrl.equals(imageView.getTag().toString())){
			return;
		}
		DisplayImgRunnable runnable = new DisplayImgRunnable(imageView, bitmap);
		Activity aty = (Activity) imageView.getContext();
		aty.runOnUiThread(runnable);
	}
	class DisplayImgRunnable implements Runnable{
		private ImageView imageView;
		private Bitmap bitmap;
		public DisplayImgRunnable(ImageView imageView, Bitmap bitmap) {
			super();
			this.imageView = imageView;
			this.bitmap = bitmap;
		}
		@Override
		public void run() {
			imageView.setImageBitmap(bitmap);
		}
	}

	/**
	 * 将bitmap放入lrc缓存
	 * @param bitmap
	 * @param urlStr
	 */
	private void addBitmapToCache(Bitmap bitmap,String urlStr){
		mLrcCache.put(urlStr, bitmap);
	}

	/**
	 * 从lrc缓存中获取bitmap
	 * @param imgUrl
	 * @return
	 */
	private Bitmap getBitmapFromCache(String imgUrl){
		return mLrcCache.get(imgUrl);
	}

	/**
	 * 从本地获取Bitmap
	 * @param imgUrl
	 * @return
	 */
	public Bitmap getBitmapFromLocal(String imgUrl) {
		//TODO
		Bitmap bitmap = BitmapUtil.getBitmapFromSDCard(imgUrl);
		if(bitmap != null){
			addBitmapToCache(bitmap, imgUrl);
		}
		return bitmap;
	}
	/**
	 * 从网络获取Bitmap
	 * @param imgUrl
	 * @return
	 */
	public Bitmap getBitmapFromNetwork(String imgUrl){
		Bitmap bitmap = null;
		try {
			URL url  = new URL(imgUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(8 * 1000);
			conn.setReadTimeout(8 * 1000);
			InputStream is = conn.getInputStream();
			//将文件保存到本地SD卡
			BitmapUtil.saveBitmap(is, imgUrl);
			bitmap = BitmapUtil.getBitmapFromSDCard(imgUrl);
			//加入lrc缓存
			addBitmapToCache(bitmap, imgUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 

		return bitmap;
	}


//	public void log(Object o){
//		Log.d("ImageLoader", o+"");
//	}
}
