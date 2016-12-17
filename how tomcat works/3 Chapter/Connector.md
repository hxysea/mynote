# Connector #

1. **catalina**(tomcat中一个**处理servlet**模块)两个重要部分：

	- **Container**
	- **Connector**

2. **StringManager**

	- 将各个模块的错误信息国际化
3. **The Application**
	- Connector
		- Connector and its supporting class（HttpConnector and HttpProcessor）
		- The class represent HTTP requests(HttpRequest) and its supporting classes
		- The class represent HTTP response(HttpResponse) and its supporting classes
		- Facade classes(HttpRequestFacade and HttpResponseFacade)
		- The Constant class
	- Startup
		- Bootstrap Class 启动应用
	- Core  
		- ServletProcessor
		- StaticResourceProcessor 


