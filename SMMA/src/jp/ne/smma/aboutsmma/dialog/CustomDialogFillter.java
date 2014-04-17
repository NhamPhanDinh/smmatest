package jp.ne.smma.aboutsmma.dialog;

import java.util.ArrayList;
import java.util.List;

import jp.ne.smma.R;
import jp.ne.smma.Ultis.Constance;
import jp.ne.smma.aboutsmma.DTO.FillterItem;
import jp.ne.smma.aboutsmma.DTO.ItemCalendar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public abstract class CustomDialogFillter extends Dialog {

	private String[] province = new String[] { "すべて選択　／　すべて解除", "スリーエム仙台市科学館",
			"仙台市天文台", "仙台市歴史民俗資料館", "仙台市八木山動物公園", "せんだいメディアテーク", "宮城県美術館",
			"仙台市縄文の森広場", "仙台文学館", "仙台市博物館", "東北大学総合学術博物館",
			"地底の森ミュージアム仙台市富沢遺跡保存館", "東北福祉大学芹沢銈介美術工芸館" };
	private Integer[] idCompany = new Integer[] { 1, 10, 17, 18, 9, 11, 19, 12,
			8, 16, 15, 13, 14 };
	private Context context;
	private ArrayList<Integer> idCompanyComplete = new ArrayList<Integer>();

	private List<ItemCalendar> rowCalendar;
	// value list
	private boolean[] dialogValueList = new boolean[idCompany.length];
	/** A list of current values. */
	private boolean[] valueList = { true, true, true, true, true, true, true,
			true, true, true, true, true, true };
	MyCustomAdapter dataAdapter = null;

	Button btnCancel;
	Button btnOK;
	ListView listView;

	/**
	 * Constructor class
	 * 
	 * @param context
	 *            - Context Activity
	 */
	public CustomDialogFillter(Context context, List<ItemCalendar> rowCalendar) {
		super(context);
		this.context = context;
		this.rowCalendar = rowCalendar;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_fillter);

		setTitle("絞り込む表示選択");
		// button
		btnOK = (Button) findViewById(R.id.dialogOK);
		btnCancel = (Button) findViewById(R.id.dialogCancel);
		listView = (ListView) findViewById(R.id.list);
		// Generate list View from ArrayList
		displayListView();
		// set click button
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		btnOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Constance.idCompanyClick.clear();
				for (int i = 1; i < idCompany.length; i++) { // i=0
					if (listView.getCheckedItemPositions().get(i)) {
						// s += idCompany[i] + ":"
						// + listview.getAdapter().getItem(i)
						// + " ";
						idCompanyComplete.add(idCompany[i]);
						// add constance
						// Constance.idCompanyClick.add(idCompany[i]);

					}
				}
				OnTaskCompleted();
			}
		});
	}

	private void displayListView() {

		// Array list of countries
		ArrayList<FillterItem> fillterList = new ArrayList<FillterItem>();

		for (int i = 0; i < province.length; i++) {
			Log.i("", "province[i]: " + province[i]);
			FillterItem fillter = new FillterItem();
			fillter.setStrContent(province[i]);
			fillter.setSelected(valueList[i]);
			// add data
			fillterList.add(fillter);
		}

		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(getContext(),
				R.layout.item_fillter_header, fillterList);

		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

	}
//	private View.OnClickListener checkAllCheckboxes = new View.OnClickListener(){
//	    public void onClick(View v) {
//	        ListView lv = getListView();
//	        int size = getListAdapter().getCount();
//	        if(lv.isItemChecked(0)){
//	            for(int i = 0; i<=size; i++){
//	                lv.setItemChecked(i, false);
//	            }
//	        } else {
//	            for(int i = 0; i<=size; i++){
//	                lv.setItemChecked(i, true);
//	            }
//	        }
//	    }
//
//	};
	/**
	 * Custom adapter
	 *
	 */
	private class MyCustomAdapter extends ArrayAdapter<FillterItem> {

		private ArrayList<FillterItem> countryList;

		public MyCustomAdapter(Context context, int textViewResourceId,
				ArrayList<FillterItem> countryList) {
			super(context, textViewResourceId, countryList);
			this.countryList = new ArrayList<FillterItem>();
			this.countryList.addAll(countryList);
		}

		public class ViewHolder {
			TextView code;
			CheckBox name;
		}

		@Override
		public View getView( int position, View convertView, ViewGroup parent) {

			 ViewHolder holder = null;
			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.item_fillter_header, null);

				holder = new ViewHolder();
				holder.code = (TextView) convertView
						.findViewById(R.id.textView1);
				holder.name = (CheckBox) convertView
						.findViewById(R.id.chkChoose);
				convertView.setTag(holder);

				holder.name.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						FillterItem country = (FillterItem) cb.getTag();
						Log.e("", "Clicked on Checkbox: " + cb.getText()
								+ " is " + cb.isChecked());
						country.setSelected(cb.isChecked());
					}
				});
				/* Set event */
//				if (position == 0) {
//					holder.name
//							.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//								@Override
//								public void onCheckedChanged(
//										CompoundButton buttonView,
//										boolean isChecked) {
//									// TODO Auto-generated method stub
//									if (buttonView == holder.name) {
//										holder.name.setChecked(isChecked);
//										for (int i = 0; i < countryList.size(); i++) {
//											countryList.get(i).setSelected(
//													isChecked);
//										}
//										notifyDataSetChanged();
//									} else {
//										int position = (Integer) buttonView
//												.getTag();
//										if (isChecked) {
//											countryList.get(position)
//													.setSelected(true);
//										} else {
//											countryList.get(position)
//													.setSelected(false);
//											if (holder.name.isChecked()) {
//												holder.name.setChecked(false);
//												for (int i = 0; i < countryList
//														.size(); i++) {
//													countryList.get(i)
//															.setSelected(true);
//													countryList.get(position)
//															.setSelected(false);
//												}
//											}
//										}
//										notifyDataSetChanged();
//									}
//								}
//							});
//				}
				//end set 
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			/* Fill data */
			FillterItem country = countryList.get(position);
			holder.code.setText(country.getStrContent());
			// holder.name.setText(country.getStrContent());
			holder.name.setChecked(country.isSelected());
			holder.name.setTag(country);

			return convertView;

		}

	}

	/**
	 * Return value
	 * 
	 * @return ArrayList<Integer>
	 */
	public ArrayList<Integer> GetIDCompanyComplete() {
		return idCompanyComplete;

	}

	// filt event 27/3/2014
	public void filtCompanyId(ArrayList<Integer> idCompanyComplete) {
		for (int i = 0; i < this.rowCalendar.size(); i++) {
			this.rowCalendar.get(i).setChosen(true);
		}
		for (int i = 0; i < this.rowCalendar.size(); i++) {
			for (int j = 0; j < idCompanyComplete.size(); j++) {
				if (this.rowCalendar.get(i).getId()
						.equals(idCompanyComplete.get(j).toString()))
					this.rowCalendar.get(i).setChosen(false);
			}
		}
	}

	/*
	 * Asbtract onTaskCompleted
	 */
	public abstract void OnTaskCompleted();
}
