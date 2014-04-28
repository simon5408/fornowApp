/**
 * 
 */
package com.fornow.app.ui.customdialog;

import com.fornow.app.R;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Simon Lv
 * 
 */
public class EditCountDialog extends Dialog {
	private TextView dialogTitle;
	private EditText countText;
	private Button countMinus, countPlus, dialogCancel, dialogConfirm;

	public EditCountDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		setEditDialogView();
	}

	private void setEditDialogView() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.edit_count, null);
		super.setContentView(view);
		dialogTitle = (TextView) view.findViewById(R.id.edit_count_title);
		countText = (EditText) view.findViewById(R.id.edit_count_text);
		countMinus = (Button) view.findViewById(R.id.edit_cout_minus);
		countPlus = (Button) view.findViewById(R.id.edit_cout_plus);
		dialogCancel = (Button) view.findViewById(R.id.edit_count_cancel);
		dialogConfirm = (Button) view.findViewById(R.id.edit_count_confirm);
		countText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		Spannable spanText = (Spannable) countText.getText();
		Selection.setSelection(spanText, countText.getText().length());
		countMinus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (getEditCount().length() != 0) {
					int count = Integer.parseInt(getEditCount().toString());
					if (count > 1) {
						count--;
						setEditCount(count + "");
						Spannable spanText = (Spannable) getEditCount();
						Selection.setSelection(spanText, getEditCount()
								.length());
					}
				}
			}
		});

		countPlus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				int count = Integer.parseInt(getEditCount().toString());
				count++;
				setEditCount(count + "");
				Spannable spanText = (Spannable) getEditCount();
				Selection.setSelection(spanText, getEditCount().length());
			}
		});
	}

	public Editable getEditCount() {
		return countText.getText();
	}

	public View getEditCountView() {
		return countText;
	}

	public void setEditCount(String count) {
		countText.setText(count);
	}

	public void setDialogTitle(String title) {
		dialogTitle.setText(title);
	}

	public void setOnEditCountMinusListener(View.OnClickListener listener) {
		countMinus.setOnClickListener(listener);
	}

	public void setOnEditCountPlusListener(View.OnClickListener listener) {
		countPlus.setOnClickListener(listener);
	}

	public void setOnCancelBtnListener(View.OnClickListener listener) {
		dialogCancel.setOnClickListener(listener);
	}

	public void setOnConfirmBtnListener(View.OnClickListener listener) {
		dialogConfirm.setOnClickListener(listener);
	}

}
