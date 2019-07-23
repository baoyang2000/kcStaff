package com.ctrl.android.kcetong.ui.news;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.activity.StationWebActivity;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.stx.xhb.xbanner.XBanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EasyServiceActivity extends BaseActivity {

    @BindView(R.id.banner_easy_service)
    XBanner      bannerEasyService;
    @BindView(R.id.bus)
    LinearLayout bus;
    @BindView(R.id.unlock_key)
    LinearLayout     unlockKey;
    @BindView(R.id.hydropower_maintenance)
    LinearLayout     hydropowerMaintenance;
    @BindView(R.id.moving_service)
    LinearLayout     movingService;
    @BindView(R.id.rush_pipe)
    LinearLayout     rushPipe;
    @BindView(R.id.waterproof_plugging)
    LinearLayout     waterproofPlugging;
    @BindView(R.id.errand_service)
    LinearLayout     errandService;
    @BindView(R.id.mother_to_child_service)
    LinearLayout     motherToChildService;
    @BindView(R.id.housekeeping)
    LinearLayout     housekeeping;
    @BindView(R.id.decoration)
    LinearLayout decoration;
    @BindView(R.id.decoration_price)
    LinearLayout decorationPrice;
    @BindView(R.id.formaldehyde_treatment)
    LinearLayout formaldehydeTreatment;
    @BindView(R.id.banjia)
        LinearLayout banjia;

    /**
     * 经度
     */
    private double longitude ;
    /**
     * 纬度
     */
    private double latitude;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_easy_service);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

//        UiUtil.clickToActivityWithArg(unlockKey, ConstantsData.ARG_KEY, EasyServiceActivity.this, EasyShopAroundListActivity.class);
//        UiUtil.clickToActivityWithArg(hydropowerMaintenance, ConstantsData.ARG_WATER, EasyServiceActivity.this, EasyShopAroundListActivity.class);
//        UiUtil.clickToActivityWithArg(movingService, ConstantsData.ARG_MOVING, EasyServiceActivity.this, EasyShopAroundListActivity.class);
//        UiUtil.clickToActivityWithArg(rushPipe, ConstantsData.ARG_RUSH_PIPE, EasyServiceActivity.this, EasyShopAroundListActivity.class);
//        UiUtil.clickToActivityWithArg(waterproofPlugging, ConstantsData.ARG_FANGSHUI, EasyServiceActivity.this, EasyShopAroundListActivity.class);
//        UiUtil.clickToActivityWithArg(errandService, ConstantsData.ARG_APT, EasyServiceActivity.this, EasyShopAroundListActivity.class);
//        UiUtil.clickToActivityWithArg(motherToChildService, ConstantsData.ARG_MOTHER, EasyServiceActivity.this, EasyShopAroundListActivity.class);
//        UiUtil.clickToActivityWithArg(housekeeping, ConstantsData.ARG_HOUSEKEEPING, EasyServiceActivity.this, EasyShopAroundListActivity.class);
//        UiUtil.clickToActivityWithArg(decoration, ConstantsData.ARG_decoration, EasyServiceActivity.this, EasyShopAroundListActivity.class);
////        UiUtil.clickToActivityWithArg(decorationPrice, ConstantsData.ARG_de, EasyServiceActivity.this, EasyShopAroundListActivity.class);
//        UiUtil.clickToActivityWithArg(formaldehydeTreatment, ConstantsData.ARG_formaldehyde_treatment,
//                EasyServiceActivity.this, EasyShopAroundListActivity.class);
//        UiUtil.clickToActivityWithArg(banjia, ConstantsData.ARG_BANJIA,
//                EasyServiceActivity.this, EasyShopAroundListActivity.class);

        longitude    = AppHolder.getInstance().getBdLocation().getLongitude();
        latitude     = AppHolder.getInstance().getBdLocation().getLatitude();
    }

    @OnClick({R.id.bus, R.id.unlock_key, R.id.hydropower_maintenance, R.id.rush_pipe,R.id.banjia,R.id.housekeeping, R.id.waterproof_plugging,R.id.decoration})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bus:
                if (Utils.isAvilible("com.baidu.BaiduMap" ,EasyServiceActivity.this)) {
                    Intent i1 = new Intent();
                    i1.setData(Uri.parse("baidumap://map/place/nearby?query=公交站"));
                    startActivity(i1);
                }
                //未安装，跳转至market下载该程序
                else {
                    LLog.d("---------22222222222");
                    String location2 = longitude + "," + latitude;
                    LLog.d(location2);
                    String city = AppHolder.getInstance().getBdLocation().getCity();
                    Intent i1   = new Intent(this, StationWebActivity.class);
                    i1.putExtra("url", "http://api.map.baidu.com/place/search?query=公交&location=" + location2 + "&radius=1000&region=" + city + "&output=html&src=幸福爱家");
                    startActivity(i1);
                }
            break;
            default:
                Utils.toastError(this, "暂未开通");
            break;
        }
    }
}
