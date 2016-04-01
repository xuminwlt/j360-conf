j360-conf
==============

j360系列配置



##简单代码配置分离工具##
- 依赖Spring配置
- 对多环境的配置通过引入文件路径进行多个环境的配置支持，让开发人员只关注代码
- 分别对jar、war工程分开配置


##对比##
- spring boot
- disconf
- spring cloud config

##参考##
- [编程方式] http://www.mojohaus.org/appassembler/appassembler-maven-plugin/usage-program-structure.html
- [进程守护方式] http://www.mojohaus.org/appassembler/appassembler-maven-plugin/usage-program-arguments.html
- [plugin插件github地址] https://github.com/mojohaus/appassembler

- [jsw官网] http://wrapper.tanukisoftware.com/doc/english/properties.html
- [jsw源码地址] https://sourceforge.net/projects/wrapper/files/wrapper_src/Wrapper_3.5.29_20160311/

###module介绍###
- j360-conf-wrapper-service 已经集成了wrapper官方关注的源码，便于调试，否则自己讲wrapper.jar导入到maven仓库引用
- j360-conf-service 使用jsw插件封装jsw进程
- j360-conf-web 配置使用war的web.xml进行配置，外部无法打包，如果使用编程方式启动jetty或者tomcat，则可以使用jsw的参数配置配置文件的地址

