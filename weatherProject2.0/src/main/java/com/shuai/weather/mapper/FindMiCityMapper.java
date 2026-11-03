package com.shuai.weather.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shuai.weather.pojo.MiCityCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FindMiCityMapper extends BaseMapper<MiCityCode> {


    @Select("SELECT * FROM MiCityCode WHERE cityname = #{cityname}")
    MiCityCode findByCityName(@Param("cityname") String cityName);


}
