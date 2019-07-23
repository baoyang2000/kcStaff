package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.HouseEstateInfo;
import com.ctrl.android.kcetong.model.HouseEstateResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.D;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.stx.xhb.xbanner.XBanner;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HouseInfoDetailActivity extends BaseActivity implements XBanner.XBannerAdapter{
    @BindView(R.id.banner_1)
    XBanner   banner_1;
    @BindView(R.id.tv_title)
    TextView  tv_title;
    @BindView(R.id.tv_time)
    TextView  tv_time;
    @BindView(R.id.tv_total_price)
    TextView  tv_total_price;
    @BindView(R.id.tv_room)
    TextView  tv_room;
    @BindView(R.id.tv_area)
    TextView  tv_area;
    @BindView(R.id.tv_single_price)
    TextView  tv_single_price;
    @BindView(R.id.tv_chaoxiang)
    TextView  tv_chaoxiang;
    @BindView(R.id.tv_floor)
    TextView  tv_floor;
    @BindView(R.id.tv_location)
    TextView  tv_location;
    @BindView(R.id.tv_type)
    TextView  tv_type;
    @BindView(R.id.tv_zhuangxiu)
    TextView  tv_zhuangxiu;
    @BindView(R.id.tv_chanqiu)
    TextView  tv_chanqiu;
    @BindView(R.id.tv_detail)
    TextView  tv_detail;
    @BindView(R.id.tv_house_age)
    TextView  tv_house_age;
    @BindView(R.id.tv_contact_name)
    TextView  tv_contact_name;
    @BindView(R.id.tv_contact_tel)
    TextView  tv_contact_tel;
    @BindView(R.id.tv_pager_number)
    TextView  tv_pager_number;
    @BindView(R.id.iv_contact_tel)
    ImageView iv_contact_tel;

    private static final String TITLE = StrConstant.HOUSE_INFO_DETAIL;

    private List<String> imageUrls =new ArrayList<>();
    private Handler      mHandler  =new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 111:
                    tv_pager_number.setText(1+"/"+msg.arg1);
                    break;
                case 112:
                    tv_pager_number.setText(msg.arg1+"/"+msg.arg2);
                    break;
            }
        }
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_house_info_detail);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HouseInfoDetailActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        requestHouseEstate(getIntent().getStringExtra("id"), "", "", "", "", 0, "", "", "","");
    }

    @OnClick({R.id.iv_contact_tel})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_contact_tel:
                if(!TextUtils.isEmpty(tv_contact_tel.getText().toString().trim())){
                    Utils.dial(HouseInfoDetailActivity.this,tv_contact_tel.getText().toString().trim());
                }
                break;
        }
    }
    /**
     * 房产信息列表
     * @param id 房产信息的id
     * @param areaCode 3级的市区code值
     * @param communityId 社区的id
     * @param totalPriceMin 房屋价格总价的最小值(当条件为不限不用传参,默认为null)
     * @param totalPriceMax 房屋价格总价的最大值(当条件为不限不用传参,默认为null)
     * @param sittingRoom 房屋的室个数(当条件为不限不用传参,默认为0),当条件4室以上时:参数为5
     * @param roomAcreageMin 房屋的总面积最小值(当条件为不限不用传参,默认为null)
     * @param roomAcreageMax 房屋的总面积最大值(当条件为不限不用传参,默认为null)
     * @param currentPage 当前页码
     * @param provinceName 省code值
     * */
    public void requestHouseEstate(String id,
            String areaCode,
            String communityId,
            String totalPriceMin,
            String totalPriceMax,
            int sittingRoom,
            String roomAcreageMin,
            String roomAcreageMax,
            String currentPage,
            String provinceName){
        Map<String,String> map = new HashMap<>();
        map.put(ConstantsData.METHOD,"pm.ppt.homeEstate.list");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("id",id);
        map.put("areaCode",areaCode);
        map.put("communityId",communityId);
        map.put("totalPriceMin",totalPriceMin+"");
        map.put("totalPriceMax",totalPriceMax+"");
        map.put("sittingRoom",sittingRoom+"");
        map.put("roomAcreageMin",roomAcreageMin+"");
        map.put("roomAcreageMax",roomAcreageMax+"");
        map.put("currentPage",currentPage);
        map.put("rowCountPerPage",ConstantsData.ROW_COUNT_PER_PAGE+"");
        map.put("provinceName",provinceName);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map + "");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestHouseEstate(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseTSubscriber<HouseEstateResult>(this) {
                        @Override
                        public void onResponseCallback(JSONObject response, String resultCode) {
                            LLog.d(response+"");
                        }

                        @Override
                        public void onNext(HouseEstateResult houseEstateResult) {
                            super.onNext(houseEstateResult);
                            if(TextUtils.equals("000",houseEstateResult.getCode())){
                                HouseEstateInfo houseEastateInfo = houseEstateResult.getData().getHomeEstateInfo().get(0);
                                tv_title.setText(houseEastateInfo.getTitle());
                                if(houseEastateInfo.getCreateTime()!=null) {
                                    tv_time.setText(D.getDateStrFromStamp("yyyy-MM-dd HH:mm",houseEastateInfo.getCreateTime()));
                                }
                                if(houseEastateInfo.getTotalPrice()!=null&&!houseEastateInfo.getTotalPrice().equals(""))
                                    tv_total_price.setText((int)Double.parseDouble(houseEastateInfo.getTotalPrice())+"万");
                                tv_room.setText(houseEastateInfo.getSittingRoom()+"室"+houseEastateInfo.getLivingRoom()+"厅"+houseEastateInfo.getBathRoom()+"卫");
                                if(houseEastateInfo.getRoomAcreage()!=null&&!houseEastateInfo.getRoomAcreage().equals(""))
                                    tv_area.setText((int)Double.parseDouble(houseEastateInfo.getRoomAcreage())+"m²");
                                if(houseEastateInfo.getSinglePrice()!=null&&!houseEastateInfo.getSinglePrice().equals(""))
                                    tv_single_price.setText((int)Double.parseDouble(houseEastateInfo.getSinglePrice())+"元/m²");
                                tv_chaoxiang.setText(houseEastateInfo.getOrientation());
                                tv_floor.setText(houseEastateInfo.getFloor()+"层");
                                tv_location.setText(houseEastateInfo.getPlaces());
                                if(houseEastateInfo.getRoomType().equals("1")) {
                                    tv_type.setText("普通住宅");
                                }
                                if(houseEastateInfo.getRoomType().equals("2")) {
                                    tv_type.setText("商业住宅");
                                }
                                tv_zhuangxiu.setText(houseEastateInfo.getRenovation());
                                tv_chanqiu.setText(houseEastateInfo.getPropertyRight()+"年");
                                tv_house_age.setText(houseEastateInfo.getRoomAge()+"年");
                                tv_detail.setText(houseEastateInfo.getHousingProfile());
                                tv_contact_name.setText(houseEastateInfo.getRoomOwner());
                                tv_contact_tel.setText(houseEastateInfo.getPhone());
                                if(houseEastateInfo.getOriginalImg() != null && !houseEastateInfo.getOriginalImg().equals("")) {
                                    String str2=houseEastateInfo.getOriginalImg().replace(" ", "");//去掉所用空格
                                    imageUrls = Arrays.asList(str2.split(","));
                                    banner_1.setData(imageUrls,null);
                                    banner_1.setmAdapter(HouseInfoDetailActivity.this);
                                    mHandler.obtainMessage(111,imageUrls.size(),0).sendToTarget();
                                    banner_1.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                        @Override
                                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                        }

                                        @Override
                                        public void onPageSelected(int position) {
                                            mHandler.obtainMessage(112, position+1, imageUrls.size()).sendToTarget();
                                        }

                                        @Override
                                        public void onPageScrollStateChanged(int state) {

                                        }
                                    });
                                }
                            }
                        }

                    });
    }
    @Override
    protected void onResume() {
        super.onResume();
        banner_1.startAutoPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        banner_1.stopAutoPlay();
    }

    @Override
    public void loadBanner(XBanner banner, View view, int position) {
        ImageView iv=(ImageView)view;
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
//        Arad.imageLoader.load(imageUrls.get(i)).placeholder(R.drawable.default_image).into(iv);
        Glide.with(this).load(imageUrls.get(position)).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                .error(R.drawable.default_image).into(iv);
    }
}
