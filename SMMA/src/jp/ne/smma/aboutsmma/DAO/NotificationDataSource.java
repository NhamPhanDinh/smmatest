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
import android.util.Log;

/**
 * Notification data source
 */
public class NotificationDataSource {
	// Database fields
	private SQLiteDatabase database;
	private DatabaseHandler dbHelper;
	private String[] allColumns = { DatabaseHandler.COLUMN_ID,
			DatabaseHandler.COLUMN_ID_EVENT, DatabaseHandler.COLUMN_NAME,
			DatabaseHandler.COLUMN_STARTDAY, DatabaseHandler.COLUMN_ENDDAY,
			DatabaseHandler.COLUMN_CHOOSE_DAY, DatabaseHandler.COLUMN_VALUE,
			DatabaseHandler.COLUMN_NOTE, DatabaseHandler.COLUMN_CHECK,
			DatabaseHandler.COLUMN_STATUS,
			DatabaseHandler.COLUMN_VALUE_NOTIFICATION };

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
	 * 
	 * @param valueNotification
	 *            - valueNotification
	 */
	public NotificationItem createNotification(int idevent, String name,
			String startday, String endday, String chooseday, String value,
			String note, String check, String status, String valueNotification) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.COLUMN_ID_EVENT, idevent);
		values.put(DatabaseHandler.COLUMN_NAME, name);

		values.put(DatabaseHandler.COLUMN_STARTDAY, startday);
		values.put(DatabaseHandler.COLUMN_ENDDAY, endday);
		values.put(DatabaseHandler.COLUMN_CHOOSE_DAY, chooseday);

		values.put(DatabaseHandler.COLUMN_VALUE, value);
		values.put(DatabaseHandler.COLUMN_NOTE, note);
		values.put(DatabaseHandler.COLUMN_CHECK, check);
		values.put(DatabaseHandler.COLUMN_STATUS, status);
		values.put(DatabaseHandler.COLUMN_VALUE_NOTIFICATION, valueNotification);

		long insertId = database.insert(DatabaseHandler.TBL_NOTIFICATION, null,
				values);

		Cursor cursor = database.query(DatabaseHandler.TBL_NOTIFICATION,
				allColumns, DatabaseHandler.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		NotificationItem notification = cursorToNotification(cursor);
		cursor.close();
		return notification;
	}

	/**
	 * Update choose day by id
	 * 
	 * @return arraylist status
	 */
	public Boolean updateChooseDayByID(String value, int id, String notifiation) {
		try {
			String querySql = "UPDATE  " + DatabaseHandler.TBL_NOTIFICATION
					+ " " + "SET " + DatabaseHandler.COLUMN_CHOOSE_DAY + " = '"
					+ value + " '," + DatabaseHandler.COLUMN_VALUE + "='"
					+ notifiation + "' WHERE "
					+ DatabaseHandler.COLUMN_ID_EVENT + " = " + id;
			Cursor cursor = database.rawQuery(querySql, null);

			cursor.moveToFirst();
			cursor.getCount();
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * Delete notification
	 */
	public void deleteNotification(NotificationItem notification) {
		long id = notification.getId();
		database.delete(DatabaseHandler.TBL_NOTIFICATION,
				DatabaseHandler.COLUMN_ID + " = " + id, null);
	}

	/**
	 * Delete all notification
	 * 
	 */
	public void deleteAllNotication() {
		database.delete(DatabaseHandler.TBL_NOTIFICATION, null, null);
	}

	/**
	 * Get all cursorToNotification
	 * 
	 * @return List<cursorToNotification>
	 */
	public List<NotificationItem> getAllNotification() {
		List<NotificationItem> users = new ArrayList<NotificationItem>();

		Cursor cursor = database.query(DatabaseHandler.TBL_NOTIFICATION,
				allColumns, null, null, null, null, null);

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
	 * Getting all status
	 * 
	 * @return arraylist status
	 */
	public ArrayList<String> getStatus() {
		ArrayList<String> status = new ArrayList<String>();

		String querySql = "SELECT  " + DatabaseHandler.COLUMN_STATUS + " "
				+ "FROM " + DatabaseHandler.TBL_NOTIFICATION;

		Cursor cursor = database.rawQuery(querySql, null);

		cursor.moveToFirst();
		cursor.getCount();
		while (!cursor.isAfterLast()) {
			String time = cursor.getString(0);
			status.add(time);
			cursor.moveToNext();
		}
		cursor.close();

		return status;
	}

	/**
	 * Getting all check equal 0, not set special
	 * 
	 * @return arraylist check equal = 0
	 */
	public ArrayList<String> getCheckEqualZero() {
		ArrayList<String> status = new ArrayList<String>();

		String querySql = "SELECT  " + DatabaseHandler.COLUMN_CHECK + " "
				+ "FROM " + DatabaseHandler.TBL_NOTIFICATION + " WHERE "
				+ DatabaseHandler.COLUMN_CHECK + " = 0";

		Cursor cursor = database.rawQuery(querySql, null);

		cursor.moveToFirst();
		cursor.getCount();
		while (!cursor.isAfterLast()) {
			String time = cursor.getString(0);
			status.add(time);
			cursor.moveToNext();
		}
		cursor.close();

		return status;
	}

	/**
	 * Getting all choose day if check = 0
	 * 
	 * @return arraylist check equal = 0
	 */
	public ArrayList<String> getChooseDayZero() {
		ArrayList<String> status = new ArrayList<String>();

		String querySql = "SELECT  " + DatabaseHandler.COLUMN_CHOOSE_DAY + " "
				+ "FROM " + DatabaseHandler.TBL_NOTIFICATION + " WHERE "
				+ DatabaseHandler.COLUMN_CHECK + " = 0";

		Cursor cursor = database.rawQuery(querySql, null);

		cursor.moveToFirst();
		cursor.getCount();
		while (!cursor.isAfterLast()) {
			String time = cursor.getString(0);
			status.add(time);
			cursor.moveToNext();
		}
		cursor.close();

		return status;
	}

	/**
	 * Update choose day zero
	 * 
	 * @return arraylist status
	 */
	public Boolean updateChooseDayZero(ArrayList<String> zeroValue) {
		try {
			Cursor cursor = null;
			for (int i = 0; i < zeroValue.size(); i++) {
				String querySql = "UPDATE  " + DatabaseHandler.TBL_NOTIFICATION
						+ " " + "SET " + DatabaseHandler.COLUMN_CHOOSE_DAY
						+ " = '" + zeroValue.get(i) + " '";
				cursor = database.rawQuery(querySql, null);

				cursor.moveToFirst();
				cursor.getCount();
			}
			cursor.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * Update all status, if true 1, else false 0
	 * 
	 * @param value
	 *            - value 1 or 0
	 * @param id
	 *            -id database
	 * @return arraylist status
	 */
	public Boolean updateAllStatus(String value) {
		try {
			String querySql = "UPDATE  " + DatabaseHandler.TBL_NOTIFICATION
					+ " " + "SET " + DatabaseHandler.COLUMN_STATUS + " = '"
					+ value + " '";
			Cursor cursor = database.rawQuery(querySql, null);

			cursor.moveToFirst();
			cursor.getCount();
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * Update status, if true 1, else false 0
	 * 
	 * @param value
	 *            - value 1 or 0
	 * @param id
	 *            -id database
	 * @return arraylist status
	 */
	public Boolean updateStatus(String value, int id) {
		try {
			String querySql = "UPDATE  " + DatabaseHandler.TBL_NOTIFICATION
					+ " " + "SET " + DatabaseHandler.COLUMN_STATUS + " = '"
					+ value + "' WHERE " + DatabaseHandler.COLUMN_ID + " = "
					+ (id+1);
			Cursor cursor = database.rawQuery(querySql, null);

			Log.i("", "querySql: "+querySql);
			cursor.moveToFirst();
			cursor.getCount();
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * Remove record value
	 */
	public Boolean removeValueRecord(int id) {
		try {
			String querySql = "DELETE FROM " + DatabaseHandler.TBL_NOTIFICATION
					+ " WHERE " + DatabaseHandler.COLUMN_ID_EVENT + " = " + id;
			Cursor cursor = database.rawQuery(querySql, null);

			cursor.moveToFirst();
			cursor.getCount();
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * Update choose day
	 */
	public Boolean updateChooseDay(String value, int id) {
		try {
			String querySql = "UPDATE  " + DatabaseHandler.TBL_NOTIFICATION
					+ " " + "SET " + DatabaseHandler.COLUMN_CHOOSE_DAY + " = '"
					+ value + "'," + DatabaseHandler.COLUMN_CHECK
					+ " = '1'  WHERE " + DatabaseHandler.COLUMN_ID + " = "
					+ (id + 1);
			Cursor cursor = database.rawQuery(querySql, null);

			cursor.moveToFirst();
			cursor.getCount();
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * get id via id_event
	 */
	public int getIdViaIdEvent(int idEvent) {
		int time = 0;
		try {
			String querySql = "SELECT  " + DatabaseHandler.COLUMN_ID + " "
					+ "FROM " + DatabaseHandler.TBL_NOTIFICATION + " WHERE "
					+ DatabaseHandler.COLUMN_ID_EVENT + " = " + (idEvent);
			Cursor cursor = database.rawQuery(querySql, null);

			cursor.moveToFirst();
			cursor.getCount();
			while (!cursor.isAfterLast()) {
				time = cursor.getInt(0);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("", "Id get via IdEvent: " + time);
		return time;

	}

	/**
	 * get choose day
	 */
	public String getDayChooseDay(int id) {
		String time = null;
		try {
			String querySql = "SELECT  " + DatabaseHandler.COLUMN_CHOOSE_DAY
					+ " " + "FROM " + DatabaseHandler.TBL_NOTIFICATION
					+ " WHERE " + DatabaseHandler.COLUMN_ID_EVENT + " = "
					+ (id);
			Log.i("", "Querry: " + querySql);
			Cursor cursor = database.rawQuery(querySql, null);

			cursor.moveToFirst();
			cursor.getCount();
			while (!cursor.isAfterLast()) {
				time = cursor.getString(0);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("", "Time choose day get DB: " + time);
		return time;

	}

	/**
	 * get choose day
	 */
	public String getDayChooseDayIdRecord(int idRecord) {
		String time = null;
		try {
			String querySql = "SELECT  " + DatabaseHandler.COLUMN_CHOOSE_DAY
					+ " " + "FROM " + DatabaseHandler.TBL_NOTIFICATION
					+ " WHERE " + DatabaseHandler.COLUMN_ID + " = "
					+ (idRecord + 1);
			Log.i("", "Querry: " + querySql);
			Cursor cursor = database.rawQuery(querySql, null);

			cursor.moveToFirst();
			cursor.getCount();
			while (!cursor.isAfterLast()) {
				time = cursor.getString(0);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("", "Time choose day get DB: " + time);
		return time;

	}

	/**
	 * get value day
	 */
	public String getValueDay(int id) {
		String time = null;
		try {
			String querySql = "SELECT  " + DatabaseHandler.COLUMN_VALUE + " "
					+ "FROM " + DatabaseHandler.TBL_NOTIFICATION + " WHERE "
					+ DatabaseHandler.COLUMN_ID_EVENT + " = " + (id);
			Log.i("", "Querry: " + querySql);
			Cursor cursor = database.rawQuery(querySql, null);

			cursor.moveToFirst();
			cursor.getCount();
			while (!cursor.isAfterLast()) {
				time = cursor.getString(0);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("", "Value get DB: " + time);
		return time;

	}

	/**
	 * Getting notification Count
	 * 
	 * @return count recort database notification
	 */
	public int getNotificationCount() {
		int count = 0;
		String countQuery = "SELECT  * FROM "
				+ DatabaseHandler.TBL_NOTIFICATION;
		Cursor cursor = database.rawQuery(countQuery, null);
		count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	/**
	 * 3 Cast cursor to notification
	 * 
	 * @param cursor
	 */
	private NotificationItem cursorToNotification(Cursor cursor) {
		NotificationItem notificationItem = new NotificationItem();

		notificationItem.setId(cursor.getInt(0));
		notificationItem.setId_event(cursor.getInt(1));
		notificationItem.setName(cursor.getString(2));
		notificationItem.setStartday(cursor.getString(3));
		notificationItem.setEndday(cursor.getString(4));
		notificationItem.setChooseday(cursor.getString(5));
		notificationItem.setValue(cursor.getString(6));
		notificationItem.setNote(cursor.getString(7));
		notificationItem.setCheck(cursor.getString(8));
		notificationItem.setStatus(cursor.getString(9));
		notificationItem.setValueNotification(cursor.getString(10));

		return notificationItem;
	}
}
