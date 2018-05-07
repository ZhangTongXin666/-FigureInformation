package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBUtil {
	private static DBUtil mInstance;
	private Context mContext;
	private SQLHelper mSQLHelp;
	private SQLiteDatabase mSQLiteDatabase;
	private Cursor cursor;
	private static String mStrLock = "lockOne";

	private DBUtil(Context context) {
		mContext = context;
		mSQLHelp = new SQLHelper(context);
		mSQLiteDatabase = mSQLHelp.getWritableDatabase();
	}

	/**
	 * 初始化数据库操作DBUtil类
	 */
	public static DBUtil getInstance(Context context) {
		if (mInstance == null){
			synchronized (mStrLock){
				if (mInstance == null) {
					mInstance = new DBUtil(context);
				}
			}
		}
		return mInstance;
	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		mSQLHelp.close();
		mSQLHelp = null;
		mSQLiteDatabase.close();
		mSQLiteDatabase = null;
		mInstance = null;
	}

	public synchronized void addClassName(List<HashMap<String,String>> listClassName){
		ContentValues values = new ContentValues();
		int length = listClassName.size();
		for (int i = 0; i < length; i++) {
			if (verifySaved(SQLHelper.CLASSNAME, listClassName.get(i).get("classname"), "classname")){
				deleteClassName(listClassName.get(i).get("classname"));
			}
			values.put("kind", listClassName.get(i).get("kind"));
			values.put("classname", listClassName.get(i).get("classname"));
			values.put("url", listClassName.get(i).get("url"));
			mSQLiteDatabase.insert(SQLHelper.CLASSNAME, null, values);
		}
	}

	public synchronized void addCardContent(List<HashMap<String, String>> list){
		ContentValues values = new ContentValues();
		for (int i = 0; i < list.size(); i++) {
			if (verifySaved(SQLHelper.CARDCONTENT, list.get(i).get("title"), "title")){
				deleteCardContent(list.get(i).get("title"));
			}
			values.put("kind", list.get(i).get("kind"));
			values.put("classname", list.get(i).get("classname"));
			values.put("title", list.get(i).get("title"));
			values.put("link", list.get(i).get("link"));
			values.put("description", list.get(i).get("description"));
			values.put("timeandauthor", list.get(i).get("timeandauthor"));
			values.put("pubdate", list.get(i).get("pubdate"));
			mSQLiteDatabase.insert(SQLHelper.CARDCONTENT, null, values);
		}
	}

	private synchronized void deleteClassName(String className){
		mSQLiteDatabase.delete(SQLHelper.CLASSNAME, "classname = ?", new String[]{className});
	}

	private synchronized void deleteCardContent(String title){
		mSQLiteDatabase.delete(SQLHelper.CARDCONTENT, "title = ?", new String[]{title});
	}

	public synchronized List<String> getAllClassName(){
		List<String> listClassName = new ArrayList<>();
		cursor = mSQLiteDatabase.query(SQLHelper.CLASSNAME,null,null, null, null, null, null, null);
		if (cursor.moveToFirst()){
			for (int i = 0; i < cursor.getCount(); i++){
				cursor.moveToPosition(i);
				listClassName.add(0,cursor.getString(1));
			}
		}
		cursor.close();
		return listClassName;
	}

	public synchronized List<HashMap<String, String>>  getCardContentByClassName(String className){
		List<HashMap<String, String>> listCardContent = new ArrayList<>();
		cursor = mSQLiteDatabase.query(SQLHelper.CARDCONTENT, null, "classname = ?", new String[]{className}, null, null, null);
		HashMap<String, String> map;
		if (cursor.moveToFirst()){
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				map = new HashMap<>();
				map.put("classname", cursor.getString(1));
				map.put("title", cursor.getString(2));
				map.put("link", cursor.getString(3));
				map.put("description", cursor.getString(4));
				map.put("timeandauthor", cursor.getString(5));
				map.put("pubdate", cursor.getString(7));
				listCardContent.add(map);
			}
		}
		cursor.close();
		return listCardContent;
	}

	public synchronized String  getCardContentByTitle(String title){
		cursor = mSQLiteDatabase.query(SQLHelper.CARDCONTENT, null, "title = ?", new String[]{title}, null, null, null);
		String content = "文献已被作者删除";
		if (cursor.moveToFirst()){
			if (cursor.getCount() > 0){
				content = cursor.getString(4);
			}
		}
		cursor.close();
		return content;
	}

	public synchronized List<HashMap<String, String>> getAllCardContent(String kind){
		List<HashMap<String, String>> listCardContent = new ArrayList<>();
		HashMap<String, String> map;
		if (kind.equals("全部资讯")){
			cursor = mSQLiteDatabase.query(SQLHelper.CARDCONTENT, null, null, null, null, null, null);
		}else {
			cursor = mSQLiteDatabase.query(SQLHelper.CARDCONTENT, null, "kind = ?", new String[]{kind}, null, null, null);
		}
		if (cursor.moveToFirst()){
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				map = new HashMap<>();
				map.put("classname", cursor.getString(1));
				map.put("title", cursor.getString(2));
				map.put("link", cursor.getString(3));
				map.put("description", cursor.getString(4));
				map.put("timeandauthor", cursor.getString(5));
				map.put("pubdate", cursor.getString(7));
				listCardContent.add(map);
			}
		}
		cursor.close();
		return listCardContent;
	}

	private synchronized boolean verifySaved(String tableName, String value, String col){
		cursor = mSQLiteDatabase.query(tableName, null, col+" = ?", new String[]{value}, null, null, null);
		if (cursor.getCount() > 0){
			cursor.close();
			return true;
		}else {
			cursor.close();
			return false;
		}
	}

}