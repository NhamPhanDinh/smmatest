package jp.ne.smma.aboutsmma.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * Database handler
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
	// Database Name
	private static final String DATABASE_NAME = "SMMA.db";
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Users table Name
	public static final String TBL_NOTIFICATION = "notification";

	// Users Table Columns names
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "_name";
	public static final String COLUMN_VALUE= "_value";
	public static final String COLUMN_DATE = "_date";
	public static final String COLUMN_STATUS = "_status"; //0: nothing, 1: 1 month, 2: 3 month, 3: other

	// Database creation sql statement
	private static final String CREATE_NOTIFICATION_TABLE = "create table " 
			+ TBL_NOTIFICATION
			+ "(" 
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_NAME + " text not null, " 
			+ COLUMN_VALUE + " text not null, " 
			+ COLUMN_DATE + " text not null, "
			+ COLUMN_STATUS + " text not null"
			+ ");";
	

	public DatabaseHandler(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL(CREATE_NOTIFICATION_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(DatabaseHandler.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_NOTIFICATION_TABLE);
		onCreate(db);
	}
}
