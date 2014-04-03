package jp.ne.smma.EventList;

import java.util.ArrayList;
import java.util.List;

import jp.ne.smma.R;
import jp.ne.smma.EventList.Controller.AlertDialogManager;
import jp.ne.smma.Ultis.ApplicationUntils;
import jp.ne.smma.Ultis.ConnectionDetector;
import jp.ne.smma.Ultis.Constance;
import jp.ne.smma.Ultis.JSONParser;
import jp.ne.smma.aboutsmma.DTO.RowAboutItem;
import jp.ne.smma.aboutsmma.adapter.CustomListViewAdapter;

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * About item
 */
public class AboutFragment extends Fragment {
	private ProgressDialog aboutDialog;
	private AlertDialogManager alert = new AlertDialogManager();
	public JSONArray mJsonArray;
	View mHeader;
	Boolean isInternet = false;
	ConnectionDetector checkInternet;
	CustomListViewAdapter adapter;

	List<RowAboutItem> rowItems;

	/* End content listview */
	private ListView listview;
	private TextView txtContentTitle;
	private ImageView bannerShow;
	View mFooter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.about_activity, container,
				false);
		
		// get data from XML
		listview = (ListView) rootView.findViewById(R.id.listview);
		mHeader = inflater.inflate(R.layout.about_header_view, null, false);
		mFooter = inflater.inflate(R.layout.event_footer_about, null, false);
		
		// txtContentTitle = (TextView) rootView
		// .findViewById(R.id.txtContentAbout);
		// add data list view
		rowItems = new ArrayList<RowAboutItem>();
		listview.addHeaderView(mHeader, null, false);
		listview.addFooterView(mFooter, null, false);
		bannerShow=(ImageView)mFooter.findViewById(R.id.bannerShow);
		// for (int i = 0; i < strContent.length; i++) {
		// RowAboutItem item = new RowAboutItem(colorCode[i], imagesItem[i],
		// strContent[i]);
		// rowItems.add(item);
		// }

		// set text content
		// set click listview
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				RowAboutItem item = rowItems.get(pos - 1);
				String colorCode = item.getColorCode();
				String companyName = item.getStrContent();
				String placeID = item.getPlaceId();
				double latitude = item.getLatitude();
				double longitude = item.getLongitude();
				// Intent intent = new
				// Intent(getActivity(),PlaceDetailActivity.class);
				// intent.putExtra("colorCode", colorCode);
				// intent.putExtra("companyName", companyName);
				// intent.putExtra("placeID", placeID);
				// intent.putExtra("latitude", latitude);
				// intent.putExtra("longitude", longitude);
				ApplicationUntils.gotoActivityPlaceDetail(getActivity(),
						PlaceDetailActivity.class, colorCode, companyName,
						placeID, latitude, longitude);

			}
		});
		// listview.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent,
		// View view, int position, long id) {
		// RowAboutItem item = listview
		// .get(position);
		// String itemId = map.get(KEY_ID);
		// String name = map.get(KEY_NAME);
		// Intent intent = new Intent(getActivity(),
		// ProductActivity.class);
		// intent.putExtra("itemId", itemId);
		// intent.putExtra("name", name);
		//
		// startActivity(intent);
		// }
		// });
		bannerShow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url = getString(R.string.about_footer_banner_url);
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		checkInternet = new ConnectionDetector(getActivity()
				.getApplicationContext());
		// list = (ListView)findViewById(R.id.list);

		isInternet = checkInternet.isConnectingToInternet();
		if (isInternet) {
			new GetAllCompany().execute();
		} else {
			showAlertDialog(getActivity(), "No Internet Connection",
					"You don't have internet connection, please try again",
					false);
		}
	}

	/**
	 * Async task class to get all company by making HTTP call
	 * */
	private class GetAllCompany extends AsyncTask<Void, Void, Void> {
		private Boolean bCheck = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			// aboutDialog = new ProgressDialog(getActivity());
			// aboutDialog.setMessage("loading company data...");
			// aboutDialog.setIndeterminate(false);
			// aboutDialog.setCancelable(false);
			// aboutDialog.show();

		}

		protected Void doInBackground(Void... arg0) {
			try {
				// Creating service handler class instance
				JSONParser jsonParser = new JSONParser();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("name_event",
						"get-company-all"));
				JSONObject mJson = jsonParser.getJSONFromUrl(Constance.url,
						params);
				if (mJson == null) {
					showAlertDialog(getActivity(), "", "TIME OUT", false);
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
									// Log.e("json about",
									// json.getString(Constance.KEY_ABOUT_COLOR)+
									// json.getString(Constance.KEY_ABOUT_PATH_IMAGE)+json.getString(Constance.KEY_ABOUT_COMPANY_NAME));
									RowAboutItem item = new RowAboutItem(
											json.getString(Constance.KEY_ABOUT_COLOR),
											json.getString(Constance.KEY_ABOUT_PATH_IMAGE),
											json.getString(Constance.KEY_ABOUT_COMPANY_NAME),
											json.getString(Constance.KEY_ABOUT_PLACE),
											json.getDouble(Constance.KEY_ABOUT_LAT),
											json.getDouble(Constance.KEY_ABOUT_LONG));
									rowItems.add(item);
									Log.d("item",
											item.getColorCode()
													+ item.getPathImageItem()
													+ item.getStrContent()
													+ item.getPlaceId()
													+ item.getLatitude()
													+ item.getLongitude());
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						bCheck = true;

					}
				}
			} catch (Exception ex) {
				// showAlertDialog(getActivity(), "", "TIME OUT", false);
				bCheck = true;
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			// dismiss the dialog after getting all tracks
			// aboutDialog.dismiss();
			// if (Constance.bCheckNetworkTimeOut) {
			// showAlertDialog(
			// getActivity(),
			// "",
			// "Can not get data from server, please check internet and try again",
			// false);
			//
			// } else {
			// if (!Constance.bCheckNetworkTimeOut){
			// updating UI from Background Thread
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					// txtContentTitle
					// .setText("知的情報資源である仙台・宮城地域のさまざまな博物館が共働することで、地域にとってより有益な機能を獲得していくための共同事業体です。各館の学芸員や専門職員が持つ知識やノウハウを集積し、分野を横断した連携イベント、学校教育への協力や地域で活動する人材の育成支援、観光資源の開発など、単館では実現困難な新たな価値の創出を行い、地域のニーズに合った新時代のミュージアムとなることを目指します。");
					adapter = new CustomListViewAdapter(getActivity(),
							R.layout.item_list_about, rowItems);
					listview.setAdapter(adapter);
					// Click event for single list row

				}
			});
		}
	}

	// }
	// }

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


}
