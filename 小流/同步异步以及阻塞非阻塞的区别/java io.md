# Java IO #

1. java io四组类：
	
	- 什么样的数据：
	- 基于字节操作的I/O接口：InputStream和OutputStream
	- 基于字符操作的I/O接口：Writer和Reader
	- 数据到哪去：
	- 基于磁盘操作的I/O接口：File
	- 基于网络操作的I/O接口：Socket 

2. InputStreamReader和OutputStreamWriter
	- 数据持久化或者网络传输都是以字节进行的，所以必须有字符->字节和字节->字符的转化
	- InputStreamReader是字节到字符的转化桥梁（InputSream->Reader需要字符集(否则会乱码），完成解码的过程）  
	- OutputStreamWriter是字符到字节的转化桥梁（Writer->OutputStream（根据指定字符集），完成编码的过程）
3. 磁盘I/O工作机制（将数据写到物理磁盘中）

	- 创建File对象
	- 读取/写入数据时，创建StreamDecoder/StreamEncoder对象
	- 创建FileInputStream/FileOutputStream对象
	- 创建FileDescriptor对象关联物理磁盘文件
	- 读取/写入数据
4. Java Socket机制

	- 客户端创建Socket对象, 关联一个InputStream和一个OutputStream对象
	- 服务端创建ServerSocket对象
	- 通过TCP/IP协议读写字节流（三次握手建立连接...）
	- 操作系统分配缓存区暂存数据

5. NIO
	- **Selector**（轮询及调度中心）
	- **Channel**（类比Socket）
	- Buffer(类比Stream)

6. 网络I/O优化
	- 减少网络交互的次数
		- 在网络交互两端设置缓存
		- 合并访问请求
	- 减少网路传输数据量的大小
		- 将数据压缩后再传输
		- 设计简单的协议，协议有包含有用的价值信息
	- 尽量减少编码
		- 网络传输前先将字符转换为字节，避免在传输时将字符转换为字节

	- 根据应用场景选择合适的交互方式
		- 同步阻塞（网页请求，从上到下依次渲染）
		- 同步非阻塞（网页请求，从上到下依次渲染，请求未响应，接着执行下个请求，然后轮询再次请求）
		- 异步阻塞（网页请求，从上到下依次渲染，后面的请求可能先渲染）
		- 异步非阻塞（网页请求，从上到下并发渲染，无先后关系）
