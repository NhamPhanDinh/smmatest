package jp.ne.smma.aboutsmma.dialog;

import jp.ne.smma.R;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingDialog extends Dialog implements
		android.view.View.OnClickListener {
	public Activity c;
	private Button btnCancel;
	private Button btnOk;

	/**
	 * Constructor class
	 * 
	 * @param a
	 *            -Current activity
	 */
	public SettingDialog(Activity a) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = a;
	}

	/**
	 * Event click dialog
	 */
	@Override
	public void onClick(View v) {
		if (v==btnOk) {
			dismiss();
		}else if(v==btnCancel){
			dismiss();
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_dialog);
		//get data from xml
		btnCancel=(Button)findViewById(R.id.btnCancel);
		btnOk=(Button)findViewById(R.id.btnOK);
	}
}
