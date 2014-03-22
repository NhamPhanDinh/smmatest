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
			String colorCode, String name, String id, double latutide, double longitude) {
		Intent intent = new Intent(context, cls);
		intent.putExtra(Constance.COLOR_ITEM_ABOUT, colorCode);
		intent.putExtra(Constance.COLOR_TEXT_INDEX_ABOUT, name);
		intent.putExtra(Constance.KEY_ABOUT_PLACE, id);
		intent.putExtra(Constance.LATITUDE_ABOUT, latutide);
		intent.putExtra(Constance.LONGITUDE_ABOUT, longitude);
		context.startActivity(intent);
	}
}
