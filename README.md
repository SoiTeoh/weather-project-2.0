# Weather Project 2.0

微信天气小程序与 Java 天气服务端。

## 项目结构

- MiniWeatherProject2.0/miniprogram：微信小程序、天气页面和 ECharts 图表。
- weatherProject2.0：Spring Boot 2.2.5、Maven、MyBatis Plus、MySQL 后端，源码目标为 Java 11。

## 启动

1. 安装 JDK 11、Maven、MySQL 和微信开发者工具。
2. 将后端三个 maven_*.properties.example 复制为去掉 .example 的文件，填写数据库配置。
3. 将 src/main/resources/application.yml.example 复制为 application.yml，默认关闭 HTTPS；如需启用请自行配置本地证书。
4. 根据所用接口设置环境变量 TIANQI_API_SECRET_KEY、TIANQI_API_USER_ID、API_KEY、GETSIGN、SIGN、APPCODE。示例测试还使用 TEST_DATABASE_URL、DATASOURCE_USERNAME、DATASOURCE_PASSWORD。
5. 在后端目录运行 mvn -Pdev spring-boot:run；原 POM 默认激活 test/prod profile，运行前请核对生效配置。
6. 微信开发者工具导入 miniprogram，填写自己的 AppID，将 pages/weather/helloweather.js 中 http://ip:端口 替换为后端地址。

## 限制

未包含数据库初始化 SQL，需自行准备表结构和城市数据。第三方接口及原有套餐需核实可用性。本次只整理首次提交，未验证编译和端到端运行。

证书、真实数据库配置、IDE 文件和编译产物不上传。被修改文件的原始版本保存在本地 .local-backup（不入库）。
