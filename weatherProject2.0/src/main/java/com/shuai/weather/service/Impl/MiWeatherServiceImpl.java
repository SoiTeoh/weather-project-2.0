package com.shuai.weather.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.shuai.weather.constant.APIConfig;
import com.shuai.weather.dtos.LocationInfo;
import com.shuai.weather.mapper.FindLocationMapper;
import com.shuai.weather.mapper.FindMiCityMapper;
import com.shuai.weather.pojo.CityCode;
import com.shuai.weather.pojo.MiCityCode;
import com.shuai.weather.service.MiWeatherService;
import com.shuai.weather.service.findLocationService;
import com.shuai.weather.util.GetPostUtil;
import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.shuai.weather.constant.APIConfig.*;

@Service
public class MiWeatherServiceImpl implements MiWeatherService {


    @Autowired
    private com.shuai.weather.service.findLocationService findLocationService;

    @Autowired
    private FindMiCityMapper findMiCityMapper;


    @Override
    public String currentweather(String latitude, String longitude) {

        LocationInfo location = findLocationService.findLocation(latitude, longitude);
        //查询City_name
        String city = location.getCity();
        String district = location.getDistrict();
        String result = null;

        String substring = city.substring(0, city.length() - 1);


        MiCityCode citycode = findMiCityMapper.findByCityName(substring);
        String citycode1 = citycode.getCitycode();


        result = MiWeather(citycode1, latitude, longitude);


        return result;
    }

    private String MiWeather(String city_code, String latitude, String longitude) {

        //https://weatherapi.market.xiaomi.com/wtr-v3/weather/all?latitude=&longitude=&appKey=&sign=&locale=
        String url = HOST + "latitude=" + latitude + "&longitude=" + longitude + "&appKey=" + city_code + "&sign=" + SIGN + "&locale=" + LOCALE + "&isGlobal=" + ISGLOBAL;

        String weather = GetPostUtil.sendGet(url);
        //最后返回的总体数据
        Map<Object, Object> weatherdata = new LinkedHashMap<>();
        //hourArrs数据
        Map<Object, Object> hourArrs = new LinkedHashMap<>();
        Map<Object, Object> dayArrs = new LinkedHashMap<>();


        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(weather);


            //未来日期预报，低温、高温、天气代表数，风向
            JsonNode forecastDaily = jsonNode.get("forecastDaily");

            //日升日落
            Map<String, Object> suntime = new LinkedHashMap<>();
            JsonNode sunRiseSet = forecastDaily.get("sunRiseSet");

            JsonNode sunvalue = sunRiseSet.get("value");
            Map<Object, Object> day = new LinkedHashMap<>();
            for (int i = 0; i < sunvalue.size(); i++) {
                JsonNode dateinfo = sunvalue.get(i);
                String from = dateinfo.get("from").toString();
                String date = from.substring(0, 11);
                day.put(i,date);
            }

            JsonNode firstvalue = sunvalue.get(0);
            String sunrise = firstvalue.get("from").toString();
            String sunset = firstvalue.get("to").toString();


            String time1 = sunrise.substring(12, 17);
            suntime.put("sunrise", time1);
            String time2 = sunset.substring(12, 17);
            suntime.put("sunset", time2);

            Map<Object, Object> temp = new LinkedHashMap<>();
            Map<Object, Object> bjTime = new LinkedHashMap<>();
            Map<String, Object> weathernum = new LinkedHashMap<>();
            Map<Integer, Object> winddirect = new LinkedHashMap<>();
            Map<String, Object> hourlyweather = new LinkedHashMap<>();
            Map<String, Object> hourtemp = new LinkedHashMap<>();


            //总循环
            for (int j = 0; j < 8; j++) {

                Map<Object, Object> hourArrs1 = new LinkedHashMap<>();
                Map<String, Object> dayArrs1 = new LinkedHashMap<>();

                //未来5天日期
                for (int i = 0; i < sunvalue.size(); i++) {
                    JsonNode dateinfo = sunvalue.get(i);
                    String from = dateinfo.get("from").toString();
                    String date = from.substring(1, 11);
                    day.put(i,date);
                }
                dayArrs1.put("date",day.get(j));

                //未来5天天气代表数
                JsonNode dailyweather = forecastDaily.get("weather");
                JsonNode dailyweathervalue = dailyweather.get("value");
                String a = dailyweathervalue.toString();
                List<String> results = extractNumbers(a);
                //System.out.println(results);
                weathernum.put("dailys", results);
                String realweather = numchangeweather(Collections.singletonList(results.get(j)));
                dayArrs1.put("img",results.get(j));
                dayArrs1.put("weather", realweather);


                //未来5天风向
                JsonNode wind = forecastDaily.get("wind");
                JsonNode direction = wind.get("direction");
                JsonNode windvalue = direction.get("value");
                for (int i = 0; i < windvalue.size(); i++) {
                    JsonNode windfromto = windvalue.get(i);
                    JsonNode to = windfromto.get("to");
                    winddirect.put(i, to);
                }
                dayArrs1.put("wind",winddirect.get(j));

                //未来5天的温度
                JsonNode temperature = forecastDaily.get("temperature");
                JsonNode tempvalue = temperature.get("value");
                for (int i = 0; i < tempvalue.size(); i++) {
                    JsonNode fromto = tempvalue.get(i);
                    JsonNode high = fromto.get("from");
                    JsonNode low = fromto.get("to");
                    List<Object> valueList = new ArrayList<>();
                    valueList.add(high);
                    valueList.add(low);
                    temp.put(i, valueList);
                }
                dayArrs1.put("temp",temp.get(j));

                //未来8小时预报,需要温度、天气代表数
                JsonNode forecastHourly = jsonNode.get("forecastHourly");

                //用于标识未来8小时时间
                JsonNode hourwind = forecastHourly.get("wind");
                JsonNode hourwindvalue = hourwind.get("value");
                for (int i = 0; i < hourwindvalue.size(); i++) {
                    JsonNode everydatetime = hourwindvalue.get(i);
                    String datetime = everydatetime.get("datetime").toString();
                    String realdatetime = datetime.substring(12, 17);
                    bjTime.put(i,realdatetime);
                }
                hourArrs1.put("BJtime",bjTime.get(j));



                JsonNode hourweather = forecastHourly.get("weather");
                String hourvalue = hourweather.get("value").toString();
                JsonNode value = hourweather.get("value");
                List<String> hourresults = new ArrayList<>();
                String numchangeweather = null;
                for (int i = 0; i < value.size(); i++) {
                    hourresults.add(String.valueOf(value.get(i)));
                    numchangeweather = numchangeweather(Collections.singletonList(hourresults.get(i)));
                }
                hourlyweather.put("weather", hourvalue);
                hourArrs1.put("img",value.get(j));
                hourArrs1.put("weather",numchangeweather);

                //每小时温度
                JsonNode temperature1 = forecastHourly.get("temperature");
                JsonNode tempvalue1 = temperature1.get("value");
                hourtemp.put("temp", tempvalue1);
                hourArrs1.put("hourtemp",tempvalue1.get(j));


                hourArrs.put(j,hourArrs1);
                dayArrs.put(j,dayArrs1);

            }


            weatherdata.put("hourArrs", hourArrs);
            weatherdata.put("dayArrs", dayArrs);
            weatherdata.put("suntime",suntime);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        Gson gson = new Gson();
        String json = gson.toJson(weatherdata);


        return json;

    }

    private String numchangeweather(List<String> results) {

        for (String result : results) {

            if (result.equals("0")){
                return "晴";
            }else if (result.equals("1")){
                return "多云";
            }else if (result.equals("2")){
                return "阴";
            }else if (result.equals("3")){
                return "阵雨";
            }else if (result.equals("4")){
                return "雷阵雨";
            }else if (result.equals("5")){
                return "雷阵雨伴有冰雹";
            }else if (result.equals("6")){
                return "雨夹雪";
            }else if (result.equals("7")){
                return "小雨";
            }else if (result.equals("8")){
                return "中雨";
            }else if (result.equals("9")){
                return "大雨";
            }else if (result.equals("10")){
                return "暴雨";
            }else if (result.equals("11")){
                return "大暴雨";
            }else if (result.equals("12")){
                return "特大暴雨";
            }else if (result.equals("13")){
                return "阵雪";
            }else if (result.equals("14")){
                return "小雪";
            }else if (result.equals("15")){
                return "中雪";
            }else if (result.equals("16")){
                return "大雪";
            }else if (result.equals("17")){
                return "暴雪";
            }else if (result.equals("18")){
                return "雾";
            }else if (result.equals("19")){
                return "冻雨";
            }else if (result.equals("20")){
                return "沙尘暴";
            }else if (result.equals("21")){
                return "小雨-中雨";
            }else if (result.equals("22")){
                return "中雨-大雨";
            }else if (result.equals("23")){
                return "大雨-暴雨";
            }else if (result.equals("24")){
                return "暴雨-大暴雨";
            }else if (result.equals("25")){
                return "大暴雨-特大暴雨";
            }else if (result.equals("26")){
                return "小雪-中雪";
            }else if (result.equals("27")){
                return "小雪-大雪";
            }else if (result.equals("28")){
                return "大雪-暴雪";
            }else if (result.equals("29")){
                return "浮沉";
            }else if (result.equals("30")){
                return "扬沙";
            }else if (result.equals("31")){
                return "强沙尘暴";
            }else if (result.equals("32")){
                return "浓雾";
            }else if (result.equals("49")){
                return "强浓雾";
            }else if (result.equals("53")){
                return "雾霾";
            }else if (result.equals("54")){
                return "中毒霾";
            }else if (result.equals("55")){
                return "重度霾";
            }else if (result.equals("56")){
                return "严重霾";
            }else if (result.equals("57")){
                return "大雾";
            }else if (result.equals("58")){
                return "特强浓雾";
            }else if (result.equals("99")){
                return "雾";
            }else if (result.equals("301")){
                return "雨";
            }else if (result.equals("302")){
                return "雪";
            }else {
                return "Warning!";
            }

        }

        return null;
    }

    private List<String> extractNumbers(String a) {
        List<String> numbers = new ArrayList<>();
        String pattern = "\"to\":\"(\\d+)\"";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(a);

        while (matcher.find()) {
            numbers.add(matcher.group(1));
        }

        return numbers;
    }
}