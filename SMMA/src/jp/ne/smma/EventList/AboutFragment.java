package jp.ne.smma.EventList;

import java.util.ArrayList;
import java.util.List;

import jp.ne.smma.R;
import jp.ne.smma.Ultis.ApplicationUntils;
import jp.ne.smma.aboutsmma.DTO.RowAboutItem;
import jp.ne.smma.aboutsmma.adapter.CustomListViewAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
/**
 * About item
 */
public class AboutFragment extends Fragment {
	/* Adapter content listview */
	 public static final String[] strContent = new String[] { "仙台文学館", "仙台市天文台",
	   "仙台市歴史民俗資料館", "仙台市八木山動物公園", "せんだいメディアテーク", "宮城県美術館", "仙台市縄文の森広場",
	   "仙台市科学館", "仙台市博物館", "SMMA", "東北大学総合学術博物館", "地底の森ミュージアム仙台市富沢遺跡保存館",
	   "東北福祉大学芹沢銈介美術工芸館" };

	 public static final String[] colorCode = new String[] { "#009944",
	   "#d72e8b", "#ea5504", "#000000", "#003f98", "#51318f", "#d7308c",
	   "#014098", "#9e774e", "#008cd6", "#9f774e", "#ea5504", "#51318f" };

	 public static final Integer[] imagesItem = { R.drawable.green_item,
	   R.drawable.pink_item, R.drawable.orange_item, R.drawable.black_item,
	   R.drawable.blue_item, R.drawable.blue_dark, R.drawable.pink_item,
	   R.drawable.blue_item, R.drawable.nau_item, R.drawable.blue_item,
	   R.drawable.nau_item, R.drawable.orange_item, R.drawable.blue_dark,
	   R.drawable.blue_item, R.drawable.blue_item, R.drawable.black_item };

	CustomListViewAdapter adapter;

	List<RowAboutItem> rowItems;

	/* End content listview */
	private ListView listview;
	private TextView txtContentTitle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.about_activity,
				container, false);
		// get data from XML
		listview = (ListView) rootView.findViewById(R.id.listview);
		txtContentTitle = (TextView)rootView. findViewById(R.id.txtContentAbout);
		// add data list view
		rowItems = new ArrayList<RowAboutItem>();
		for (int i = 0; i < strContent.length; i++) {
			RowAboutItem item = new RowAboutItem(colorCode[i], imagesItem[i],
					strContent[i]);
			rowItems.add(item);
		}

		adapter = new CustomListViewAdapter(getActivity(),
				R.layout.item_list_about, rowItems);
		listview.setAdapter(adapter);
		// set text content
		txtContentTitle
	    .setText("知的情報資源である仙台・宮城地域のさまざまな博物館が共働することで、地域にとってより有益な機能を獲得していくための共同事業体です。各館の学芸員や専門職員が持つ知識やノウハウを集積し、分野を横断した連携イベント、学校教育への協力や地域で活動する人材の育成支援、観光資源の開発など、単館では実現困難な新たな価値の創出を行い、地域のニーズに合った新時代のミュージアムとなることを目指します。");
		// set click listview
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				ApplicationUntils.gotoActivityPlaceDetail(getActivity(),
						PlaceDetailActivity.class, colorCode[pos], rowItems.get(pos)
								.getStrContent(), pos);
			}
		});
		
		return rootView;
	}
}
