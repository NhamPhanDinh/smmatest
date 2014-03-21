package jp.ne.smma.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Receiver notification
 * 
 */
public class ReceiverNotification extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.e("", "Running receiver");
		Intent service = new Intent(context, AlarmServiceNotification.class);
		context.startService(service);
	}

}
