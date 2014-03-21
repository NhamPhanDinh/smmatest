package jp.ne.smma.EventList;

import jp.ne.smma.R;
import jp.ne.smma.Ultis.Constance;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Place detail activity
 */
public class PlaceDetailActivity extends Activity implements OnClickListener {
	private ImageView imgMain;
	private TextView strTitle;
	private TextView strAdd;
	private TextView strPhone;
	private TextView strUrl;
	private LinearLayout linearBannerPlaceDetail;

	private Intent intent;

	private int index = 0;
	private String colorCode;
	private String titleItem;
	ProgressDialog pDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.place_detail_activity);
		// get data from xml
		linearBannerPlaceDetail = (LinearLayout) findViewById(R.id.linearBannerPlaceDetail);
		imgMain = (ImageView) findViewById(R.id.imgContent);
		strTitle = (TextView) findViewById(R.id.title);
		strAdd = (TextView) findViewById(R.id.addPlace);
		strPhone = (TextView) findViewById(R.id.telPlace);
		strUrl = (TextView) findViewById(R.id.urlPlace);
		// get data from
		intent = getIntent();
		index = intent.getIntExtra(Constance.COLOR_INDEX_ABOUT, 0);
		colorCode = intent.getStringExtra(Constance.COLOR_ITEM_ABOUT);
		titleItem = intent.getStringExtra(Constance.COLOR_TEXT_INDEX_ABOUT);
		// set data
//		strTitle.setText(titleItem);
//		linearBannerPlaceDetail.setBackgroundColor(Color.parseColor(colorCode));
//		strAdd.setText("〒982-0815");
//		strPhone.setText("TEL : 022-307-5665");
//		strUrl.setText("仙台市太白区山田上ノ台町10-1 ");
		pDialog = new ProgressDialog(PlaceDetailActivity.this);
		new loadImage().execute("");

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	public void showMap(View v) {
		try {
			double latitude = 40.714728;
			double longitude = -73.998672;
			String label = "ABC Label";
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

	public void backIcon(View v) {
		finish();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (pDialog!=null) {
    		pDialog.dismiss();
		}
	}

	private class loadImage extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
        	 runOnUiThread(new Runnable() {
                 public void run() {
                     // some code #3 (Write your code here to run in UI thread)
                	 imgMain.setBackgroundResource(R.drawable.image_main_index);
                 	strTitle.setText(titleItem);
             		linearBannerPlaceDetail.setBackgroundColor(Color.parseColor(colorCode));
             		strAdd.setText("〒982-0815");
             		strPhone.setText("TEL : 022-307-5665");
             		strUrl.setText("仙台市太白区山田上ノ台町10-1 ");
                 }
             });
        	
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        	if (pDialog!=null) {
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
}