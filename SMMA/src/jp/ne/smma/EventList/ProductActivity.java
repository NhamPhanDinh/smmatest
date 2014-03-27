package jp.ne.smma.EventList;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jp.ne.smma.R;
import jp.ne.smma.EventList.Controller.AlertDialogManager;
import jp.ne.smma.Ultis.ApplicationUntils;
import jp.ne.smma.Ultis.Constance;
import jp.ne.smma.Ultis.ImageLoader;
import jp.ne.smma.Ultis.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class ProductActivity extends Activity {
	// image view
	private ImageView imgIcon;

	private Button imgAdd;
	// TextView
	private TextView textHeader;
	private TextView textName;
	private TextView textAddress;
	private TextView textPhone;
	// String
	public static final String KEY_ID = "ev_id";
	public static final String KEY_CAT_ID = "ev_cat_id";
	public static final String KEY_COMPANY_NAME = "ev_company_name";
	public static final String KEY_NAME = "ev_name";
	public static final String KEY_DATE_FROM = "ev_date_from";
	public static final String KEY_DATE_END = "ev_date_end";
	public static final String KEY_ERROR = "error";
	public static final String KEY_CONTENT = "ev_content";
	public static final String KEY_TEXT_FIRST = "ev_text_first";
	public static final String KEY_TEL = "ev_tel";
	public static final String KEY_WEB = "ev_web";
	public static final String KEY_IMG_COLOR = "ev_color";
	public static final String KEY_IMG_URL = "ev_path_image";
	private String URL = "http://dev9.ominext.com/smma/?page_id=27649";
	static final String KEY_SUCCESS = "success";
	String pathDetail;
	String pathItem;
	String textOfName;
	String textOfAddress;
	String textOfPhone;
	String fName;

	// Button
	Button backButton;
	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	// Progress Dialog
	private ProgressDialog pDialog;
	ImageLoader imageLoaderProduct;
	ImageLoader imageLoaderIcon;

	TextView product_detail;
	TextView nameTv;
	TextView computerNameTv;
	TextView dateTv;
	HashMap<String, String> map;

	String strName = null;
	String strTitle = null;
	String strDate = null;
	int id = 0;
	String dateFrom = null;
	String dateEnd = null;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		if(Constance.checkPortrait){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		else{
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		imageLoaderProduct = new ImageLoader(
				ProductActivity.this.getApplicationContext());
		imageLoaderIcon = new ImageLoader(
				ProductActivity.this.getApplicationContext());
		imgIcon = (ImageView) findViewById(R.id.product_img_icon);
		textHeader = (TextView) findViewById(R.id.product_name_header);
		textName = (TextView) findViewById(R.id.product_text_name);
		textAddress = (TextView) findViewById(R.id.product_text_address);
		textPhone = (TextView) findViewById(R.id.product_text_phone);
		backButton = (Button) findViewById(R.id.product_btn_back);

		imgAdd = (Button) findViewById(R.id.imageButton1);
		// save db
		imgAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// convert date

				java.text.DateFormat df = new java.text.SimpleDateFormat(
						"yyyy年MM月dd日"); //old M
				java.text.DateFormat df1 = new java.text.SimpleDateFormat(
						"yyyy/MM/dd");
				df.setTimeZone(java.util.TimeZone.getDefault());
				try {
					java.util.Date dateE = df.parse(dateEnd);
					java.util.Date dateF = df.parse(dateFrom);
				
				// show notification
				ApplicationUntils.showDialogChooseDateEventDetail(
						ProductActivity.this, id, strName, df1.format(dateF),
						df1.format(dateE), strTitle, strDate);
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		// load image
		nameTv = (TextView) findViewById(R.id.product_name);
		computerNameTv = (TextView) findViewById(R.id.product_computer_name);
		dateTv = (TextView) findViewById(R.id.product_date);

		// Content example
		product_detail = (TextView) findViewById(R.id.product_detail_txt);

		new loadImage().execute();
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent setIntent = new
				// Intent(ProductActivity.this,MainActivity.class);
				//
				// startActivity(setIntent);
				// @Override
//				Intent i = new Intent(getApplicationContext(),
//						MainActivity.class);
//				startActivity(i);
//				finish();
				// super.onBackPressed();
				onBackPressed();
			}
		});

		textPhone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Starting a new async task
				String phone = textPhone.getText().toString();
				String phoneNumber = phone.replaceAll("TEL: ", "");
				phoneNumber = phoneNumber.replace(" ", "");
				Log.d("phone number", phoneNumber);
				Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri
						.parse("tel:" + phoneNumber));
				startActivity(callIntent);
			}
		});
		textAddress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Starting a new async task
				String url = textAddress.getText().toString();
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
	}

	/**
	 * Async Task that send a request to url Gets new list view data Appends to
	 * list view
	 * */
	private class loadImage extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(ProductActivity.this);
			pDialog.setMessage("Please wait..");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Intent intent = getIntent();
			fName = intent.getStringExtra("name");
			String id = intent.getStringExtra("itemId");
			Log.e("item id", id);

			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();

			params1.add(new BasicNameValuePair("name_event", "get-event-detail"));
			params1.add(new BasicNameValuePair("id", id));
			JSONObject mJson = jsonParser.getJSONFromUrl(URL, params1);
			try {
				if (mJson.getString(KEY_SUCCESS) != null) {
					// Toast.makeText(this, "success = 0",
					// Toast.LENGTH_LONG).show();
					String res = mJson.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						JSONArray jsonData = mJson.getJSONArray("data");
						for (int i = 0; i < jsonData.length(); i++) {

							map = new HashMap<String, String>();
							JSONObject json = jsonData.getJSONObject(i);
							map.put(KEY_ID, json.getString("ev_id"));
							map.put(KEY_CAT_ID, json.getString("ev_cat_id"));
							map.put(KEY_COMPANY_NAME,
									json.getString("ev_company_name"));
							map.put(KEY_NAME, json.getString("ev_name"));
							map.put(KEY_DATE_FROM,
									json.getString("ev_date_from"));
							map.put(KEY_DATE_END, json.getString("ev_date_end"));
							map.put(KEY_CONTENT, json.getString("ev_content"));
							map.put(KEY_IMG_COLOR, json.getString("ev_color"));
							map.put(KEY_TEXT_FIRST,
									json.getString("ev_text_first"));
							map.put(KEY_TEL, json.getString("ev_tel"));
							map.put(KEY_WEB, json.getString("ev_web"));
							map.put(KEY_IMG_URL,
									json.getString("ev_path_image"));

						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			// dismiss the dialog after getting all tracks
			// updating UI from Background Thread
			ProductActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					try {
						// imageLoaderProduct.DisplayImage(pathDetail,
						// imgDetail);
						textHeader.setText(fName);
						textHeader.setBackgroundColor(Color.parseColor(map
								.get(KEY_IMG_COLOR)));

						// UrlImageParser p = new UrlImageParser(product_detail,
						// getApplicationContext());
						// Spanned htmlSpan =
						// Html.fromHtml(map.get(KEY_CONTENT),
						// p, null);
						//
						// product_detail.setText(htmlSpan);
						product_detail.setText(Html.fromHtml(
								map.get(KEY_CONTENT), new ImageGetter() {

									@Override
									public Drawable getDrawable(String source) {
										// TODO Auto-generated method stub

										Drawable drawable = null;
										if (source.startsWith("http")) {
											// load from internet

											URL sourceURL;
											try {
												sourceURL = new URL(source);
												URLConnection urlConnection = sourceURL
														.openConnection();
												urlConnection.connect();
												InputStream inputStream = urlConnection
														.getInputStream();
												BufferedInputStream bufferedInputStream = new BufferedInputStream(
														inputStream);
												Bitmap bm = BitmapFactory
														.decodeStream(bufferedInputStream);

												// convert Bitmap to Drawable
												drawable = new BitmapDrawable(
														getResources(), bm);

												drawable.setBounds(0, 0,
														bm.getWidth(),
														bm.getHeight());

											} catch (MalformedURLException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											} catch (IOException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

										} else {
											// load from local drawable

											int dourceId = getApplicationContext()
													.getResources()
													.getIdentifier(source,
															"drawable",
															getPackageName());

											drawable = getApplicationContext()
													.getResources()
													.getDrawable(dourceId);

											drawable.setBounds(
													0,
													0,
													drawable.getIntrinsicWidth(),
													drawable.getIntrinsicHeight());
										}

										return drawable;

									}
								}, null));
						product_detail.setMovementMethod(LinkMovementMethod.getInstance());
					} catch (Exception e) {
						// TơơODO: handle exception
					} finally {
						nameTv.setText(map.get(KEY_NAME));
						computerNameTv.setText(map.get(KEY_COMPANY_NAME));
						dateTv.setText(map.get(KEY_DATE_FROM) + " ~ "
								+ map.get(KEY_DATE_END));
						imageLoaderIcon.DisplayImage(map.get(KEY_IMG_URL),
								imgIcon);
						textName.setText(map.get(KEY_TEXT_FIRST));
						textAddress.setText(map.get(KEY_WEB));
						textPhone.setText(map.get(KEY_TEL));
						// set text
						strName = map.get(KEY_COMPANY_NAME);
						strTitle = map.get(KEY_NAME);
						strDate = map.get(KEY_DATE_FROM) + " ~ "
								+ map.get(KEY_DATE_END);
						try {
							id = Integer.parseInt(map.get(KEY_ID));
						} catch (Exception e2) {
							// TODO: handle exception
							e2.printStackTrace();
						}

						dateFrom = map.get(KEY_DATE_FROM);
						dateEnd = map.get(KEY_DATE_END);
					}
				}
			});
			pDialog.dismiss();
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (pDialog != null) {
			pDialog.dismiss();
		}
	}
	
	@Override
    public void onBackPressed() {
        super.onBackPressed();   
        //    finish();

    }
}
