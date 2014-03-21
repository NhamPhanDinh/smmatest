package jp.ne.smma.Ultis;

import android.content.Context;
import android.content.Intent;

/**
 * Class defind application until
 */
public class ApplicationUntils {
	/**
	 * Go to Place detail Activity
	 */
	public static void gotoActivityPlaceDetail(Context context, Class<?> cls,
			String colorCode, String itemTittle, int index) {
		Intent intent = new Intent(context, cls);
		intent.putExtra(Constance.COLOR_ITEM_ABOUT, colorCode);
		intent.putExtra(Constance.COLOR_TEXT_INDEX_ABOUT, itemTittle);
		intent.putExtra(Constance.COLOR_INDEX_ABOUT, index);
		context.startActivity(intent);
	}
}
