----------
2/18/2017 2:02:01 PM 

----------

## 什么是AOP ##

1. OOP引入抽象、封装、继承、多态等概念建立一种对象层次结构，用以模拟公共行为的集合。
2. 为分散的对象引入公共行为则需要AOP
	- OOP适合从上到下的关系，AOP适合从左到右的关系
		- 如日志功能
	- 散布在各处的无关的代码称之为横切代码
		- 如安全性、异常处理和透明的持续性
	- 封装多个类的公共行为到一个可重用模块，即为Aspect
		- 减少系统的重复代码
		- 降低模块间的耦合度
		- 有利于未来的可操作性和可维护性
	- AOP实现技术
		- 动态代理技术
			- 截取消息，对该消息进行装饰，以取代原有对象行为的执行
		- 静态织入
			- 特定的语法创建Aspect，编译期间织入有关“方面”的代码
	- AOP使用场景
		- Authentication 权限
		- Caching 缓存
		- Context Passing 内容传递
		- Error Handling 错误处理
		- Lazy Loading 懒加载
		- Debugging 调试
		- Logging,Tracing,Profiling and Monitoring 记录、跟踪、优化、校准
		- Performance optimization 性能优化
		- Persistence 持久化
		- Resource Pooling 资源池
		- Synchronization 同步
		- Transactions 事务
	- 概念
		- Aspect
			- 横切对象的模块化
		- JoinPoint
			- 程序中明确的点，如方法的调用或特定异常的抛出
		- Advice
			- 在特定的连接点，AOP框架执行的动作。包括around，before，throws通知
				- Spring以拦截器做通知模型，维护一个“围绕”连接点的拦截器链
		- Pointcut
			- 指定一个通知将被引发的一系列连接点的集合。
				- 例如使用正则表达式
		- Introduction
			- 添加方法或字段到被通知的类
		- Target Object
		- AOP Proxy
		- Weaving