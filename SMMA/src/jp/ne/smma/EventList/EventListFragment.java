package jp.ne.smma.EventList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.ne.smma.R;
import jp.ne.smma.EventList.Controller.AlertDialogManager;
import jp.ne.smma.EventList.Controller.EventListAdapter;
import jp.ne.smma.Ultis.ConnectionDetector;
import jp.ne.smma.Ultis.Constance;
import jp.ne.smma.Ultis.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.RootElement;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.costum.android.widget.LoadMoreListView;

public class EventListFragment extends Fragment {

	public static final String KEY_SUCCESS = "success";
	public static final String KEY_ID = "ev_id";
	public static final String KEY_CAT_ID = "ev_cat_id";
	public static final String KEY_NAME = "ev_company_name";
	public static final String KEY_ADDRESS = "ev_name";
	public static final String KEY_DAY = "ev_date";
	public static final String KEY_ERROR = "error";
	public static final String KEY_IMG_COLOR = "ev_color";
	public static final String KEY_IMG_URL = "ev_path_image";
	public static final String url = "http://dev9.ominext.com/smma/?page_id=27649";
	// ListView list;
	private EventListAdapter adapter;
	private ArrayList<HashMap<String, String>> eventList;
	private ArrayList<HashMap<String, String>> items;
	private ListView listEvent;
	// Alert dialog manager
	private AlertDialogManager alert = new AlertDialogManager();
	private ProgressDialog pDialog;
	View btnLoadMore;
	private String getImgTAG = "";
	int count = 0;
	int lengJson = 0;
	private int pageNumber = 1;
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	View mHeader;
	public JSONArray mJsonArray;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.event_list, container, false);
		btnLoadMore = inflater.inflate(R.layout.event_list_footer, null, false);
		listEvent = (ListView) rootView.findViewById(R.id.list_event);
		mHeader = inflater.inflate(R.layout.event_list_header, null, false);
		listEvent.addHeaderView(mHeader);
		listEvent.addFooterView(btnLoadMore);

		/**
		 * Listening to Load More button click event
		 * */
		btnLoadMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Starting a new async task
				new loadMoreListView().execute();
			}
		});
		listEvent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> map = eventList.get(position - 1);
				String itemId = map.get(KEY_ID);
				String name = map.get(KEY_NAME);
				Intent intent = new Intent(getActivity(), ProductActivity.class);
				intent.putExtra("itemId", itemId);
				intent.putExtra("name", name);

				startActivity(intent);
			}
		});

		// ((LoadMoreListView) listEvent)
		// .setOnLoadMoreListener(new OnLoadMoreListener() {
		// public void onLoadMore() {
		// // Do the work to load more items at the end of list
		// // here
		// new LoadDataTask().execute();
		// }
		// });
		// test code
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		cd = new ConnectionDetector(getActivity().getApplicationContext());
		// list = (ListView)findViewById(R.id.list);

		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {
			eventList = new ArrayList<HashMap<String, String>>();
			new GetList().execute();
		} else {
			showAlertDialog(getActivity(), "No Internet Connection",
					"You don't have internet connection, please try again",
					false);
		}

	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetList extends AsyncTask<Void, Void, Void> {
		private Boolean bCheck = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}

		protected Void doInBackground(Void... arg0) {
			try {
				// Creating service handler class instance
				JSONParser jsonParser = new JSONParser();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				// POST API to server
				params.add(new BasicNameValuePair("name_event", "get-event-all"));
				params.add(new BasicNameValuePair("id", String
						.valueOf(pageNumber)));

				JSONObject mJson = jsonParser.getJSONFromUrl(url, params);
				pageNumber = pageNumber + 1;
				if (mJson == null) {
					showAlertDialog(getActivity(), "", "TIME OUT", false);
				} else {
					try {
						if (mJson.getString(KEY_SUCCESS) != null) {
							// Toast.makeText(this, "success = 0",
							// Toast.LENGTH_LONG).show();
							String res = mJson.getString(KEY_SUCCESS);
							if (Integer.parseInt(res) == 1) {
								mJsonArray = mJson.getJSONArray("data");
								// mJsonArray = jsonData;
								for (int i = 0; i < mJsonArray.length(); i++) {
									HashMap<String, String> map = new HashMap<String, String>();
									JSONObject json = mJsonArray
											.getJSONObject(i);
									map.put(KEY_ID, json.getString("ev_id"));
									map.put(KEY_CAT_ID,
											json.getString("ev_cat_id"));
									map.put(KEY_NAME,
											json.getString("ev_company_name"));
									map.put(KEY_ADDRESS,
											json.getString("ev_name"));
									map.put(KEY_DAY, json.getString("ev_date"));
									map.put(KEY_IMG_COLOR,
											json.getString("ev_color"));
									map.put(KEY_IMG_URL,
											json.getString("ev_path_image"));
									Log.d("KEY_IMG_URL", map.get(KEY_IMG_URL));
									eventList.add(map);

								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						// bCheck = true;

					}
				}
			} catch (Exception ex) {
				// showAlertDialog(getActivity(), "", "TIME OUT", false);
				// bCheck = true;
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			// dismiss the dialog after getting all tracks
			pDialog.dismiss();
			if (Constance.bCheckNetworkTimeOut) {
				showAlertDialog(
						getActivity(),
						"",
						"Can not get data from server, please check internet and try again",
						false);

			} else {
				// updating UI from Background Thread
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						// list = (ListView) findViewById(R.id.list);

						// Getting adapter by passing xml data ArrayList
						adapter = new EventListAdapter(getActivity(), eventList);
						listEvent.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						// Click event for single list row
					}
				});
			}
		}
	}

	/**
	 * Async Task that send a request to url Gets new list view data Appends to
	 * list view
	 * */
	private class loadMoreListView extends AsyncTask<Void, Void, Void> {
		Boolean checkEventNumber = false;

		@Override
		protected void onPreExecute() {
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("loading list events..");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			listEvent.setClickable(false);
			// getActivity().runOnUiThread(new Runnable() {
			// public void run() {
			// listEvent.setOnItemClickListener(new OnItemClickListener() {
			// @Override
			// public void onItemClick(AdapterView<?> parent,
			// View view, int position, long id) {
			// v.
			// }
			// });
			// }
			// });
			try {
				JSONParser jsonParser = new JSONParser();
				List<NameValuePair> params1 = new ArrayList<NameValuePair>();
				// POST API to server
				params1.add(new BasicNameValuePair("name_event",
						"get-event-all"));
				params1.add(new BasicNameValuePair("id", String
						.valueOf(pageNumber)));

				JSONObject mJson = jsonParser.getJSONFromUrl(url, params1);
				pageNumber = pageNumber + 1;
				if (mJson == null) {
					showAlertDialog(getActivity(), "", "TIME OUT", false);
				} else {
					try {
						if (mJson.getString(KEY_SUCCESS) != null) {
							// Toast.makeText(this, "success = 0",
							// Toast.LENGTH_LONG).show();
							String res = mJson.getString(KEY_SUCCESS);
							if (Integer.parseInt(res) == 1) {
								mJsonArray = mJson.getJSONArray("data");
								// mJsonArray = jsonData;
								for (int i = 0; i < mJsonArray.length(); i++) {
									HashMap<String, String> map = new HashMap<String, String>();
									JSONObject json = mJsonArray
											.getJSONObject(i);
									map.put(KEY_ID, json.getString("ev_id"));
									map.put(KEY_CAT_ID,
											json.getString("ev_cat_id"));
									map.put(KEY_NAME,
											json.getString("ev_company_name"));
									map.put(KEY_ADDRESS,
											json.getString("ev_name"));
									map.put(KEY_DAY, json.getString("ev_date"));
									map.put(KEY_IMG_COLOR,
											json.getString("ev_color"));
									map.put(KEY_IMG_URL,
											json.getString("ev_path_image"));
									Log.d("KEY_IMG_URL", map.get(KEY_IMG_URL));
									eventList.add(map);

								}
							} else {
								checkEventNumber = true;

							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						// bCheck = true;

					}
				}
			} catch (Exception ex) {
				// showAlertDialog(getActivity(), "", "TIME OUT", false);
				// bCheck = true;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			// dismiss the dialog after getting all tracks
			// updating UI from Background Thread
			pDialog.dismiss();
			
			if (checkEventNumber == true) {
				showAlertDialog(getActivity(), "", "no event loaded", false);

			}
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					// list = (ListView) findViewById(R.id.list);
					// get listview current position - used to maintain
					// scroll
					// position
					int currentPosition = listEvent.getFirstVisiblePosition();
					// Appending new data to menuItems ArrayList
					adapter = new EventListAdapter(getActivity(), eventList);
					listEvent.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					// Setting new scroll position
					listEvent.setSelectionFromTop(currentPosition + 1, 0);
				}

			});

			listEvent.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					HashMap<String, String> map = eventList.get(position - 1);
					String itemId = map.get(KEY_ID);
					String name = map.get(KEY_NAME);
					Intent intent = new Intent(getActivity(),
							ProductActivity.class);
					intent.putExtra("itemId", itemId);
					intent.putExtra("name", name);

					startActivity(intent);
				}
			});

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

	@SuppressWarnings("deprecation")
	public void showAlert(final Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);
		alertDialog.setCancelable(false);
		// Setting Dialog Message
		alertDialog.setMessage(message);
		// Showing Alert Message
		alertDialog.show();
	}
}
