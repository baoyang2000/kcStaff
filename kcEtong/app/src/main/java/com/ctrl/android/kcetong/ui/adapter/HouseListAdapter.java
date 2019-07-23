package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.House;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.ui.base.ListBaseAdapter;
import com.ctrl.android.kcetong.ui.base.SuperViewHolder;

/**
 * Created by cxl on 2017/1/16.
 */

public class HouseListAdapter extends ListBaseAdapter<House> {

    private Context           context;
    private HouseBindListener linstener;

    public HouseListAdapter(Context context, HouseBindListener linstener) {
        super(context);
        this.context = context;
        this.linstener = linstener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.house_list_item2;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {

        final House listBean           = mDataList.get(position);
        TextView    community_name     = holder.getView(R.id.community_name);
        TextView    buildind_unit_room = holder.getView(R.id.buildind_unit_room);
        CheckBox    default_checkbox   = holder.getView(R.id.default_checkbox);
        TextView    remove_house_bind2 = holder.getView(R.id.remove_house_bind2);
        ImageView   remove_house_bind  = holder.getView(R.id.remove_house_bind);
        if (listBean.getCommunityName() != null) {
            community_name.setText(S.getStr(listBean.getCommunityName()));
        }
        if (listBean.getBuilding() != null || listBean.getUnit() != null || listBean.getRoom() != null) {
            buildind_unit_room.setText(S.getStr(listBean.getBuilding()) + context.getResources().getString(R.string.floor) + S.getStr(listBean.getUnit()) + " 单元 " + S.getStr(listBean.getRoom()) + " 室");
        }
        //是否默认住址（0：非默认、1：默认）
        if (Integer.valueOf(listBean.getIsDefault()) == 1) {
            default_checkbox.setChecked(true);
        } else {
            default_checkbox.setChecked(false);
        }

        default_checkbox.setClickable(false);
        remove_house_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linstener.removeHouseBind(listBean, position);
            }
        });
        remove_house_bind2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linstener.removeHouseBind(listBean, position);
            }
        });
    }

    public interface HouseBindListener {
        public void removeHouseBind(House listBean, int position);
    }
}
