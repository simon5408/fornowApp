/**
 * 
 */
package com.fornow.app.ui.customdialog;

import com.fornow.app.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Simon Lv
 * 
 */
public class CommonMsgDialog extends Dialog {
	private TextView dialogTitle, dialogMsg;
	private Button dialogCancel, dialogConfirm;

	public CommonMsgDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		setDialogView();
	}

	private void setDialogView() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.common_msg_dialog, null);
		super.setContentView(view);
		dialogTitle = (TextView) findViewById(R.id.dialog_title);
		dialogMsg = (TextView) findViewById(R.id.dialog_msg);
		dialogCancel = (Button) findViewById(R.id.dialog_cancel);
		dialogConfirm = (Button) findViewById(R.id.dialog_confirm);
		
	}

	public void setDialogTitle(String title) {
		dialogTitle.setText(title);
	}

	public void setDialogMsg(String msg) {
		dialogMsg.setText(msg);
	}

	public void setOnCancelBtnListener(View.OnClickListener listener) {
		dialogCancel.setOnClickListener(listener);
	}

	public void setOnConfirmBtnListener(View.OnClickListener listener) {
		dialogConfirm.setOnClickListener(listener);
	}

}
