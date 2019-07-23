package com.ctrl.android.kcetong.toolkit.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.ctrl.android.kcetong.model.Address;
import com.ctrl.android.kcetong.model.CityModel;
import com.ctrl.android.kcetong.model.DistrictModel;
import com.ctrl.android.kcetong.model.ProvinceModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Administrator on 2016/10/24.
 */

public class AddressData {
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    /**
     * key - 区 values - 邮编
     */
    //protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName ="";
    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode ="";
    public List addressdata(Context context){
        List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();
        AssetManager        asset        = context.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
        } catch (Throwable e) {
            e.printStackTrace();
            Log.d("zzz",e.toString());
        }
        return provinceList;
    }

    public Address TakeAddressGeneId(Context context, Address address)
    {
        List<ProvinceModel> provinceList = addressdata(context);
        for (ProvinceModel provinceModel : provinceList) {
            if (provinceModel.getName().contains(address.getProvinceId()))
            {
                //address.setProviceId(provinceModel.getId());
                address.setProvinceId(provinceModel.getId());
                List<CityModel> cityList = provinceModel.getCityList();
                for (CityModel cityModel : cityList) {
                    if (cityModel.getName().contains(address.getCityId()))
                    {
                        address.setCityId(cityModel.getId());
                        List<DistrictModel> disList = cityModel.getDistrictList();
                        for (DistrictModel districtModel : disList) {
                            if (districtModel.getName().contains(address.getAreaId()))
                            {
                                address.setAreaId(districtModel.getId());
                            }
                        }
                    }
                }
            }
        }
        return address;
    }
}
