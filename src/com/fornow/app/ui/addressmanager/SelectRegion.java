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
public class SelectRegion extends Activity {
	private Context mContext;
	private ListView listView;
	private List<RegionData> regions;
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
				R.string.select_region_header));
		String data = getIntent().getExtras().getString("regions");
		regions = GsonTool.getGsonTool().fromJson(data,
				new TypeToken<List<RegionData>>() {
				}.getType());

	}

	@Override
	protected void onStart() {
		
		super.onStart();
		adapter = new ProvinceListAdapter(mContext, regions);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent(SelectRegion.this,
						SelectArea.class);
				if (regions.get(position).getSons().size() > 0) {
					String data = GsonTool.getGsonTool().toJson(
							regions.get(position).getSons());
					intent.putExtra("areas", data);
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
			SelectRegion.this.finish();
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
