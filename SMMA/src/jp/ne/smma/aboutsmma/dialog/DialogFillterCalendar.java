package jp.ne.smma.aboutsmma.dialog;

import java.util.ArrayList;
import java.util.List;

import jp.ne.smma.aboutsmma.DTO.ItemCalendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.widget.ListView;

/**
 * Dialog fillter calendar
 */
public abstract class DialogFillterCalendar {
	private String[] province = new String[] { "仙台文学館", "仙台市天文台", "仙台市歴史民俗資料館",
			"仙台市八木山動物公園", "せんだいメディアテーク", "宮城県美術館", "仙台市縄文の森広場", "仙台市科学館",
			"仙台市博物館", "東北大学総合学術博物館", "地底の森ミュージアム仙台市富沢遺跡保存館", "東北福祉大学芹沢銈介美術工芸館" };
	private Integer[] idCompany = new Integer[] { 10, 17, 18, 9, 11, 19, 12, 8,
			16, 15, 13, 14 };
	private ListView listview;
	private Context context;
	private ArrayList<Integer> idCompanyComplete = new ArrayList<Integer>();

	
	private List<ItemCalendar> rowCalendar;

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
		showMultiChoiceItems();
	}

	/**
	 * show MultiChoice Items
	 */
	private void showMultiChoiceItems() {
		AlertDialog builder = new AlertDialog.Builder(context)
				.setTitle("絞り込む表示選択")
				.setMultiChoiceItems(
						province,
						new boolean[] { false, false, false, false, false,
								false, false, false, false, false, false, false },
						new OnMultiChoiceClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								// TODO Auto-generated method stub
							}
						})
				.setNegativeButton("完了", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						for (int i = 0; i < idCompany.length; i++) {
							if (listview.getCheckedItemPositions().get(i)) {
								// s += idCompany[i] + ":"
								// + listview.getAdapter().getItem(i)
								// + " ";
								idCompanyComplete.add(idCompany[i]);
							}
						}
						OnTaskCompleted();
					}
				}).setPositiveButton("キャンセル", null).create();
		listview = builder.getListView();
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
