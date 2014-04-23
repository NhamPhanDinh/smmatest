package jp.ne.smma.aboutsmma.dialog;

import java.util.ArrayList;
import java.util.List;

import jp.ne.smma.R;
import jp.ne.smma.Ultis.Constance;
import jp.ne.smma.aboutsmma.DTO.ItemCalendar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;

/**
 * Dialog fillter calendar
 */
public abstract class DialogFillterCalendar {
	private String[] province = new String[] { "すべて選択　／　すべて解除", "スリーエム仙台市科学館",
			"仙台市天文台", "仙台市歴史民俗資料館", "仙台市八木山動物公園", "せんだいメディアテーク", "宮城県美術館",
			"仙台市縄文の森広場", "仙台文学館", "仙台市博物館", "東北大学総合学術博物館",
			"地底の森ミュージアム仙台市富沢遺跡保存館", "東北福祉大学芹沢銈介美術工芸館" };
	private Integer[] idCompany = new Integer[] { 1, 8, 17, 18, 9, 11, 19, 12,
			10, 16, 15, 13, 14 };
	private ListView listview;
	private Context context;
	private ArrayList<Integer> idCompanyComplete = new ArrayList<Integer>();

	private List<ItemCalendar> rowCalendar;
	// value list
	private boolean[] dialogValueList = new boolean[idCompany.length];
	/** A list of current values. */
	private boolean[] valueList = { true, true, true, true, true, true, true,
			true, true, true, true, true, true };

	/**
	 * Constructor class
	 * 
	 * @param context
	 *            - Context Activity
	 */
	public DialogFillterCalendar(Context context, List<ItemCalendar> rowCalendar) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.rowCalendar = rowCalendar;
		// get data from xml
		// dialogValueList=valueList.clone();
		showMultiChoiceItems();
	}

	@SuppressWarnings("unused")
	private void loadSavedPreferences() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean checkBoxValue = sharedPreferences.getBoolean("CheckBox_Value",
				false);
		// String name = sharedPreferences.getString("storedName", "YourName");
		// if (checkBoxValue) {
		// checkBox.setChecked(true);
		// } else {
		// checkBox.setChecked(false);
		// }
		//
		// editText.setText(name);
	}

	@SuppressWarnings("unused")
	private void savePreferences(String key, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * show MultiChoice Items
	 */
	private void showMultiChoiceItems() {
		// check have click

		AlertDialog builder = new AlertDialog.Builder(context)
				.setTitle("絞り込む表示選択")
				.setMultiChoiceItems(province, valueList,
				// .setMultiChoiceItems(province, dialogValueList,
						new OnMultiChoiceClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								// TODO Auto-generated method stub
								if (province[which].contains("すべて選択")) {
									AlertDialog d = (AlertDialog) dialog;
									ListView v = d.getListView();
									int i = 0;
									while (i < province.length) {
										v.setItemChecked(i, isChecked);
										valueList[i] = isChecked;
										i++;
									}
								}
								valueList[which] = isChecked;
							}
						})
				.setNegativeButton("完了", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Constance.idCompanyClick.clear();
						for (int i = 1; i < idCompany.length; i++) { // i=0
							if (listview.getCheckedItemPositions().get(i)) {
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
				}).setPositiveButton("キャンセル", null).create();

		// add listview
		listview = builder.getListView();
		// add view header
		// LayoutInflater inflater = (LayoutInflater) context.getSystemService(
		// Context.LAYOUT_INFLATER_SERVICE );
		// ViewGroup header =
		// (ViewGroup)inflater.inflate(R.layout.item_fillter_header, listview,
		// false);
		// listview.addHeaderView(header, null, false);

		builder.show();
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
