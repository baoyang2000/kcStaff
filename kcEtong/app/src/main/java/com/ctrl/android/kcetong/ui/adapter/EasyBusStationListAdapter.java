package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.BusStation;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.activity.StationWebActivity;
import com.ctrl.android.kcetong.ui.base.ListBaseAdapter;
import com.ctrl.android.kcetong.ui.base.SuperViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cxl on 2017/2/4.
 */

public class EasyBusStationListAdapter extends ListBaseAdapter<BusStation> {

    private Context context;
    public EasyBusStationListAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.easy_bus_station_list_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {

        final BusStation station          = mDataList.get(position);
        TextView         station_name     = holder.getView(R.id.station_name);
        TextView         station_distance = holder.getView(R.id.station_distance);
        TextView         station_address  = holder.getView(R.id.station_address);
        LinearLayout     item             = holder.getView(R.id.item);

        station_name.setText((position + 1) + "." + station.getStationName());
        station_distance.setText(station.getStationDistance() + "m");
        station_address.setText(station.getStationAddress());
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showShortToast(context, station.getStationName());
                /*Intent i1 = new Intent();
                i1.setData(Uri.parse("baidumap://map/marker?location=40.057406655722,116.2964407172&title=Marker&content=makeamarker&traffic=on"));
                context.startActivity(i1);*/
                // 判断后的逻辑：
                //已安装，打开程序，需传入参数包名："com.skype.android.verizon"
               if(isAvilible("com.baidu.BaiduMap")){
                   LLog.d("---------11111111111");
                   LatLng location = station.getLoction();
                   LLog.d("----------location="+location);
                   String location2 = location.latitude + "," + location.longitude;
                   LLog.d("----------location2="+location2);
                   String title    =(station.getStationName());
                   String content  =station.getStationDistance() + "m"+station.getStationAddress();
                   Intent i1       = new Intent();
                   i1.setData(Uri.parse("baidumap://map/marker?location=" + location2 + "&title=" + title + "&content=" + content + "&traffic=on"));
                   LLog.d("baidumap://map/marker?location=" + location2 + "&title=" + title + "&content=" + content + "&traffic=on");
                   context.startActivity(i1);
                }
                //未安装，跳转至market下载该程序
                else {
                   LLog.d("---------22222222222");
                    LatLng location = station.getLoction();
                    // http://api.map.baidu.com/marker?location=39.916979519873,116.41004950566&title=我的位置&content=百度奎科大厦&output=html
                    String location2 = location.latitude + "," + location.longitude;
                    String city = AppHolder.getInstance().getBdLocation().getCity();
                    Intent i1   = new Intent(context, StationWebActivity.class);
                       i1.putExtra("url", "http://api.map.baidu.com/place/search?query=公交&location=" + location2+"&radius=1000&region="+ city+"&output=html&src=幸福爱家");
                    context.startActivity(i1);
                }
            }
        });

    }
    /**
     * 检查手机上是否安装了指定的软件
     * @param packageName：应用包名
     * @return
     */
    private boolean isAvilible(String packageName){
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

}
