----------
1/21/2017 3:06:23 PM 

----------
## 系统架构 ##
1. 系统组件
	- CAS Server
		- 基于spring框架的java程序
		- 主要功能是对用户进行身份认证和对cas client授权访问
		- 通过发布和校验ticket完成功能
			- 用户身份认证通过，在cas server路径下写入身份认证成功的票据cookie
				- e.g., **Set-Cookie:** CASTGC=TGT-1312-ncCKemb0UF9uDnuLn6WxGPIuA56pgceKAzIBMMoRJiulhVIVza-cas; Path=/
			- 根据TGT生成ST,使用设置HTTP头Location字段的方式响应给浏览器，实现请求跳转
				- **Location:** http://10.18.147.181/home/index.action?ticket=ST-22619-LSXgfE3n9MgVzpXvbymG-cas
			- cas client将ST传递给cas server校验ST，校验通过授权访问cas client
	- CAS Clients
		- **Platforms**:集成使用cas client包（通过CAS，SAML，OAuth等协议）对接cas server
			- Apache httpd Server(mod **auth cas module**)
			- Java(**Java CAS Client**)
			- .NET(**.NET CAS Client**)
			- PHP(**phpCAS**)
			- Python(**paycas**)
			- Ruby(**rubycas-client**)
		- **Applications**：直接对接cas server
			- Outlook Web Application(ClearPass++, .NET Client)
			- Atlassian Confluence
			- Atlassian JIRA
			- Drupal
			- Liferay
			- uPortal
1. Ticketing
	- TicketRegistry
	- ExpirationPolicy