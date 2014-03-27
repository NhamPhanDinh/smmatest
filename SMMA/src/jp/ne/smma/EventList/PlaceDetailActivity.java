package jp.ne.smma.EventList;

import java.util.ArrayList;
import java.util.List;

import jp.ne.smma.R;
import jp.ne.smma.Ultis.ConnectionDetector;
import jp.ne.smma.Ultis.Constance;
import jp.ne.smma.Ultis.JSONParser;
import jp.ne.smma.aboutsmma.DTO.RowAboutItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Place detail activity
 */
public class PlaceDetailActivity extends Activity implements OnClickListener {
	private ImageView imgMain;
	private ImageView imageMap;
	private TextView strTitle;
	private TextView strAdd;
	private TextView strPhone;
	private TextView strUrl;
	private TextView textContent;
	private LinearLayout linearBannerPlaceDetail;

	private Intent intent;

	private int index = 0;
	private String colorCode;
	private String titleItem;
	private String placeID;
	private double latitude;
	private double longitude;
	private String content;
	private String textTel;
	private String textWeb;
	private String text1;
	private String text2;
	private String urlImagePlace;
	private WebView mWebViewDetail;

	ProgressDialog pDialog;
	public JSONArray mJsonArray;
	Boolean isInternet = false;
	ConnectionDetector checkInternet;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.place_detail_activity);
		if(Constance.checkPortrait){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			 Log.e("bbbbbbbbbb", "ffffffffffffffffff");
		}
		else{
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		// get data from xml
		linearBannerPlaceDetail = (LinearLayout) findViewById(R.id.linearBannerPlaceDetail);
		imgMain = (ImageView) findViewById(R.id.imgContent);
		strTitle = (TextView) findViewById(R.id.title);
		strAdd = (TextView) findViewById(R.id.addPlace);
		strPhone = (TextView) findViewById(R.id.telPlace);
		strUrl = (TextView) findViewById(R.id.urlPlace);
//		textContent = (TextView)findViewById(R.id.text_content);
		mWebViewDetail = (WebView)findViewById(R.id.web_view_about);
		imageMap=(ImageView)findViewById(R.id.imageMap);
		imageMap.setVisibility(View.GONE);
		// get data from
		intent = getIntent();
		titleItem = intent.getStringExtra(Constance.COLOR_TEXT_INDEX_ABOUT);
		colorCode = intent.getStringExtra(Constance.COLOR_ITEM_ABOUT);
		placeID = intent.getStringExtra(Constance.KEY_ABOUT_PLACE);
		latitude = intent.getDoubleExtra(Constance.LATITUDE_ABOUT, 40.714728);
		longitude = intent
				.getDoubleExtra(Constance.LONGITUDE_ABOUT, -73.998672);

		// set data
		// strTitle.setText(titleItem);
		// linearBannerPlaceDetail.setBackgroundColor(Color.parseColor(colorCode));
		// strAdd.setText("〒982-0815");
		// strPhone.setText("TEL : 022-307-5665");
		// strUrl.setText("仙台市太白区山田上ノ台町10-1 ");
		pDialog = new ProgressDialog(PlaceDetailActivity.this);
		new loadData().execute("");
		
		strPhone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Starting a new async task
				String phone = strPhone.getText().toString();
				String phoneNumber = phone.replaceAll("TEL: ", "");
				phoneNumber = phoneNumber.replace(" ", "");
				Log.d("phone number", phoneNumber);
				Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri
						.parse("tel:" + phoneNumber));
				startActivity(callIntent);
			}
		});
		
		strUrl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Starting a new async task
				String url = strUrl.getText().toString();
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	public void showMap(View v) {
		try {
			String label = titleItem;
			String uriBegin = "geo:" + latitude + "," + longitude;
			String query = latitude + "," + longitude + "(" + label + ")";
			String encodedQuery = Uri.encode(query);
			String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
			Uri uri = Uri.parse(uriString);
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Device don't have Google Map application",
					Toast.LENGTH_SHORT).show();
		}
	}
//	public void clickCall(View v){
//		String phone = strPhone.getText().toString();
//		String phoneNumber = phone.replaceAll("TEL: ", "");
//		phoneNumber = phoneNumber.replace(" ", "");
//		Log.d("phone number", phoneNumber);
//		Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri
//				.parse("tel:" + phoneNumber));
//		startActivity(callIntent);
//	}
	
//	public void openURL(View v){
//		String url = strUrl.getText().toString();
//		Intent i = new Intent(Intent.ACTION_VIEW);
//		i.setData(Uri.parse(url));
//		startActivity(i);
//	}

	public void backIcon(View v) {
		finish();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (pDialog != null) {
			pDialog.dismiss();
		}
	}

	private class loadData extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("id", placeID));
			params1.add(new BasicNameValuePair("name_event",
					"get-company-place"));
			JSONObject mJson = jsonParser
					.getJSONFromUrl(Constance.url, params1);
			if (mJson == null) {
				// showAlertDialog(getActivity(), "", "TIME OUT", false);
				// Log.e("aaaaaaa", "bbbbbbbb");
			} else {
				try {
					// Toast.makeText(this, "success = 0",
					// Toast.LENGTH_LONG).show();
					mJsonArray = mJson.getJSONArray(Constance.KEY_DATA);
					// mJsonArray = jsonData;

					for (int i = 0; i < mJsonArray.length(); i++) {
						JSONObject json = mJsonArray.getJSONObject(i);
						// Log.e("json about",
						// json.getString(Constance.KEY_ABOUT_COLOR)+
						// json.getString(Constance.KEY_ABOUT_PATH_IMAGE)+json.getString(Constance.KEY_ABOUT_COMPANY_NAME));
						urlImagePlace = json.getString(Constance.PATH_IMG_PALCE_DETAIL);
						content = json
								.getString(Constance.CONTENT_PALCE_DETAIL);
						textTel = json.getString(Constance.TEL_PALCE_DETAIL);
						textWeb = json.getString(Constance.WEB_PALCE_DETAIL);
						text1 = json.getString("c_add_text_1");
						text2 = json.getString("c_add_text_2");
					}
				} catch (Exception e) {
					e.printStackTrace();
					// bCheck = true;
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (pDialog != null) {
				pDialog.dismiss();
			}
			imageMap.setVisibility(View.VISIBLE);
			runOnUiThread(new Runnable() {
				public void run() {
					// some code #3 (Write your code here to run in UI thread)
					//imgMain.setBackgroundResource(R.drawable.image_main_index);
//					textContent.setText(Html.fromHtml(content));
					mWebViewDetail.loadDataWithBaseURL(null, content,"text/html", "UTF-8",null);
					strTitle.setText(titleItem);
					linearBannerPlaceDetail.setBackgroundColor(Color
							.parseColor(colorCode));
					strAdd.setText(text1);
					strPhone.setText("TEL:" + textTel);
					strUrl.setText(textWeb);
				}
			});

		}

		@Override
		protected void onPreExecute() {

			pDialog.setMessage("loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

	}
}