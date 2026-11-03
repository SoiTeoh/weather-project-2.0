package com.shuai.weather.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.weather.mapper.FindLocationMapper;
import com.shuai.weather.pojo.CityCode;
import com.shuai.weather.dtos.LocationInfo;
import com.shuai.weather.service.findLocationService;
import com.shuai.weather.util.GetPostUtil;
import org.springframework.stereotype.Service;

import static com.shuai.weather.constant.APIConfig.*;

@Service
public class findLocationServiceImpl extends ServiceImpl<FindLocationMapper, CityCode> implements findLocationService {


    @Override
    public LocationInfo findLocation(String latitude, String longitude) {

        //https://api.ahfi.cn/api/mapjx? + lat + "=" + latitude + "&" + lng + "=" + longtitude; API失效
        //String url = TIANQI_DAILY_WEATHER_URL + LATITUDE + "=" + latitude + "&" + LONGITUDE + "=" + longitude;
        //改用高德地图逆地理编码
        //https://restapi.amap.com/v3/geocode/regeo?output=xml&location=116.310003,39.991957&key=<用户的key>&radius=1000&extensions=all
        String url = TIANQI_DAILY_WEATHER_URL + longitude + "," + latitude + "&key="+ API_KEY;
        //返回JSON对象
        String result = GetPostUtil.sendGet(url);

        //String replace = result.replace(result.substring(result.length() - 5, result.length()), "");

        //新建位置对象，设置位置信息
        LocationInfo locationInfo = new LocationInfo();
        //将所得信息转化为JsonObject对象
        JSONObject jsonObject = JSON.parseObject(result);

        JSONObject regeocode = jsonObject.getJSONObject("regeocode");
        JSONObject addressComponent = regeocode.getJSONObject("addressComponent");

        String country = addressComponent.getString("country");
        locationInfo.setCountry(country);

        String province = addressComponent.getString("province");
        locationInfo.setProvince(province);

        String city = addressComponent.getString("city");
        locationInfo.setCity(city);

        String cityadcode = addressComponent.getString("adcode");
        locationInfo.setCityadcode(cityadcode);

        String district = addressComponent.getString("district");
        locationInfo.setDistrict(district);

       /* //获取JSONObject对象：data
        JSONObject data = jsonObject.getJSONObject("data");

        String country = data.getString("country");
        locationInfo.setCountry(country);

        String countrycode = data.getString("countrycode");
        locationInfo.setCountrycode(countrycode);

        String province = data.getString("province");
        locationInfo.setProvince(province);

        String city = data.getString("city");
        locationInfo.setCity(city);

        String cityadcode = data.getString("cityadcode");
        locationInfo.setCityadcode(cityadcode);

        String district = data.getString("district");
        locationInfo.setDistrict(district);

        String desc = data.getString("desc");
        locationInfo.setDesc(desc);*/

        locationInfo.setLatitude(latitude);
        locationInfo.setLongitude(longitude);

        return locationInfo;
    }


}
