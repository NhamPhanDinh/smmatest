package jp.ne.smma.EventList.Controller;

import java.util.ArrayList;
import java.util.List;

import jp.ne.smma.EventList.EventCalendarFragment;
import jp.ne.smma.EventList.SettingActivity;
import jp.ne.smma.Ultis.ConnectionDetector;
import jp.ne.smma.Ultis.Constance;
import jp.ne.smma.Ultis.JSONParser;
import jp.ne.smma.aboutsmma.DTO.ItemCalendar;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

/**
 * Get data event calendar
 */
public abstract class GetDataEventCalendar {
	private Context mContext;
	private ProgressDialog aboutDialog;
	private int pageNumber = 0;
	private JSONArray mJsonArray;
	ItemCalendar item;
	ConnectionDetector checkInternet;
	Boolean isInternet;
	ArrayList<ItemCalendar> rowCalendar = new ArrayList<ItemCalendar>();;

	/*
	 * Contructor class
	 */
	public GetDataEventCalendar(Context context, int pageNumber) {
		this.mContext = context;
		this.pageNumber = pageNumber;
		runGetData();

	}

	public void runGetData() {
		// run assytask
		checkInternet = new ConnectionDetector(mContext.getApplicationContext());

		isInternet = checkInternet.isConnectingToInternet();
		if (isInternet) {
			new GetEventCalendar().execute();
		} else {
			showAlertDialog(mContext, "No Internet Connection",
					"You don't have internet connection, please try again",
					false);
		}
	}

	/**
	 * Async task class to get all company by making HTTP call
	 * */
	private class GetEventCalendar extends AsyncTask<Void, Void, Void> {
		int colorMode = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			aboutDialog = new ProgressDialog(mContext);
			aboutDialog.setMessage("loading data...");
			aboutDialog.setIndeterminate(false);
			aboutDialog.setCancelable(false);
			aboutDialog.show();

		}

		protected Void doInBackground(Void... arg0) {
			try {
				// Creating service handler class instance
				JSONParser jsonParser = new JSONParser();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("name_event", "get-calendar"));
				params.add(new BasicNameValuePair("id", String
						.valueOf(pageNumber)));
				JSONObject mJson = jsonParser.getJSONFromUrl(Constance.url,
						params);

				if (mJson == null) {

					showAlertDialog(mContext, "",
							"Time out when connect server. Please try again",
							false);
				} else {
					try {
						if (mJson.getString(Constance.KEY_SUCCESS) != null) {
							// Toast.makeText(this, "success = 0",
							// Toast.LENGTH_LONG).show();
							String res = mJson.getString(Constance.KEY_SUCCESS);
							if (Integer.parseInt(res) == 1) {
								mJsonArray = mJson
										.getJSONArray(Constance.KEY_DATA);
								// mJsonArray = jsonData;

								for (int i = 0; i < mJsonArray.length(); i++) {
									JSONObject json = mJsonArray
											.getJSONObject(i);
									// get icon
									for (int j = 0; j < Constance.colorCodeCompany.length; j++) {
										if (json.getString(
												Constance.CALENDAR_KEY_COLOR)
												.equals(Constance.colorCodeCompany[j]))
											colorMode = Constance.imagesItemCompany[j];
									}

									// fill data
									item = new ItemCalendar(
											json.getString(Constance.CALENDAR_KEY_ID),
											json.getString(Constance.CALENDAR_EVENT_ID),
											json.getString(Constance.CALENDAR_KEY_COMPANYNAME),
											json.getString(Constance.CALENDAR_KEY_EVENTNAME),
											json.getString(Constance.CALENDAR_KEY_STARTDAY),
											json.getString(Constance.CALENDAR_KEY_ENDDAY),
											colorMode,
											json.getString(Constance.CALENDAR_KEY_COLOR));
									rowCalendar.add(item);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();

					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			// dismiss the dialog after getting all tracks
			aboutDialog.dismiss();
			Constance.listItemCalendar = rowCalendar;
			OnTaskCompleted();

		}
	}

	@SuppressWarnings("deprecation")
	public void showAlertDialog(final Context context, String title,
			String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);
		alertDialog.setCancelable(false);
		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				((Activity) context).finish();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

	/**
	 * Get item calendar
	 * 
	 * @return ItemCalendar
	 */
	public ArrayList<ItemCalendar> getItemCalendar() {
		return rowCalendar;

	}

	public abstract void OnTaskCompleted();
}
