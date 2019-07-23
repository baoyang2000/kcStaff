package com.ctrl.android.kcetong.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.House;
import com.ctrl.android.kcetong.toolkit.util.S;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 只有一个textview列表的 adapter
 * Created by Eric on 2015/10/20
 */
public class ListCommunityAdapter extends BaseAdapter {

    private Activity        mActivity;
    private List<House> list;

    public ListCommunityAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setList(List<House> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_item2, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final House item = list.get(position);

        holder.itemName.setText(S.getStr(item.getCommunityName()));

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_name)
        TextView itemName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
