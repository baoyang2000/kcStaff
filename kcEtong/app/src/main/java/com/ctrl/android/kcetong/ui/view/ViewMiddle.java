package com.ctrl.android.kcetong.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.ui.adapter.TextAdapter;

import java.util.ArrayList;
import java.util.LinkedList;

public class ViewMiddle extends LinearLayout implements ViewBaseAction {

	private Context  context;
	private ListView regionListView;
	private ListView plateListView;
	private ArrayList<String>               groups       = new ArrayList<>();
	private LinkedList<String>              childrenItem = new LinkedList<>();
	private SparseArray<LinkedList<String>> children     = new SparseArray<>();
	private TextAdapter      plateListViewAdapter;
	private TextAdapter      earaListViewAdapter;
	private OnSelectListener mOnSelectListener;
	private int    tEaraPosition  = 0;
	private int    tBlockPosition = 0;
	private String showString     = "不限";
	private int pos;

	public ViewMiddle(Context context) {
		super(context);
		this.context=context;
		init(context);
	}

	public ViewMiddle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void setData(ArrayList<String> list,SparseArray<LinkedList<String>> list2){
		this.groups=list;
		this.children=list2;
		earaListViewAdapter = new TextAdapter(context, groups,
				R.drawable.choose_item_selecte,
				R.drawable.choose_eara_item_selector);
		earaListViewAdapter.setTextSize(17);
		earaListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);
		regionListView.setAdapter(earaListViewAdapter);
		earaListViewAdapter
				.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, int position) {
						pos=position;
						if (position < children.size()) {
							plateListView.setVisibility(VISIBLE);
							childrenItem.clear();
							childrenItem.addAll(children.get(position));
							plateListViewAdapter.notifyDataSetChanged();
						}
					}
				});
		if (tEaraPosition < children.size())
			childrenItem.clear();
			childrenItem.addAll(children.get(tEaraPosition));

		plateListViewAdapter = new TextAdapter(context, childrenItem,
				R.drawable.choose_item_selecte,
				R.drawable.choose_plate_item_selector);
		plateListViewAdapter.setTextSize(15);
		plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
		plateListView.setAdapter(plateListViewAdapter);
		plateListViewAdapter
				.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, final int position) {

						showString = childrenItem.get(position);
						if (mOnSelectListener != null) {
							mOnSelectListener.getValue(showString,position);
						}
					}
				});
		//init(context);
	}

	/*public void setGroups(ArrayList<String>list){
		groups=list;
		//init(context);
	}
	public void setChildren(SparseArray<LinkedList<String>>list){
		children=list;
		init(context);
	}*/


	public void updateShowText(String showArea, String showBlock) {
		if (showArea == null || showBlock == null) {
			return;
		}
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).equals(showArea)) {
				earaListViewAdapter.setSelectedPosition(i);
				childrenItem.clear();
				if (i < children.size()) {
					childrenItem.addAll(children.get(i));
				}
				tEaraPosition = i;
				break;
			}
		}
		for (int j = 0; j < childrenItem.size(); j++) {
			if (childrenItem.get(j).replace("不限", "").equals(showBlock.trim())) {
				plateListViewAdapter.setSelectedPosition(j);
				tBlockPosition = j;
				break;
			}
		}
		setDefaultSelect();
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_region, this, true);
		regionListView = (ListView) findViewById(R.id.listView);
		plateListView = (ListView) findViewById(R.id.listView2);
		//setBackgroundDrawable(getResources().getDrawable(
			//	R.drawable.choosearea_bg_left));

		/*for(int i=0;i<10;i++){
			groups.add(i+"行");
			LinkedList<String> tItem = new LinkedList<String>();
			for(int j=0;j<15;j++){
				tItem.add(i+"行"+j+"列");
			}
			children.put(i, tItem);
		}*/

		/*earaListViewAdapter = new TextAdapter(context, groups,
				R.drawable.choose_item_selecte,
				R.drawable.choose_eara_item_selector);
		earaListViewAdapter.setTextSize(17);
		earaListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);
		regionListView.setAdapter(earaListViewAdapter);
		earaListViewAdapter
				.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, int position) {
						if (position < children.size()) {
							plateListView.setVisibility(VISIBLE);
							childrenItem.clear();
							childrenItem.addAll(children.get(position));
							plateListViewAdapter.notifyDataSetChanged();
						}
					}
				});
		if (tEaraPosition < children.size())
			childrenItem.addAll(children.get(tEaraPosition));
		plateListViewAdapter = new TextAdapter(context, childrenItem,
				R.drawable.choose_item_selecte,
				R.drawable.choose_plate_item_selector);
		plateListViewAdapter.setTextSize(15);
		plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
		plateListView.setAdapter(plateListViewAdapter);*/

		if (tBlockPosition < childrenItem.size())
			showString = childrenItem.get(tBlockPosition);
		if (showString.contains("不限")) {
			showString = showString.replace("不限", "");
		}
		setDefaultSelect();

	}



	public void setDefaultSelect() {
		regionListView.setSelection(tEaraPosition);
		plateListView.setSelection(tBlockPosition);
	}
	public int getPos() {
		return pos;
	}



	public String getShowText() {
		return showString;
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		public void getValue(String showText, int pos);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}
}
