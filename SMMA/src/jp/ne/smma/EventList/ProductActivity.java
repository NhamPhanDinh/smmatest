package jp.ne.smma.EventList;

import java.util.ArrayList;
import java.util.List;

import jp.ne.smma.R;
import jp.ne.smma.EventList.Controller.AlertDialogManager;
import jp.ne.smma.Ultis.ApplicationUntils;
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
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
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
	private ImageView imgHeader;
	private ImageView imgDetail;
	private ImageView imgIcon;

	private Button imgAdd;
	// TextView
	private TextView textHeader;
	private TextView textName;
	private TextView textAddress;
	private TextView textPhone;
	// String
	private String URL = "http://nhampd.orgfree.com/get_list_product.php";
	static final String KEY_SUCCESS = "success";
	String pathDetail;
	String pathItem;
	String textOfName;
	String textOfAddress;
	String textOfPhone;

	// Button
	Button backButton;
	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	// Progress Dialog
	private ProgressDialog pDialog;
	ImageLoader imageLoaderProduct;
	ImageLoader imageLoaderIcon;
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
		imageLoaderProduct = new ImageLoader(
				ProductActivity.this.getApplicationContext());
		imageLoaderIcon = new ImageLoader(
				ProductActivity.this.getApplicationContext());
		imgDetail = (ImageView) findViewById(R.id.product_img_detail);
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
				ApplicationUntils.showDialogChooseDateEventDetail(
						ProductActivity.this, 2222, "Go to super market",
						"2014/03/12", "2014/04/16", "Test value", "Test note");
			}
		});
		// load image
		new loadImage().execute();

		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent setIntent = new
				// Intent(ProductActivity.this,MainActivity.class);
				//
				// startActivity(setIntent);
				// @Override
				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(i);
				finish();
				// super.onBackPressed();
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
	}

	// private class loadImageTask extends AsyncTask<Void, Void, Void> {
	// @Override
	// protected void onPreExecute() {
	// // Showing progress dialog before sending http request
	// pDialog = new ProgressDialog(ProductActivity.this);
	// pDialog.setMessage("Please wait..");
	// pDialog.setIndeterminate(true);
	// pDialog.setCancelable(false);
	// pDialog.show();
	// }
	//
	// @Override
	// protected Void doInBackground(Void... params) {
	//
	// return null;
	// }
	// }

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
			String fName = intent.getStringExtra("name");
			textHeader.setText(fName);
			String id = intent.getStringExtra("itemId");
			Log.e("item id", id);

			JSONParser jsonParser = new JSONParser();
			;
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
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
							JSONObject json = jsonData.getJSONObject(i);
							textOfName = json.getString("name");
							textOfAddress = json.getString("add");
							textOfPhone = json.getString("phone");
							pathDetail = json.getString("path_image_main");
							pathItem = json.getString("path_image_item");
							Log.e("path product", pathDetail);
							Log.e("path product item", pathItem);

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
						imageLoaderProduct.DisplayImage(pathDetail, imgDetail);

					} catch (Exception e) {
						// TơơODO: handle exception
					} finally {

						imageLoaderIcon.DisplayImage(pathItem, imgIcon);
						textName.setText(textOfName);
						textAddress.setText(textOfAddress);
						textPhone.setText(textOfPhone);
					}
				}
			});
			pDialog.dismiss();
		}
	}

}
