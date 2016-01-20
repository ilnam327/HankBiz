package com.hank.rs.common;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hank.R;

import java.util.ArrayList;
import java.util.List;

public  class SpinnerAdapter<T> extends BaseAdapter {

	public static interface IOnItemSelectListener{
		public void onItemClick(int pos);
	};

	 private Context mContext;   
	 private List<SpinnerItem> mObjects = new ArrayList();
	 private int mSelectItem = 0;
	    
	 private LayoutInflater mInflater;
	
	 public SpinnerAdapter(Context context){
		 init(context);
	 }
	 
	 public void refreshData(List<SpinnerItem> objects, int selIndex){
		 mObjects = objects;
		 if (selIndex < 0){
			 selIndex = 0;
		 }
		 if (selIndex >= mObjects.size()){
			 selIndex = mObjects.size() - 1;
		 }
		 
		 mSelectItem = selIndex;
	 }
	 
	 private void init(Context context) {
	        mContext = context;
	        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 }
	    
	    
	@Override
	public int getCount() {

		return mObjects.size();
	}

	@Override
	public Object getItem(int pos) {
		return mObjects.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		 ViewHolder viewHolder;
    	 
	     if (convertView == null) {
	    	 convertView = mInflater.inflate(R.layout.spinner_item, null);
	         viewHolder = new ViewHolder();
	         viewHolder.mTextView = (TextView) convertView.findViewById(R.id.spinner_content);
	         convertView.setTag(viewHolder);
	     } else {
	         viewHolder = (ViewHolder) convertView.getTag();
	     }


        SpinnerItem item =  (SpinnerItem)getItem(pos);

		 viewHolder.mTextView.setText(item.getValue());

	     return convertView;
	}

	public static class ViewHolder
	{
	    public TextView mTextView;
    }


}
