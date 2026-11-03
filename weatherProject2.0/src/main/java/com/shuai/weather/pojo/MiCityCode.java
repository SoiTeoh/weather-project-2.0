package com.shuai.weather.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("MiCityCode")
public class MiCityCode implements Serializable {

    @TableField("id")
    private String id;
    @TableField("pid")
    private String pid;
    @TableField("citycode")
    private String citycode;
    @TableField("cityname")
    private String cityname;
    @TableField("postcode")
    private String postcode;
    @TableField("areacode")
    private String areacode;



}
