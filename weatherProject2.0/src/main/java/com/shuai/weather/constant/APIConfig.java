package com.shuai.weather.constant;

public class APIConfig {

    //心知天气API
    // 生活指数请求地址 https://api.seniverse.com/v3/life/suggestion.json?key=your_api_key&location=shanghai&language=zh-Hans

    public static final String TIANQI_API_SECRET_KEY = System.getenv("TIANQI_API_SECRET_KEY");
    public static final String TIANQI_API_USER_ID= System.getenv("TIANQI_API_USER_ID");

    //高德地图API常量
    public static final String API_KEY = System.getenv("API_KEY");
    //API_EXTENSIONS
    public static final String API_EXTENSIONS = "base";
    //API_OUTPUT
    public static final String API_OUTPUT = "JSON";
    //启用签名
    public static final String GETSIGN = System.getenv("GETSIGN");

    //https://restapi.amap.com/v3/weather/weatherInfo?city=110101&key=<用户key>  CURRENT_WEATHER
    public static final String CURRENT_WEATHER= "https://restapi.amap.com/v3/weather/weatherInfo?";


    //启用高德逆地理编码
    //https://restapi.amap.com/v3/geocode/regeo?output=xml&location=116.310003,39.991957&key=<用户的key>&radius=1000&extensions=all
    public static final String TIANQI_DAILY_WEATHER_URL= "https://restapi.amap.com/v3/geocode/regeo?output=JSON&location=";

    //LONGITUDE
    public static final String LONGITUDE = "lng";
    //LATITUDE
    public static final String LATITUDE = "lat";

    //小米天气实时接口
    //https://weatherapi.market.xiaomi.com/wtr-v3/weather/all?latitude=&longitude=&appKey=&sign=&locale=
    public static final String HOST = "https://weatherapi.market.xiaomi.com/wtr-v3/weather/all?";
    public static final String SIGN = System.getenv("SIGN");
    public static final String ISGLOBAL = "false";
    public static final String LOCALE = "zh_cn";



    //阿里云天气接口，套餐已过期，且以后再无半年免费套餐
    public static final String HOST1 = "https://jisutqybmf.market.alicloudapi.com";
    public static final String CITY_PATH = "/weather/city";
    public static final String WEATHER_PATH = "/weather/query";
    public static final String METHOD = "GET";
    public static final String APPCODE = System.getenv("APPCODE");


}
