# Goals of an Enterprise Architecture #

- Be robust
- Be performant and scalable
- Take advantage of design principles
- Avoid unnecessary complexity
- Be maintainable and extensible
- Be delivered on time
- Be easy to test
- Promote reuse

Depending business requirements:
- Support for multiple client types
- Portability 

# Deciding Whether to Use a Distributed Architecture #

- Using EJB makes applications harder to test
	- 过度依赖EJB容器服务
- Using EJB makes applications harder to deploy
	- 类加载器复杂
	- 部署复杂
	- 开发-->部署-->测试周期更慢，削减开发人员生产力

- 使用远程接口的EJB束缚实现OO设计

