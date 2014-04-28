/*****************************************************************************
*
 *                      FORNOW PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to ForNow
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from ForNow.
 *
 *            Copyright (c) 2014 by ForNow.  All rights reserved.
 *
 *****************************************************************************/
package com.fornow.app.ui.addressmanager;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fornow.app.R;
import com.fornow.app.controller.ControllerManager;
import com.fornow.app.model.RegionData;
import com.fornow.app.net.ViewListener;
import com.fornow.app.net.ViewUpdateObj;
import com.fornow.app.ui.LoadingAnim;
import com.fornow.app.utils.GsonTool;
import com.google.gson.reflect.TypeToken;

/**
 * @author Simon Lv 2014-3-20
 */
public class SelectProvince extends Activity {
//	private ExpandableListView elistview;
	private ProvinceListAdapter adapter;
	private Context mContext;
	private ListView listView;
	private TextView headerTitle;
	private List<RegionData> groups;
	public static final int GET_DATA_SUCCESS = 0x00, NET_ERROR = 0x03;
	private Dialog dialog;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
//			int pos = 0;
			switch (msg.what) {
			case GET_DATA_SUCCESS:
				dialog.dismiss();
				String data = msg.getData().getString("data");
				loadData(data);
				break;
			case NET_ERROR:
				dialog.dismiss();
				View view = getLayoutInflater()
						.inflate(R.layout.my_toast, null);
				TextView toastText = (TextView) view
						.findViewById(R.id.toast_text);
				toastText.setText(getResources().getString(
						R.string.str_net_error));
				Toast toast = new Toast(SelectProvince.this);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setDuration(Toast.LENGTH_SHORT);
				toast.setView(view);
				toast.show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_province);
		mContext = this.getApplicationContext();
		dialog = new LoadingAnim(SelectProvince.this, R.style.my_dialog);
		listView = (ListView) findViewById(R.id.provinceList);
		headerTitle = (TextView) findViewById(R.id.heade_title);
		headerTitle.setText(getResources().getString(
				R.string.select_province_header));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent(SelectProvince.this,
						SelectCities.class);
				if (groups.get(position).getSons().size() > 0) {
					String cities = GsonTool.getGsonTool().toJson(
							groups.get(position).getSons());
					intent.putExtra("cities", cities);
					startActivityForResult(intent, 0);
				}
			}

		});
		// elistview = (ExpandableListView) findViewById(R.id.provinceList);
		// elistview.setDivider(null);
		getRegions();
	}

	public void loadData(String data) {
		groups = GsonTool.getGsonTool().fromJson(data,
				new TypeToken<List<RegionData>>() {
				}.getType());
		adapter = new ProvinceListAdapter(mContext, groups);
		listView.setAdapter(adapter);

		// adapter = new ProvinceAdapter(mContext, groups);
		// elistview.setAdapter(adapter);
		// elistview.setOnGroupExpandListener(new OnGroupExpandListener() {
		//
		// @Override
		// public void onGroupExpand(int groupPosition) {
		// for (int i = 0; i < groups.size(); i++) {
		// if (groupPosition != i) {
		// elistview.collapseGroup(i);
		// }
		// }
		// }
		// });
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case Activity.RESULT_OK:
			setResult(Activity.RESULT_OK, data);
			SelectProvince.this.finish();
			break;
		default:
			break;
		}
	}

	public void getRegions() {
		ControllerManager.getInstance().getRegionController().unRegisterAll();
		ControllerManager.getInstance().getRegionController()
				.registerNotification(new ViewListener() {

					@Override
					public void updateView(ViewUpdateObj obj) {
						
						Message updateViewMsg;
						switch (obj.getCode()) {
						case 200:
							updateViewMsg = mHandler
									.obtainMessage(GET_DATA_SUCCESS);
							updateViewMsg.getData().putString("data",
									obj.getData());
							mHandler.sendMessage(updateViewMsg);
							break;
						default:
							updateViewMsg = mHandler.obtainMessage(NET_ERROR);
							mHandler.sendMessage(updateViewMsg);
							break;
						}
					}
				});
		ControllerManager.getInstance().getRegionController().getRegions();
	}

	@Override
	protected void onStart() {
		
		super.onStart();
	}

	@Override
	protected void onResume() {
		
		super.onResume();
	}

	public void softBack(View v) {
		this.finish();
	}
}
