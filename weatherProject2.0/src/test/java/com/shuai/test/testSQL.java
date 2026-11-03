package com.shuai.test;

import java.sql.Connection;
import java.sql.DriverManager;

public class testSQL {


    // 定义数据库驱动名称
    private static String driverName = "com.mysql.cj.jdbc.Driver";
    // 定义数据库url
    private static String url = System.getenv("TEST_DATABASE_URL");
    // 有的账号密码直接放在url后可连接
    //private static String url2 = System.getenv("TEST_DATABASE_URL");
    private static String sql = "create database test";

    public static void main(String[] args) {
        Connection connection = null;

        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, System.getenv("DATASOURCE_USERNAME"), System.getenv("DATASOURCE_PASSWORD"));
            // 重写的方法
            connection = DriverManager.getConnection(url);
            connection.createStatement().executeLargeUpdate(sql);
            System.out.println(sql);
            System.out.println(connection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
