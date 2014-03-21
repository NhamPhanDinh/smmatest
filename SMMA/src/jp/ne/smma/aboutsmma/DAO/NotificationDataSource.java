package jp.ne.smma.aboutsmma.DAO;

import java.util.ArrayList;
import java.util.List;

import jp.ne.smma.aboutsmma.DTO.NotificationItem;
import jp.ne.smma.aboutsmma.database.DatabaseHandler;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Notification data source
 */
public class NotificationDataSource {
	// Database fields
	private SQLiteDatabase database;
	private DatabaseHandler dbHelper;
	private String[] allColumns = { DatabaseHandler.COLUMN_ID,
			DatabaseHandler.COLUMN_NAME, DatabaseHandler.COLUMN_VALUE,
			DatabaseHandler.COLUMN_DATE, DatabaseHandler.COLUMN_STATUS };

	/**
	 * Constructor
	 * 
	 * 
	 * @param context
	 */
	public NotificationDataSource(Context context) {
		dbHelper = new DatabaseHandler(context);
	}

	/**
	 * Open database
	 * 
	 * 
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	/**
	 * Close database
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * Create notification
	 */
	public NotificationItem createNotification(int id, String name, String value,
			String date, String status ) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.COLUMN_ID, id);
		values.put(DatabaseHandler.COLUMN_NAME, name);
		values.put(DatabaseHandler.COLUMN_VALUE, value);
		values.put(DatabaseHandler.COLUMN_DATE, date);
		values.put(DatabaseHandler.COLUMN_STATUS, status);

		long insertId = database.insert(DatabaseHandler.TBL_NOTIFICATION, null, values);

		Cursor cursor = database.query(DatabaseHandler.TBL_NOTIFICATION, allColumns,
				DatabaseHandler.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		NotificationItem notification = cursorToNotification(cursor);
		cursor.close();
		return notification;
	}



	/**
	 * Delete notification
	 */
	public void deleteUser(NotificationItem notification) {
		long id = notification.getId();
		database.delete(DatabaseHandler.TBL_NOTIFICATION, DatabaseHandler.COLUMN_ID
				+ " = " + id, null);
	}

	/**
	 * Get all cursorToNotification
	 * 
	 * @return List<cursorToNotification>
	 */
	public List<NotificationItem> getAllUser() {
		List<NotificationItem> users = new ArrayList<NotificationItem>();

		Cursor cursor = database.query(DatabaseHandler.TBL_NOTIFICATION, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			NotificationItem cursorToNotification = cursorToNotification(cursor);
			users.add(cursorToNotification);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return users;
	}



	/**
	 * Cast cursor to notification
	 * 
	 * @param cursor
	 */
	private NotificationItem cursorToNotification(Cursor cursor) {
		NotificationItem notificationItem = new NotificationItem();

		notificationItem.setId(cursor.getInt(0));
		notificationItem.setName(cursor.getString(1));
		notificationItem.setValue(cursor.getString(2));
		notificationItem.setDate(cursor.getString(3));
		notificationItem.setStatus(cursor.getString(4));

		return notificationItem;
	}
}
