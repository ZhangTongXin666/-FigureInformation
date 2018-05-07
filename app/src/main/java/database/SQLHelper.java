package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper
{
	public static final String DB_NAME = "database.db";// 数据库名称
	public static final int VERSION = 1;
	
	public static final String TABLE_CHANNEL = "channel";//数据表

	public static final String ID = "id";//
	public static final String NAME = "name";
	public static final String ORDERID = "orderId";
	public static final String SELECTED = "selected";
	public static final String CLASSNAME = "classname";//类别名称
	public static final String CARDCONTENT = "cardcontent";//卡片内容
	public static final String CREATE_CLASSNAME_TABLE = "create table "+CLASSNAME+"(_id INTEGER PRIMARY KEY, classname text, kind text, url text)";
	public static final String CREATE_CARDCONTENT_TABLE = "create table "+CARDCONTENT+"(_id INTEGER PRIMARY KEY, classname text, title text, link text, description text, timeandauthor text, kind text," +
			"pubdate text)";
	private Context context;
	public SQLHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		this.context = context;
	}

	public Context getContext(){
		return context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 创建数据库后，对数据库的操作
		String sql = "create table if not exists "+TABLE_CHANNEL +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				ID + " INTEGER , " +
				NAME + " TEXT , " +
				ORDERID + " INTEGER , " +
				SELECTED + " SELECTED)";
		db.execSQL(sql);
		db.execSQL(CREATE_CLASSNAME_TABLE);
		db.execSQL(CREATE_CARDCONTENT_TABLE);
	}



	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 更改数据库版本的操作
		onCreate(db);
	}

}
