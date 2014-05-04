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
public class SelectCitiesActivity extends Activity {
	private Context mContext;
	private ListView listView;
	private List<RegionData> cities;
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
				R.string.select_city_header));
		String data = getIntent().getExtras().getString("cities");
		cities = JSONHelper.fromJson(data,
				new TypeToken<List<RegionData>>() {
				});

	}

	@Override
	protected void onStart() {
		
		super.onStart();
		adapter = new ProvinceListAdapter(mContext, cities);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent(SelectCitiesActivity.this,
						SelectRegionActivity.class);
				if (cities.get(position).getSons().size() > 0) {
					String regions = JSONHelper.toJson(
							cities.get(position).getSons());
					intent.putExtra("regions", regions);
					startActivityForResult(intent, 0);
				}
			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case Activity.RESULT_OK:
			setResult(Activity.RESULT_OK, data);
			SelectCitiesActivity.this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		
		super.onResume();
	}

	public void softBack(View v) {
		this.finish();
	}
}
