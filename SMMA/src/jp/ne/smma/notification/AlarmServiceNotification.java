package jp.ne.smma.notification;

import jp.ne.smma.R;
import jp.ne.smma.EventList.ProductActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
/**
 * AlarmService Notification
 */
public class AlarmServiceNotification extends Service {

	private NotificationManager mManager;
	String idEvent;
	String name;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("", "Running create service");
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public void onStart(Intent intent, int startId) {
		Log.e("", "Running start service");
		super.onStart(intent, startId);
		//get data
		if (intent!=null) {
			idEvent=intent.getStringExtra("itemId");
			name=intent.getStringExtra("name");
		}
		
		if (idEvent!=null && name!=null) {
			Log.e("Alarm", "idEvent: "+idEvent);
			Log.e("Alarm", "name: "+name);
		
		
		//send intent

		mManager = (NotificationManager) this.getApplicationContext()
				.getSystemService(
						this.getApplicationContext().NOTIFICATION_SERVICE);
		Intent intent1 = new Intent(this.getApplicationContext(),
				ProductActivity.class); //mainactivity
		intent1.putExtra("itemId", idEvent);
		intent1.putExtra("name", name);

		Notification notification = new Notification(R.drawable.ic_launcher,
				getString(R.string.content_notification), System.currentTimeMillis());
		intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
				this.getApplicationContext(), 0, intent1,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(this.getApplicationContext(),
				getString(R.string.app_name),getString(R.string.content_notification),
				pendingNotificationIntent);

		mManager.notify(0, notification);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
