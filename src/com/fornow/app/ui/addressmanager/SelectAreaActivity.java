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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.fornow.app.R;
import com.fornow.app.model.RegionData;
import com.fornow.app.utils.JSONHelper;
import com.google.gson.reflect.TypeToken;

/**
 * @author Jiafa Lv
 * @email simon-jiafa@126.com
 * @date Apr 29, 2014 9:40:02 AM
 */
public class SelectAreaActivity extends Activity {
	private Context mContext;
	private ListView listView;
	private List<RegionData> areas;
	private TextView headerTitle;
	private ProvinceListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_province);
		mContext = this.getApplicationContext();
		listView = (ListView) findViewById(R.id.provinceList);
		headerTitle = (TextView) findViewById(R.id.heade_title);
		headerTitle.setText(getResources().getString(
				R.string.select_xiaoqu_header));
		String data = getIntent().getExtras().getString("areas");
		areas = JSONHelper.fromJson(data,
				new TypeToken<List<RegionData>>() {
				});

	}

	@Override
	protected void onStart() {
		
		super.onStart();
		adapter = new ProvinceListAdapter(mContext, areas);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent it = new Intent();
				it.putExtra("areaId", areas.get(position).getId());
				it.putExtra("areaName", areas.get(position).getName());
				setResult(Activity.RESULT_OK, it);
				SelectAreaActivity.this.finish();
			}

		});
	}

	@Override
	protected void onResume() {
		
		super.onResume();
	}

	public void softBack(View v) {
		this.finish();
	}

}
