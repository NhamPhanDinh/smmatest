package jp.ne.smma.Ultis;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class DialogUtil
{
	/**
	 * Toast for showing
	 */
	private static Toast currentToast = null;

	public DialogUtil()
	{
	}

	/**
	 * Method that show toast message
	 * 
	 * path of external storage directory
	 */
	public static void showToast(Context context, String text, int duration)
	{
		if (currentToast == null
				|| currentToast.getView().getWindowVisibility() != View.VISIBLE)
		{
			currentToast = Toast.makeText(context, text, duration);
			currentToast.setGravity(Gravity.TOP, 0, 0);
			currentToast.show();
		}
	}

	/**
	 * Method that show toast message
	 * 
	 */
	public static void showToast(Context context, String text, int duration,
			int gravity)
	{
		try
		{
			if (currentToast == null
					|| currentToast.getView().getWindowVisibility() != View.VISIBLE)
			{
				currentToast = Toast.makeText(context, text, duration);
				currentToast.setGravity(gravity, 0, 0);
				currentToast.show();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create and show a massage dialog
	 * 
	 * @param context
	 * @param title
	 * @param message
	 */
	public static void doMessageDialog(Context context, String title,
			String message)
	{
		doMessageDialog(context, title, message, "Ok", null);
	}

	/**
	 * Create and show a message dialog
	 * 
	 * @param context
	 *            Context of dialog
	 * @param title
	 *            Title of dialog
	 * @param message
	 *            Message of dialog
	 * @param buttonText
	 *            Positive button text
	 * @param onClick
	 *            OnClick positive button event
	 */
	public static void doMessageDialog(Context context, String title,
			String message, String buttonText, OnClickListener onClick)
	{

		AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title)
				.setMessage(message).setPositiveButton(buttonText, onClick)
				.setCancelable(false).create();
		dialog.show();
	}
	/**
	 * Create and show a message dialog
	 * 
	 * @param context
	 *            Context of dialog
	 * @param title
	 *            Title of dialog
	 * @param message
	 *            Message of dialog
	 * @param buttonText
	 *            Positive button text
	 */
	public static void createMessageDialog(Context context, String title,
			String message, String buttonText)
	{

		AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title)
				.setMessage(message).setPositiveButton(buttonText, new OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				})
				.setCancelable(false).create();
		dialog.show();
	}
	/**
	 * Create and show a message dialog
	 * 
	 * @param context
	 * @param sTitle
	 * @param sMessage
	 * @param sPositiveButtonText
	 *            Positive button text
	 * @param positiveButtonOnClick
	 *            OnClick positive button event
	 * @param sNegativeButtonText
	 *            Negative button text
	 * @param negativeButtonOnClick
	 *            OnClick negative button event
	 */
	public static void doMessageDialog(Context context, String sTitle,
			String sMessage, String sPositiveButtonText,
			OnClickListener positiveButtonOnClick, String sNegativeButtonText,
			OnClickListener negativeButtonOnClick)
	{
		// Thread contextThread = context.getMainLooper().getThread();
		// if (contextThread != Thread.currentThread())
		// return;

		AlertDialog dialog = new AlertDialog.Builder(context).setTitle(sTitle)
				.setMessage(sMessage)
				.setPositiveButton(sPositiveButtonText, positiveButtonOnClick)
				.setNegativeButton(sNegativeButtonText, negativeButtonOnClick)
				.setCancelable(false).create();
		
		dialog.show();
	}

}
