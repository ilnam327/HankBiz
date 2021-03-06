package com.hank.rs.common;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.hank.R;

import java.util.List;

public class SpinnerPopWindow extends PopupWindow implements OnItemClickListener{

	private Context mContext;
	private ListView mListView;
	private GridView mGridView;
    private SpinnerAdapter mAdapter;
	private IOnItemSelectListener mItemSelectListener;

	public SpinnerPopWindow(Context context)
	{
		super(context);
		mContext = context;
		init();
	}

	private void init()
	{
		View view = LayoutInflater.from(mContext).inflate(R.layout.spiner_window_layout, null);
		setContentView(view);
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);

		setFocusable(true);
    	ColorDrawable dw = new ColorDrawable(0x00);
		setBackgroundDrawable(dw);

		//mListView = (ListView) view.findViewById(R.id.listview);
		mGridView= (GridView) view.findViewById(R.id.gridview);
		mAdapter = new SpinnerAdapter(mContext);
        //mAdapter=new SpinnerAdapter(mContext);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
	}


	public void refreshData(List<SpinnerItem> list, int selIndex)
	{
		if (list != null && selIndex  != -1){
			mAdapter.refreshData(list, selIndex);
		}
	}



	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
		dismiss();
		if (mItemSelectListener != null){
			mItemSelectListener.onItemClick(pos);
		}
	}

    public void setItemListener(IOnItemSelectListener listener){
        mItemSelectListener = listener;
    }




	public  interface IOnItemSelectListener{
        public void onItemClick(int pos);
    };


}
