package jp.ne.smma.aboutsmma.adapter;

import java.util.List;

import jp.ne.smma.R;
import jp.ne.smma.EventList.EventListFragment;
import jp.ne.smma.Ultis.ImageLoader;
import jp.ne.smma.aboutsmma.DTO.RowAboutItem;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CustomListViewAdapter extends ArrayAdapter<RowAboutItem> {

	Context context;
	public ImageLoader imageLoader; 
	
	public CustomListViewAdapter(Context context, int resourceId,
			List<RowAboutItem> items) {
		super(context, resourceId, items);
		this.context = context;
		imageLoader=new ImageLoader(context);
	}

	/* private view holder class */
	public class ViewHolder {
		LinearLayout linearBacground;
		ImageView imageIndexItem;
		TextView txtContent;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowAboutItem rowItem = getItem(position);
 
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_list_about, null);
            holder = new ViewHolder();
            
            holder.linearBacground=(LinearLayout)convertView.findViewById(R.id.linearItemAbout);
            holder.txtContent = (TextView) convertView.findViewById(R.id.txtNameListAbout);
            holder.imageIndexItem = (ImageView) convertView.findViewById(R.id.imgListAbout);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        /* Set item */
        holder.linearBacground.setBackgroundColor(Color.parseColor(rowItem.getColorCode()));
        holder.txtContent.setText(rowItem.getStrContent());
        imageLoader.DisplayImage(rowItem.getPathImageItem(), holder.imageIndexItem);
        Log.e("test url image about",rowItem.getPathImageItem());
       // holder.imageIndexItem.setImageResource(rowItem.getImageItem());
 
        return convertView;
    }
}
