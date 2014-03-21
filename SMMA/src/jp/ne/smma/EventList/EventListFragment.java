package jp.ne.smma.EventList;

import java.net.ContentHandler;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;

public class EventListFragment extends Fragment {

	public static final String KEY_SUCCESS = "success";
	public static final String KEY_ID = "id";
	public static final String KEY_NAME = "name";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_DAY = "day";
	public static final String KEY_ERROR = "error";
	public static final String KEY_IMG_URL = "pathimage";
	public static final String url = "http://nhampd.orgfree.com/get_all_eventlist.php";
	// ListView list;
	private EventListAdapter adapter;
	private ArrayList<HashMap<String, String>> eventList;
	private ArrayList<HashMap<String, String>> items;
	private LoadMoreListView listEvent;
	// Alert dialog manager
	private AlertDialogManager alert = new AlertDialogManager();
	private ProgressDialog pDialog;
	Button btnLoadMore;
	private String getImgTAG = "";
	int count = 0;
	int lengJson = 0;
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	public JSONArray mJsonArray;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.event_list, container, false);
		btnLoadMore = new Button(getActivity());
		btnLoadMore.setBackgroundResource(R.drawable.img_load_more);
		listEvent = (LoadMoreListView) rootView.findViewById(R.id.list_event);
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
					"You don't have internet connection, please try again", false);
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
				params.add(new BasicNameValuePair("name", "1111"));
				JSONObject mJson = jsonParser.getJSONFromUrl(url, params);
				if (mJson == null) {
					showAlertDialog(getActivity(), "", "TIME OUT", false);
					Log.e("aaaaaaa", "bbbbbbbb");
				} else {
					try {
						if (mJson.getString(KEY_SUCCESS) != null) {
							// Toast.makeText(this, "success = 0",
							// Toast.LENGTH_LONG).show();
							String res = mJson.getString(KEY_SUCCESS);
							if (Integer.parseInt(res) == 1) {
								mJsonArray = mJson.getJSONArray("data");
								// mJsonArray = jsonData;
								int d = 10;
								lengJson = mJsonArray.length();
								if (d >= mJsonArray.length())
									d = mJsonArray.length();
								for (int i = 0; i < d; i++) {
									HashMap<String, String> map = new HashMap<String, String>();
									JSONObject json = mJsonArray
											.getJSONObject(i);
									map.put(KEY_ID, json.getString("id"));
									;
									map.put(KEY_NAME, json.getString("name"));
									map.put(KEY_ADDRESS,
											json.getString("address"));
									map.put(KEY_DAY, json.getString("day"));
									map.put(KEY_IMG_URL,
											json.getString("pathimage"));
									Log.d("KEY_IMG_URL", map.get(KEY_IMG_URL));
									count = count + 1;
									eventList.add(map);

								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						//bCheck = true;

					}
				}
			} catch (Exception ex) {
				// showAlertDialog(getActivity(), "", "TIME OUT", false);
//				bCheck = true;
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
				showAlertDialog(getActivity(), "", "Can not get data from server, please check internet and try again", false);
				
			} else {
				// updating UI from Background Thread
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						// list = (ListView) findViewById(R.id.list);

						// Getting adapter by passing xml data ArrayList
						adapter = new EventListAdapter(getActivity(), eventList);
						listEvent.setAdapter(adapter);

						// Click event for single list row
						listEvent
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										HashMap<String, String> map = eventList
												.get(position);
										String itemId = map.get(KEY_ID);
										String name = map.get(KEY_NAME);
										Intent intent = new Intent(
												getActivity(),
												ProductActivity.class);
										intent.putExtra("itemId", itemId);
										intent.putExtra("name", name);

										startActivity(intent);
									}
								});
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

		@Override
		protected void onPreExecute() {
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(getActivity());
			if (count > lengJson) {
				pDialog.setMessage("no events");
			} else
				pDialog.setMessage("Please wait..");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			for (int i = count; i < count + 10; i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject json;
				try {
					json = mJsonArray.getJSONObject(i);
					map.put(KEY_ID, json.getString("id"));
					;
					map.put(KEY_NAME, json.getString("name"));
					map.put(KEY_ADDRESS, json.getString("address"));
					map.put(KEY_DAY, json.getString("day"));
					map.put(KEY_IMG_URL, json.getString("pathimage"));
					Log.d("KEY_IMG_URL", map.get(KEY_IMG_URL));
					count = count + 1;
					eventList.add(map);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			// dismiss the dialog after getting all tracks
			pDialog.dismiss();
			// updating UI from Background Thread
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					// list = (ListView) findViewById(R.id.list);
					// get listview current position - used to maintain scroll
					// position
					int currentPosition = listEvent.getFirstVisiblePosition();

					// Appending new data to menuItems ArrayList
					adapter = new EventListAdapter(getActivity(), eventList);
					listEvent.setAdapter(adapter);
					// Setting new scroll position
					listEvent.setSelectionFromTop(currentPosition + 1, 0);
					listEvent.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							HashMap<String, String> map = eventList
									.get(position);
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

			});
		}
	}

	/*
	 * private class LoadDataTask extends AsyncTask<Void, Void, Void> {
	 * 
	 * @Override protected void onPreExecute() { // Showing progress dialog
	 * before sending http request listEvent.removeFooterView(btnLoadMore); }
	 * 
	 * @Override protected Void doInBackground(Void... params) { if
	 * (isCancelled()) { return null; }
	 * 
	 * // Simulates a background task try { Thread.sleep(1000); } catch
	 * (InterruptedException e) { }
	 * 
	 * for (int i = count; i < count + 10; i++) { HashMap<String, String> map =
	 * new HashMap<String, String>(); JSONObject json; try { json =
	 * mJsonArray.getJSONObject(i); map.put(KEY_ID, json.getString("id")); ;
	 * map.put(KEY_NAME, json.getString("name")); map.put(KEY_ADDRESS,
	 * json.getString("address")); map.put(KEY_DAY, json.getString("day"));
	 * map.put(KEY_IMG_URL, json.getString("pathimage")); Log.d("KEY_IMG_URL",
	 * map.get(KEY_IMG_URL)); count = count + 1; eventList.add(map);
	 * 
	 * } catch (JSONException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 * 
	 * return null; }
	 * 
	 * @Override protected void onPostExecute(Void result) { // We need notify
	 * the adapter that the data have been changed
	 * adapter.notifyDataSetChanged(); // Call onLoadMoreComplete when the
	 * LoadMore task, has finished ((LoadMoreListView)
	 * listEvent).onLoadMoreComplete(); listEvent.addFooterView(btnLoadMore);
	 * super.onPostExecute(result); }
	 * 
	 * @Override protected void onCancelled() { // Notify the loading more
	 * operation has finished
	 * ((LoadMoreListView)listEvent).onLoadMoreComplete(); } }
	 */
	@SuppressWarnings("deprecation")
	public void showAlertDialog(final Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);
		alertDialog.setCancelable(false);
		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				((Activity)context).finish();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
}
