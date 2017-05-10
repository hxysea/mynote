----------
3/31/2017 9:22:22 AM 

----------
1. 两个包：
	- dubbo-monitor
	- dubbo-admin

## Linux指令 ##
1. chmod 777 **.sh
2. tail -f 路径/*.log
3. set ff=**，设置编码格式（eg,set ff=unix）

## tomcat相关 ##
1. tomcat请求流程
	```
	请求-->Connector-->Engine-->Host-->Context-->Servlet
						-->Request
						-->Response
	```
2. tomcat配置
	- Tomcat的JVM设置和连接数设置
		- Linux环境下修改，在文件开头增加如下设置：
			- JAVA_OPTS='-Xms256m -Xmx512m'，其中，-Xms表示初始化内存的大小，-Xmx表示可以说设置使用的最大内存  
