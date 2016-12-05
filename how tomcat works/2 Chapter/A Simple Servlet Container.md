# A Simple Servlet Container #

1. **Servlet Interface**

	- public void **init**(Servlet config)
	- public void **service**(ServletRequest request, ServletResponse response)throws ServletException, IOExcepion
	- public void **destroy**()
	- public ServletConfig **getServletConfig**()
	- public String **getSevletInfo**()
2.  **What** does servlet container do when http request comes?

	- 第一次请求来时，装载匹配的Servlet类并调用Servlet的**init**方法且只初始化**一次**
	- 为每次请求创建一个ServletRequest对象和一个ServletResponse对象
	- 调用Servlet的**service**方法，传递上一步创建的**ServletReques**t和**ServletResponse**对象
	- 关闭Servlet时，调用**destroy**方法并卸载Servlet类

3. **Why** need Servlet Container？
	- 利用反射，同一入口处理不同请求 
	- 利用Servlet作为父接口，使用了多态的特性

4. **Application1**
	- HttpServer
	- Request
	- Response
	- StaticResouceProcessor
	- ServletProcessor
	- Constants


 
