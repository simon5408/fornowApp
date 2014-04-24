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
package com.fornow.app.ui.mine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fornow.app.R;
import com.fornow.app.model.OrderList;
import com.fornow.app.ui.MyListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class OrderAdapter extends BaseAdapter {
	private List<OrderList> mOrderList;
	private Context mContext;
	private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public OrderAdapter(List<OrderList> orderList, Context context) {
		this.mOrderList = orderList;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return mOrderList.size();
	}

	@Override
	public OrderList getItem(int position) {
		return mOrderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final ViewHolder holder;
		if (rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			rowView = inflater.inflate(R.layout.order_item, null);
			holder = new ViewHolder(rowView);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		if (getItem(position).getOrder_id() != null) {
			holder.getOrderId().setText(
					mContext.getResources().getString(R.string.str_dingdanhao)
							+ getItem(position).getOrder_id());
		}

		if (getItem(position).getDeal_date() != null) {
			String dateAfterFormat = format.format(new Date(Long.parseLong(getItem(position)
					.getDeal_date())));
			holder.getOrderUpdateDate().setText(dateAfterFormat);
		}
		if (getItem(position).getGoods_list().length > 0) {
			OrderGoodsAdapter itemAdapter = new OrderGoodsAdapter(getItem(
					position).getGoods_list(), mContext);
			holder.getGoodsList().setAdapter(itemAdapter);
		}

		if (getItem(position).getAddress() != null) {
			holder.getOrderShdz().setText(getItem(position).getAddress());
		}
		holder.getOrderStatus0().setVisibility(View.GONE);
		holder.getOrderStatus1().setVisibility(View.GONE);
		holder.getOrderStatus2().setVisibility(View.GONE);
		holder.getOrderStatus3().setVisibility(View.GONE);
		switch (getItem(position).getStatus()) {
		case 0:
			holder.getOrderStatus0().setVisibility(View.VISIBLE);
			break;
		case 1:
			holder.getOrderStatus0().setVisibility(View.VISIBLE);
			holder.getOrderStatus1().setVisibility(View.VISIBLE);
			break;
		case 2:
			holder.getOrderStatus0().setVisibility(View.VISIBLE);
			holder.getOrderStatus1().setVisibility(View.VISIBLE);
			holder.getOrderStatus2().setVisibility(View.VISIBLE);
			break;
		case 3:
			holder.getOrderStatus0().setVisibility(View.VISIBLE);
			holder.getOrderStatus1().setVisibility(View.VISIBLE);
			holder.getOrderStatus2().setVisibility(View.VISIBLE);
			holder.getOrderStatus3().setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		return rowView;
	}

	public class ViewHolder {
		private View baseView;
		private TextView orderId, orderUpdateDate, orderShdz;
		private MyListView goodsList;
		private ImageView orderStatus0, orderStatus1, orderStatus2,
				orderStatus3;

		public ViewHolder(View baseView) {
			this.baseView = baseView;
		}

		public TextView getOrderId() {
			if (orderId == null) {
				orderId = (TextView) baseView.findViewById(R.id.order_id);
			}
			return orderId;
		}

		public TextView getOrderUpdateDate() {
			if (orderUpdateDate == null) {
				orderUpdateDate = (TextView) baseView
						.findViewById(R.id.order_update_time);
			}
			return orderUpdateDate;
		}

		public TextView getOrderShdz() {
			if (orderShdz == null) {
				orderShdz = (TextView) baseView.findViewById(R.id.order_shdz);
			}
			return orderShdz;
		}

		public MyListView getGoodsList() {
			if (goodsList == null) {
				goodsList = (MyListView) baseView.findViewById(R.id.listView01);
			}
			return goodsList;
		}

		public ImageView getOrderStatus0() {
			if (orderStatus0 == null) {
				orderStatus0 = (ImageView) baseView
						.findViewById(R.id.order_status0);
			}
			return orderStatus0;
		}

		public ImageView getOrderStatus1() {
			if (orderStatus1 == null) {
				orderStatus1 = (ImageView) baseView
						.findViewById(R.id.order_status1);
			}
			return orderStatus1;
		}

		public ImageView getOrderStatus2() {
			if (orderStatus2 == null) {
				orderStatus2 = (ImageView) baseView
						.findViewById(R.id.order_status2);
			}
			return orderStatus2;
		}

		public ImageView getOrderStatus3() {
			if (orderStatus3 == null) {
				orderStatus3 = (ImageView) baseView
						.findViewById(R.id.order_status3);
			}
			return orderStatus3;
		}
	}
}
