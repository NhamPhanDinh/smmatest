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
	String idEvent;
	String name;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.e("", "Running receiver");
		//get intent
		idEvent=intent.getStringExtra("itemId");
		name=intent.getStringExtra("name");
		Log.i("ReceverNotification", "idEvent: "+idEvent);
		Log.i("ReceverNotification", "name: "+name);
		//send service
		Intent service = new Intent(context, AlarmServiceNotification.class);
		service.putExtra("itemId", idEvent);
		service.putExtra("name", name);
		context.startService(service);
	}

}
