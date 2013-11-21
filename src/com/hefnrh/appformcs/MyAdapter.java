package com.hefnrh.appformcs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	
	private String[] items;
	private Context parent;
	
	public MyAdapter(Context parent, String[] items) {
		this.parent = parent;
		this.items = items;
	}
	@Override
	public int getCount() {
		return items.length;
	}

	@Override
	public Object getItem(int paramInt) {
		return items[paramInt];
	}

	@Override
	public long getItemId(int paramInt) {
		return paramInt;
	}

	@Override
	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		LayoutInflater inflater = LayoutInflater.from(parent);
		paramView = inflater.inflate(R.layout.spinner_item, null);
		if (paramView != null) {
			TextView txt = (TextView) paramView.findViewById(R.id.itemtext);
			txt.setText(items[paramInt]);
		}
		return paramView;
	}

}
