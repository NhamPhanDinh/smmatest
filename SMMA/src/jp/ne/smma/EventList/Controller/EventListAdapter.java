package jp.ne.smma.EventList.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import jp.ne.smma.R;
import jp.ne.smma.EventList.EventListFragment;
import jp.ne.smma.Ultis.ImageLoader;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventListAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;

	public EventListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.event_list_item, null);
		TextView name = (TextView) vi.findViewById(R.id.text_name);
		TextView address = (TextView) vi.findViewById(R.id.text_address);
		TextView day = (TextView) vi.findViewById(R.id.text_day);
		ImageView image = (ImageView) vi.findViewById(R.id.list_image);
		LinearLayout event_thumb_bg = (LinearLayout) vi
				.findViewById(R.id.event_thumbnail);
		ImageView event_arrow_img = (ImageView) vi
				.findViewById(R.id.event_arrow_btn);
		HashMap<String, String> event = new HashMap<String, String>();
		event = data.get(position);

		name.setText(event.get(EventListFragment.KEY_NAME));
		address.setText(event.get(EventListFragment.KEY_ADDRESS));
		day.setText(event.get(EventListFragment.KEY_DAY));
		event_thumb_bg.setBackgroundColor(Color.parseColor(event.get(EventListFragment.KEY_IMG_COLOR)));

		/*LayerDrawable bgDrawable = (LayerDrawable) event_arrow_img
				.getBackground();
		final GradientDrawable shape = (GradientDrawable) bgDrawable
				.findDrawableByLayerId(R.id.arrow_shape);
		shape.setColor(Color.parseColor("#9ACD32"));*/

		// GradientDrawable backgroundGradient =
		// (GradientDrawable)event_arrow_img.getBackground();
		// backgroundGradient.setColor(Color.parseColor("#9ACD32"));

		event_arrow_img.setBackgroundColor(Color.parseColor(event.get(EventListFragment.KEY_IMG_COLOR)));
		imageLoader.DisplayImage(event.get(EventListFragment.KEY_IMG_URL),
				image);
		Log.d("key img url", EventListFragment.KEY_IMG_URL);

		// Setting all values in listview

		return vi;
	}

}
