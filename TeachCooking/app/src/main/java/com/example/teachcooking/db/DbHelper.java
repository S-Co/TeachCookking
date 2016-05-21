package com.example.teachcooking.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.teachcooking.entity.CookBook;
import com.google.gson.Gson;

public class DbHelper extends SQLiteOpenHelper{

	public static final String DB_NAME = "jiao_zuo_cai.db";
	public static final int VERSION = 1;

	private SQLiteDatabase dbWrite;
	private SQLiteDatabase dbRead;
	public DbHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		dbWrite = getWritableDatabase();
		dbRead = getReadableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {


		db.execSQL("CREATE TABLE " + ColumnsNearBy.TABLE_NAME + "("
				+ ColumnsNearBy.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ ColumnsNearBy.COOK_DETAIL + " TEXT,"
				+ ColumnsNearBy.COOK_ID + " TEXT"
				+")");
		db.execSQL("CREATE TABLE " + ColumnsCollect.TABLE_NAME + "("
				+ ColumnsCollect.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ ColumnsCollect.COOK_DETAIL + " TEXT,"
				+ ColumnsCollect.COOK_ID + " TEXT"
				+")");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * 插入菜谱到数据库里面
	 * @param cookBook
	 */
	public void insertCookDetailToNearBy(CookBook cookBook){
		String detail = new Gson().toJson(cookBook);
		ContentValues values = new ContentValues();
		values.put(ColumnsNearBy.COOK_DETAIL, detail);
		values.put(ColumnsNearBy.COOK_ID, cookBook.getId());
		dbWrite.insert(ColumnsNearBy.TABLE_NAME, null, values );
	}
	public interface ColumnsNearBy{
		String TABLE_NAME = "near_by";
		String ID = "_id";
		String COOK_ID = "cook_id";
		String COOK_DETAIL = "cook_detail";
	}
	/**
	 *得到最近浏览的菜谱
	 * @return
	 */
	public List<CookBook> getNearByCookBooks(String limit) {
		List<CookBook> cookBooks = new ArrayList<CookBook>();
		Cursor cursor = dbRead.query(ColumnsNearBy.TABLE_NAME, null, null, null, null, null, ColumnsNearBy.ID+" desc",limit);
		Gson gson = new Gson();
		while(cursor.moveToNext()){
			String detail = cursor.getString(cursor.getColumnIndex(ColumnsNearBy.COOK_DETAIL));
			CookBook cookBook = gson.fromJson(detail, CookBook.class);
			cookBooks.add(cookBook);
		}
		cursor.close();
		return cookBooks;
	}
	
	/**
	 * 判断是否退出最近查看的菜谱
	 * @param cookBook
	 * @return
	 */
	public boolean exitCookBookInNearBy(CookBook cookBook){
		String[] columns = {ColumnsNearBy.COOK_ID};
		String selection = ColumnsNearBy.COOK_ID+"=?";
		String[] selectionArgs = {cookBook.getId()};
		Cursor cursor = dbRead.query(ColumnsNearBy.TABLE_NAME, columns , selection, selectionArgs, null, null, null);
		if(cursor.moveToNext()){
			cursor.close();
			return true;
		}
		return false;
	}
	
	public interface ColumnsCollect{
		String TABLE_NAME = "collect";
		String ID = "_id";
		String COOK_ID = "cook_id";
		String COOK_DETAIL = "cook_detail";
	}
	
	/**
	 * 得到收藏的菜谱
	 * @return
	 */
	public List<CookBook> getCollectCookBooks(String limit) {
		List<CookBook> cookBooks = new ArrayList<CookBook>();
		Cursor cursor = dbRead.query(ColumnsCollect.TABLE_NAME, null, null, null, null, null, ColumnsCollect.ID+" desc",limit); 
		Gson gson = new Gson();
		while(cursor.moveToNext()){
			String detail = cursor.getString(cursor.getColumnIndex(ColumnsCollect.COOK_DETAIL));
			CookBook cookBook = gson.fromJson(detail, CookBook.class);
			cookBooks.add(cookBook);
		}
		cursor.close();
		return cookBooks;
	}
	
	/**
	 * 是否退出收藏的菜谱列表
	 * @param cookBook
	 * @return
	 */
	public boolean exitCookBookInCollect(CookBook cookBook){
		String[] columns = {ColumnsCollect.COOK_ID};
		String selection = ColumnsCollect.COOK_ID+"=?";
		String[] selectionArgs = {cookBook.getId()};
		Cursor cursor = dbRead.query(ColumnsCollect.TABLE_NAME, columns , selection, selectionArgs, null, null, null);
		if(cursor.moveToNext()){
			cursor.close();
			return true;
		}
		return false;
	}
	/**
	 *插入菜谱到收藏列表
	 * @param cookBook
	 */
	public void insertCookDetailToCollect(CookBook cookBook){
		String detail = new Gson().toJson(cookBook);
		ContentValues values = new ContentValues();
		values.put(ColumnsCollect.COOK_DETAIL, detail);
		values.put(ColumnsCollect.COOK_ID, cookBook.getId());
		dbWrite.insert(ColumnsCollect.TABLE_NAME, null, values );
	}
	
	/**
	 * 删除收藏列表的菜谱
	 * @param cookBook
	 */
	public void deleteCookDetailFromCollect(CookBook cookBook){
		String whereClause = ColumnsCollect.COOK_ID + "=?";
		String[] whereArgs = {cookBook.getId()};
		dbWrite.delete(ColumnsCollect.TABLE_NAME, whereClause, whereArgs );
	}

	/**
	 * 删除收藏列表里面菜谱的id
	 * @param cookBookIds
	 */
	public void deleteCollectCookBook(List<String> cookBookIds) {
		for (String id : cookBookIds) {
			String whereClause = ColumnsCollect.COOK_ID + "=?";
			String[] whereArgs = new String[]{id};
			dbWrite.delete(ColumnsCollect.TABLE_NAME, whereClause, whereArgs);
		}
	}
}
