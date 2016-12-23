
----------
12/21/2016 7:51:06 PM 

----------
# 构建Java中间件 #

1. 什么是中间件
	- 应用与应用之间的桥梁
	- 应用与服务之间的桥梁
	- 解决特定场景问题的组件
2. 三个领域中间件
 	- 远程调用的和对象访问的中间件
	 	- 解决分布式环境下互相访问的问题
 	- 消息中间件
	 	- 解决应用之间的消息传递、解耦、异步问题
 	- 数据访问中间件 
	 	- 解决数据库的共性问题的组件
3. 构建java中间件的基础知识 
	- 跨平台的Java运行环境---JVM
	- 垃圾回收与内存堆布局
		- 分代布局
			- **Hostspot**: Young/Tenured/Perm
				- Eden-->Survivor-->Tenured
		- Hotspot垃圾回收策略、设置和调优

4. Java并发编程的类、 接口和方法
	- 线程池
	- synchronized
	- ReentrantLock
		- 提供tryLock方法
		- 接收一个boolean类型参数描述锁是否公平
		- ReentrantReadWriteLock
			- readLock()
			- writeLock()
		- volatile
			- 变量可见性
				- 保证同一个变量在多线程中的可见性，更多用于修饰作为开关状态的变量
		- Atomics
		- wait、notify和notifyAll
		- CoutDownLatch
		- CyclicBarrier
		- Semaphore
		- Exchanger
		- Future和FutureTask
		- 并发容器
			- CopyOnWrite
				- 写时重建容器
			- Concurrent
				- 尽量保证读不加锁，修改时不影响读
5. 动态代理 
	- 静态代理
	- 动态代理 

6. 反射
	- 获取对象属于哪个类
	- 获取类的信息	
		- 获取类的名称
		- 获取类中定义的方法
		- 获取类中定义的成员
	- 构建对象
		- 要求被构造的对象的类一定要有一个无参数的构造函数
	- 动态执行方法
		- 首先获取方法对象
		- 然后嗲用Method的invoke方法
			- 第一个参数可以为null，用于调用静态方法
	- 动态操作属性
		- 首先获取Fileld对象
		- 然后通过set方法设置属性值
			- 第一个参数可以为null，用于设置静态属性

7. 网络通信实现选择
	- BIO
	- NIO
		- MINA，Netty
	- AIO

8. 分布式系统中的中间件 
	- 应用拆分
		- 服务框架完成应用拆分，完成服务化
	- 服务拆分
	- 数据拆分
		- 数据层完成数据的拆分以及整个数据的管理、扩容、迁移等工作
	- 应用解耦
		- 完成应用的解耦