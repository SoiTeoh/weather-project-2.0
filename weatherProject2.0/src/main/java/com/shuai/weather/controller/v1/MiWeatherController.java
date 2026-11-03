package com.shuai.weather.controller.v1;

import com.alibaba.fastjson.JSON;
import com.shuai.weather.api.MiWeatherControllerApi;
import com.shuai.weather.service.MiWeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/weather")
public class MiWeatherController implements MiWeatherControllerApi {

    @Autowired
    private MiWeatherService miWeatherService;


    @GetMapping("/miweather")
    @Override
    public String currentweather(@RequestParam("latitude") String latitude,@RequestParam("longitude") String longitude) {
        String currentweather = miWeatherService.currentweather(latitude, longitude);
        //String jsonString = JSON.toJSONString(currentweather);
        return currentweather;
    }
}
