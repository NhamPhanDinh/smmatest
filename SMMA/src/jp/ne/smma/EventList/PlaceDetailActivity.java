package jp.ne.smma.EventList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.ne.smma.R;
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
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * Place detail activity
 */
public class PlaceDetailActivity extends Activity implements OnClickListener {
	private WebView imgMain;
	private ImageView mImageMain;
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
	ImageLoader mImgLoader;
	WebSettings mSettings;
	// flag for Internet connection status
	Boolean isInternetPresent = false;
	String LOGTAG = "show img";

	// Connection detector class
	ConnectionDetector cd;
	// private static final String HTML_FORMAT =
	// "<html><body style=\"text-align: center; background-color: black; vertical-align: center;\"><img src = \"%s\" /></body></html>";

	WindowManager mWinMgr;
	public static int displayWidth = 0, displayHeight = 0;

	ImageLoader imageLoader;
	ImageLoaderConfiguration config;
	DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.place_detail_activity);
		if (Constance.checkPortrait) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		// get data from xml
		linearBannerPlaceDetail = (LinearLayout) findViewById(R.id.linearBannerPlaceDetail);
		// imgMain = (ImageView) findViewById(R.id.imgContent);
		strTitle = (TextView) findViewById(R.id.title);
		strAdd = (TextView) findViewById(R.id.addPlace);
		strPhone = (TextView) findViewById(R.id.telPlace);
		strUrl = (TextView) findViewById(R.id.urlPlace);
		// imgMain = (WebView) findViewById(R.id.image_place_detail);
		// mImageMain = (ImageView) findViewById(R.id.image_place_detail);
		// mWinMgr = (WindowManager)
		// this.getSystemService(Context.WINDOW_SERVICE);
		// displayWidth = mWinMgr.getDefaultDisplay().getWidth();
		// displayHeight = mWinMgr.getDefaultDisplay().getHeight();
		// mSettings = imgMain.getSettings();
		// mSettings.setUseWideViewPort(true);
		// mSettings.setLoadWithOverviewMode(true);
		// mSettings.setBuiltInZoomControls(true);
		// imgMain.setBackgroundColor(Color.TRANSPARENT);
		// imgMain.getSettings().setUseWideViewPort(true);
		// textContent = (TextView)findViewById(R.id.text_content);
		mWebViewDetail = (WebView) findViewById(R.id.web_view_about);
		mWebViewDetail.setWebChromeClient(new WebChromeClient());
		mWebViewDetail.getSettings().setDefaultFontSize(14);
		mWebViewDetail.getSettings().setRenderPriority(RenderPriority.HIGH);
		mWebViewDetail.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

		imageMap = (ImageView) findViewById(R.id.imageMap);

		imageMap.setVisibility(View.GONE);

		// get data from
		intent = getIntent();
		titleItem = intent.getStringExtra(Constance.COLOR_TEXT_INDEX_ABOUT);
		colorCode = intent.getStringExtra(Constance.COLOR_ITEM_ABOUT);
		placeID = intent.getStringExtra(Constance.KEY_ABOUT_PLACE);
		Log.e("kkkkkkkkkkkkk", placeID);
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

		// Init config for Universal Loader Image
		imageLoader = ImageLoader.getInstance();
		File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());

		config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();

		imageLoader.init(config);

		options = new DisplayImageOptions.Builder()
				// .showImageOnLoading(R.drawable.ic_stub)
				// .showImageForEmptyUri(R.drawable.ic_empty)
				// .showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

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

	// public void clickCall(View v){
	// String phone = strPhone.getText().toString();
	// String phoneNumber = phone.replaceAll("TEL: ", "");
	// phoneNumber = phoneNumber.replace(" ", "");
	// Log.d("phone number", phoneNumber);
	// Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri
	// .parse("tel:" + phoneNumber));
	// startActivity(callIntent);
	// }

	// public void openURL(View v){
	// String url = strUrl.getText().toString();
	// Intent i = new Intent(Intent.ACTION_VIEW);
	// i.setData(Uri.parse(url));
	// startActivity(i);
	// }

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
						urlImagePlace = json
								.getString(Constance.PATH_IMG_PALCE_DETAIL);
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
			imageMap.setVisibility(View.VISIBLE);
			runOnUiThread(new Runnable() {
				public void run() {
					// some code #3 (Write your code here to run in UI thread)
					// imgMain.setBackgroundResource(R.drawable.image_main_index);
					// textContent.setText(Html.fromHtml(content));

					cd = new ConnectionDetector(
							PlaceDetailActivity.this.getApplicationContext());
					// list = (ListView)findViewById(R.id.list);

					isInternetPresent = cd.isConnectingToInternet();
					if (isInternetPresent) {
						if (Constance.bCheckNetworkTimeOut) {
							showAlertDialog(
									PlaceDetailActivity.this,
									"",
									"Can not get data from server, please check internet and try again",
									false);
						}
						mWebViewDetail.setBackgroundColor(0x00000000);
//						mWebViewDetail.setLayerType(
//								WebView.LAYER_TYPE_SOFTWARE, null);
						mWebViewDetail.loadDataWithBaseURL(null, content,
								"text/html", "UTF-8", null);

						// Use ImageLoader Universal to lazy load Image
						// imageLoader.displayImage(urlImagePlace, mImageMain,
						// options);
						// imageLoader.displayImage(urlImagePlace, mImageMain,
						// options, new ImageLoadingListener() {
						//
						// @Override
						// public void onLoadingStarted(String imageUri, View
						// view) {
						// // TODO Auto-generated method stub
						//
						// }
						//
						// @Override
						// public void onLoadingFailed(String imageUri, View
						// view,
						// FailReason failReason) {
						// // TODO Auto-generated method stub
						//
						// }
						//
						// @Override
						// public void onLoadingComplete(String imageUri, View
						// view, Bitmap loadedImage) {
						// // TODO Auto-generated method stub
						// //pDialog.dismiss();
						// Log.d("ProgressDialog", "Run in here.");
						// }
						//
						// @Override
						// public void onLoadingCancelled(String imageUri, View
						// view) {
						// // TODO Auto-generated method stub
						//
						// }
						// });

						// String img = "<img src=" + urlImagePlace + " "
						// + "width=" + "100%" + " " + "style="
						// + "margin: 0px 0px" + ">";
						// final String html = String.format(HTML_FORMAT,
						// urlImagePlace);

						// imgMain.loadDataWithBaseURL(null, img, "text/html",
						// "UTF-8", null);
						strTitle.setText(titleItem);
						linearBannerPlaceDetail.setBackgroundColor(Color
								.parseColor(colorCode));
						strAdd.setText(text1);
						strPhone.setText("TEL:" + textTel);
						strUrl.setText(textWeb);

					} else {
						showAlertDialog(
								PlaceDetailActivity.this,
								"No Internet Connection",
								"You don't have internet connection, please try again",
								false);
					}

				}
			});

			if (pDialog != null) {
				pDialog.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {

			pDialog.setMessage("loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

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
				finish();
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

	// notify webkit that our virtual view size changed size (after inv-zoom)
	// private void viewSizeChanged(int w, int h, int textwrapWidth,
	// float scale, int anchorX, int anchorY, boolean ignoreHeight) {
	// if (DebugFlags.WEB_VIEW_CORE) {
	// Log.v(LOGTAG, "viewSizeChanged w=" + w + "; h=" + h
	// + "; textwrapWidth=" + textwrapWidth + "; scale="
	// + scale);
	// }
	// if (w == 0) {
	// Log.w(LOGTAG, "skip viewSizeChanged as w is 0");
	// return;
	// }
	// int width = w;
	// if (mSettings.getUseWideViewPort()) {
	// if (mViewportWidth == -1) {
	// if (mSettings.getLayoutAlgorithm() == WebSettings.LayoutAlgorithm.NORMAL)
	// {
	// width = WebView.DEFAULT_VIEWPORT_WIDTH;
	// } else {
	// /*
	// * if a page's minimum preferred width is wider than the
	// * given "w", use it instead to get better layout result. If
	// * we start a page with MAX_ZOOM_WIDTH, "w" will be always
	// * wider. If we start a page with screen width, due to the
	// * delay between {@link #didFirstLayout} and
	// * {@link #viewSizeChanged},
	// * {@link #nativeGetContentMinPrefWidth} will return a more
	// * accurate value than initial 0 to result a better layout.
	// * In the worse case, the native width will be adjusted when
	// * next zoom or screen orientation change happens.
	// */
	// width = Math.min(WebView.sMaxViewportWidth, Math
	// .max(w, Math.max(
	// WebView.DEFAULT_VIEWPORT_WIDTH,
	// nativeGetContentMinPrefWidth())));
	// }
	// } else if (mViewportWidth > 0) {
	// width = Math.max(w, mViewportWidth);
	// } else {
	// width = textwrapWidth;
	// }
	//
	// }
	//
	// skip viewSizeChanged as w is 0
	//
	// WebView mWebView = (WebView) findViewById(R.id.webview);
	// mWebView.getSettings().setBuiltInZoomControls(true);

}