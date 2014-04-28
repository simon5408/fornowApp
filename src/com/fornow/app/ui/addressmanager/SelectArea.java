/**
 * 
 */
package com.fornow.app.ui.addressmanager;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.fornow.app.model.RegionData;
import com.fornow.app.utils.GsonTool;
import com.google.gson.reflect.TypeToken;
import com.fornow.app.R;

/**
 * @author Simon Lv
 * 
 */
public class SelectArea extends Activity {
	private Context mContext;
	private ListView listView;
	private List<RegionData> areas;
	private TextView headerTitle;
	private ProvinceListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_province);
		mContext = this.getApplicationContext();
		listView = (ListView) findViewById(R.id.provinceList);
		headerTitle = (TextView) findViewById(R.id.heade_title);
		headerTitle.setText(getResources().getString(
				R.string.select_xiaoqu_header));
		String data = getIntent().getExtras().getString("areas");
		areas = GsonTool.getGsonTool().fromJson(data,
				new TypeToken<List<RegionData>>() {
				}.getType());

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		adapter = new ProvinceListAdapter(mContext, areas);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.putExtra("areaId", areas.get(position).getId());
				setResult(Activity.RESULT_OK, it);
				SelectArea.this.finish();
			}

		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void softBack(View v) {
		this.finish();
	}

}
