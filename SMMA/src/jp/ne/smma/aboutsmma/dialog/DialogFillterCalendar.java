package jp.ne.smma.aboutsmma.dialog;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

/**
 * Dialog fillter calendar
 */
public class DialogFillterCalendar extends Dialog {
	private String[] province = new String[] { "仙台文学館", "仙台市天文台", "仙台市歴史民俗資料館",
			"仙台市八木山動物公園", "せんだいメディアテーク", "宮城県美術館", "仙台市縄文の森広場", "仙台市科学館",
			"仙台市博物館", "東北大学総合学術博物館", "地底の森ミュージアム仙台市富沢遺跡保存館", "東北福祉大学芹沢銈介美術工芸館" };
	private Integer[] idCompany = new Integer[] { 10, 17, 18, 9, 11, 19, 12, 8,
			16, 15, 13, 14 };
	private ListView listview;
	private Context context;
	/**
	 * Constructor class
	 * 
	 * @param context
	 *            - Context Activity
	 */
	public DialogFillterCalendar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// get data from xml
		showMultiChoiceItems();
	}

	/**
	 * show MultiChoice Items
	 */
	 private void showMultiChoiceItems() {
	        AlertDialog builder = new AlertDialog.Builder(context)
	                .setTitle("絞り込む表示選択")
	                .setMultiChoiceItems(province,
	                        new boolean[] { false, false, false, false, false },
	                        new OnMultiChoiceClickListener() {

	                            @Override
	                            public void onClick(DialogInterface dialog,
	                                    int which, boolean isChecked) {
	                                // TODO Auto-generated method stub
	                            }
	                        })
	                .setPositiveButton("完了", new DialogInterface.OnClickListener() {

	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {

	                        String s = "Value：";
	                        for (int i = 0; i < province.length; i++) {
	                            if (listview.getCheckedItemPositions().get(i)) {
	                                s += i + ":" + listview.getAdapter().getItem(i) + " ";
	                            }
	                        }
	                        if (listview.getCheckedItemPositions().size() > 0) {
	                            new AlertDialog.Builder(context)
	                                    .setMessage(s).show();
	                            System.out.println(listview.getCheckedItemPositions()
	                                    .size());
	                        }

	                        else if (listview.getCheckedItemPositions().size() <= 0) {
	                            new AlertDialog.Builder(context)
	                                    .setMessage("您未选择任何省份").show();
	                        }
	                    }
	                }).setNegativeButton("キャンセル", null).create();
	        listview = builder.getListView();
	        builder.show();
	    }

}
