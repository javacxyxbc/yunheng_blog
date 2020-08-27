# 云衡博客

<p align=center>
![输入图片说明](https://images.gitee.com/uploads/images/2020/0827/202455_66b975f3_5657726.png "屏幕截图.png")
</p>
<p align=center>
   云衡博客，一个基于微服务架构的前后端分离博客系统
</p>
<p align="center">
<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
    	<img src="https://img.shields.io/hexpm/l/plug.svg" ></img>
		<img src="https://img.shields.io/badge/JDK-1.8+-green.svg" ></img>
        <img src="https://img.shields.io/badge/springboot-2.2.2.RELEASE-green" ></img>
<img src="https://img.shields.io/badge/SpringCloud-Hoxton.RELEASE-brightgreen" ></img><img src="https://img.shields.io/badge/vue-2.5.17-green" ></img>
<img src="https://img.shields.io/badge/swagger-2.6.1-green" ></img>
<img src="https://img.shields.io/badge/mybatis--plus-3.1.2-green" ></img></a></p>


##  前言

该项目是本人最近做的一个多人博客系统,后端主要基于java 的springcloud微服务,前端由vue构建.

我会把这个项目当作自己的一个开源项目并不断去维护他

之后,我会利用github pages来写一些文章,说明项目中各个模块的内容,如权限管理,架构设计,自定义AOP等.

欢迎大家提Issues,也请大家帮忙点个赞,谢谢~~!

## 项目介绍

- 云衡博客，一个基于微服务架构的前后端分离博客系统。
- 前端使用Vue + Element , 后端使用spring boot + spring cloud + mybatis-plus进行开发
- 使用  Jwt + Spring Security+redis做登录验证和权限校验
- 使用ElasticSearch作为搜索服务
- 文件支持上传七牛云,支持本地nginx静态文件存储
- 使用rabbitmq作为消息队列.
- 数据库方面利用MySQL Sharding 分库分表,以mysql为主,redis缓存为辅,充分利用缓存,提高系统的吞吐量.
- 项目代码简洁规范,代码约束明确
- 模块之间分工明确,达到高解耦效果



## 站点演示



> 【前台演示】：<http://124.70.200.174:82 />
>
> 【admin管理员端】：<http://124.70.200.174 />
>
> 【演示账号】：hy2020	123456
>
> 注:演示账号受权限限制,只有查询权限,没有增删改查的权限,具体的请pull到自己的电脑上自行体验



## 运行配置

云衡博客使用了一些监控的Spring Cloud组件，但是并不一定都需要部署，必须启动的服务包含

`hy_admin`，`hy_web`，`hy_picture`， `hy_sms`, `hy_search`,`hy_gateway`

其它的jar都可以不启动，也不影正常使用

推荐配置：2核4G

## 项目特点

- 友好的代码结构及注释，便于阅读及二次开发
- 实现前后端分离，通过Json进行数据交互，前端再也不用关注后端技术
- 页面交互使用Vue2.x，极大的提高了开发效率。
- 引入swagger文档支持，方便编写API接口文档。
- 引入RabbitMQ 消息队列，用于邮件发送、更新Redis和elasticsearch
- 引入JustAuth第三方登录开源库，支持Gitee、Github账号登录。
- 引入ElasticSearch 作为全文检索服务，并支持可插拔配置
- 引入七牛云对象存储，同时支持本地nginx文件存储
- 引入RBAC权限管理设计，灵活的权限控制，按钮级别的细粒度权限控制，满足绝大部分的权限需求
- 引入Zipkin链路追踪，聚合各业务系统调用延迟数据，可以一眼看出延迟高的服务
- 采用自定义参数校验注解，轻松实现后端参数校验
- 自定义AOP切面,利用注解实现权限验证,限流,记录操作日志等
- 采用Nacos作为服务发现和配置中心，轻松完成项目的配置的维护
- 采用Sentinel流量控制框架，通过配置再也不怕网站被爆破

## 项目文档

文档地址：待做(我会在最近这段时间内写出来)

## 项目地址

目前项目托管在Gitee和Github平台上中，欢迎大家star 和 fork 支持~

- github地址 `https://github.com/hzh0425/yunheng-blog`
- Gitee地址  `https://gitee.com/zisuu/yunheng_blog`

## 目录介绍

- hy_admin: 提供admin端API接口服务；
- hy_web：提供web端API接口服务；
- hy_picture： 图片服务，用于图片上传和下载；
- hy_sms：消息服务，用于更新ElasticSearch、redis、邮件和短信发送
- hy_search：搜索服务，ElasticSearch作为全文检索工具
- hy_commons：公共模块，主要用于存放Entity实体类、Feign远程调用接口、以及公共config配置
- hy_utils: 是常用工具类；
- hy_xo: 是存放 VO、Service，Dao层的
- hy_base: 是一些Base基类
- doc: 存放mysql数据库,docker,nacos等配置文件
- hy_monitor：监控服务，集成SpringBootAdmin用于管理和监控SpringBoot应用程序
- hy_spider：爬虫服务`（目前还未完善）`
- hy_gateway：网关服务
- hy_zipkin：链路追踪服务，`目前使用java -jar的方式启动`
- vue_hy_admin：VUE的后台管理页面
- vue_hy_web： VUE的门户网站

## 技术选型

### 系统架构图

### 后端技术

|      技术      |          说明           |                             官网                             |
| :------------: | :---------------------: | :----------------------------------------------------------: |
|   SpringBoot   |         MVC框架         | [ https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
|  SpringCloud   |       微服务框架        |           https://spring.io/projects/spring-cloud/           |
| SpringSecurity |     认证和授权框架      |          https://spring.io/projects/spring-security          |
|  MyBatis-Plus  |         ORM框架         |                   https://mp.baomidou.com/                   |
|   Swagger-UI   |      文档生产工具       | [ https://github.com/swagger-api/swagger-ui](https://github.com/swagger-api/swagger-ui) |
|     Kibana     |    分析和可视化平台     |               https://www.elastic.co/cn/kibana               |
| Elasticsearch  |        搜索引擎         | [ https://github.com/elastic/elasticsearch](https://github.com/elastic/elasticsearch) |
|      Solr      |        搜索引擎         |                http://lucene.apache.org/solr/                |
|    RabbitMQ    |        消息队列         |   [ https://www.rabbitmq.com/](https://www.rabbitmq.com/)    |
|     Redis      |       分布式缓存        |                      https://redis.io/                       |
|     Docker     |       容器化部署        |      [ https://www.docker.com](https://www.docker.com/)      |
|     Druid      |      数据库连接池       | [ https://github.com/alibaba/druid](https://github.com/alibaba/druid) |
|     七牛云     |    七牛云 - 对象储存    |         https://developer.qiniu.com/sdk#official-sdk         |
|      JWT       |       JWT登录支持       |                 https://github.com/jwtk/jjwt                 |
|     SLF4J      |        日志框架         |                    http://www.slf4j.org/                     |
|     Lombok     |    简化对象封装工具     | [ https://github.com/rzwitserloot/lombok](https://github.com/rzwitserloot/lombok) |
|     Nginx      | HTTP和反向代理web服务器 |                      http://nginx.org/                       |
|    JustAuth    |    第三方登录的工具     |             https://github.com/justauth/JustAuth             |
|     Hutool     |     Java工具包类库      |                  https://hutool.cn/docs/#/                   |
|    阿里大于    |      短信发送平台       |            https://doc.alidayu.com/doc2/index.htm            |
| Github Actions |       自动化部署        |              https://help.github.com/en/actions              |
|     Zipkin     |        链路追踪         |             https://github.com/openzipkin/zipkin             |
| Flexmark-java  |    Markdown转换Html     |            https://github.com/vsch/flexmark-java             |
|   Ip2region    |    离线IP地址定位库     |          https://github.com/lionsoul2014/ip2region           |

### 前端技术

|         技术          |         说明          |                             官网                             |
| :-------------------: | :-------------------: | :----------------------------------------------------------: |
|        Vue.js         |       前端框架        |                      https://vuejs.org/                      |
|      Vue-router       |       路由框架        |                  https://router.vuejs.org/                   |
|         Vuex          |   全局状态管理框架    |                   https://vuex.vuejs.org/                    |
|        Element        |      前端ui框架       |    [ https://element.eleme.io](https://element.eleme.io/)    |
|         Axios         |     前端HTTP框架      | [ https://github.com/axios/axios](https://github.com/axios/axios) |
|        Echarts        |       图表框架        |                      www.echartsjs.com                       |
|     mavon-editor      |       md编辑器        |           <https://www.jianshu.com/p/78ea4f94a3d0>           |
|     Highlight.js      |   代码语法高亮插件    |         https://github.com/highlightjs/highlight.js          |
|      Tui-editor       |    Markdown编辑器     |              https://github.com/nhn/tui.editor               |
|      vue-cropper      |     图片裁剪组件      |           https://github.com/xyxiao001/vue-cropper           |
| vue-image-crop-upload |  vue图片剪裁上传组件  |      https://github.com/dai-siki/vue-image-crop-upload       |
|   vue-emoji-comment   | Vue Emoji表情评论组件 |       https://github.com/pppercyWang/vue-emoji-comment       |
|     clipboard.js      |   现代化的拷贝文字    |                  http://www.clipboardjs.cn/                  |
|      js-beautify      |  美化JavaScript代码   |         https://github.com/beautify-web/js-beautify          |
|     FileSaver.js      |   保存文件在客户端    |           https://github.com/eligrey/FileSaver.js            |
|   vue-side-catalog    |      目录导航栏       |          <https://www.jb51.net/article/179514.htm>           |

## 项目搭建

### 后端

1.将doc文件下的数据库脚本,导入到mysql数据库

2.将项目pull到自己的电脑上,导入到idea

3.在terminal终端输入:

`mvn clean install -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true`

4.将doc文件下nacos配置文件,导入到nacos自己的nacos,修改配置中的各个服务地址(如rabbitmq,redis等)

5.修改各个模块下的bootstrap.yml,将nacos地址改为自己的地址

6.启动hy_gateway,hy_admin,hy_sms,hy_search,hy_web

注:只启动hy_gateway和hy_admin也是可以用的,只有一些特定功能用不了

启动后可以打开swagger-ui界面,如admin:

`localhost:8601/swagger-ui.html`

![输入图片说明](https://images.gitee.com/uploads/images/2020/0827/202439_c96670dd_5657726.png "屏幕截图.png")

### 前端

1.将vue_hy_admin导入到vs中

2.终端输入

```
# 使用淘宝镜像源进行依赖安装，解决国内下载缓慢的问题(出现警告可以忽略)
npm install --registry=https://registry.npm.taobao.org

# 启动项目
npm run dev
```

3.完成后会自动跳出admin页面

4.若出现node-sass  module not found等xxx模块not found的错误,

```
npm install xxxxx --save
```

5.vue_hy_web和admin差不多的步骤

## 环境搭建

### 开发工具



|     工具     |       说明        |                             官网                             |
| :----------: | :---------------: | :----------------------------------------------------------: |
|     IDEA     |    Java开发IDE    |           https://www.jetbrains.com/idea/download            |
|      VS      |    前端开发IDE    |               <https://code.visualstudio.com/>               |
| RedisDesktop |  Redis可视化工具  | [ https://redisdesktop.com/download](https://redisdesktop.com/download) |
| SwitchHosts  |   本地Host管理    |             https://oldj.github.io/SwitchHosts/              |
|   X-shell    | Linux远程连接工具 |               https://xshell.en.softonic.com/                |
|    X-ftp     | Linux文件传输工具 |         https://www.netsarang.com/zh/all-downloads/          |
|    SQLyog    |  数据库连接工具   |               https://sqlyog.en.softonic.com/                |
| ScreenToGif  |    Gif录制工具    | [ https://www.screentogif.com/](https://www.screentogif.com/) |

### 开发环境

|     工具      |  介绍  |                             下载                             |
| :-----------: | :----: | :----------------------------------------------------------: |
|      JDK      |        | https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html |
| Elasticsearch | 6.3.0  |               https://www.elastic.co/downloads               |
|  SpringCloud  |  H版   |          <https://spring.io/projects/spring-cloud/>          |
|     MySQL     |  8.0   |                    https://www.mysql.com/                    |
|    Erlang     |  20.3  |                   https://www.erlang.org/                    |
|   RabbitMQ    | 3.7.4  |            http://www.rabbitmq.com/download.html             |
|     Nginx     |  1.10  |              http://nginx.org/en/download.html               |
|     Redis     | 3.3.0  |                  https://redis.io/download                   |
|    Docker     | 1.13.1 |                  <https://www.docker.com/>                   |

## 致谢

欢迎大家来提issue!

|      |      |
| :--: | :--: |
|      |      |

## 将来要做的事



- [x] 门户网站增加登录页面
- [x] 支持第三方登录
- [x] 集成ElasticSearch
- [x] 将图片存储在七牛云中
- [x] 写一个评论模块
- [x] 按钮级别的细粒度权限控制
- [x] 增加评论表情
- [x] 增加数据字典管理
- [x] 前端增加用户个人中心
- [ ] 增加一个FAQ常见问题文档
- [x] 使用Sentinel做服务限流和熔断
- [ ] 增加 云衡博客小程序项目 uniapp_hy_web，基于[ColorUI](https://github.com/weilanwl/ColorUI) 和 [Uniapp](https://uniapp.dcloud.io/)
- [ ] 集成Github Actions，完成云衡博客持续集成服务
- [ ] 完善网盘管理
- [ ] 增加更新记录
- [ ] 完善网关模块
- [ ] 使用Freemark页面静态化技术对博客详情页静态化
- [ ] 让原创文章能够同步到多平台，如：CSDN，掘金，博客园等
- [ ] 增加博客迁移功能，让其它平台的博客，如：CSDN、博客园，WordPress能够同步到云衡博客中
- [ ] 适配门户页面的移动端布局

## 贡献代码

开源项目离不开大家的支持，如果您有好的想法或者代码实现，欢迎提交Pull Request~

1. fork本项目到自己的repo
2. 把fork过去的项目也就是你仓库中的项目clone到你的本地
3. 修改代码
4. commit后push到自己的库
5. 发起PR（pull request） 请求，提交到`dev`分支
6. 等待作者合并

## 赞赏

服务器和域名等服务的购买和续费都会产生一定的费用，为了维持项目的正常运作，如果觉得本项目对您有帮助的话，欢迎朋友能够给予一些支持，非常感谢~
![输入图片说明](https://images.gitee.com/uploads/images/2020/0827/202404_16864e01_5657726.png "屏幕截图.png")

## 相关截图

