## 解密1 Netty是什么

- 一句话

  Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers & clients.换句话说，Netty是用于**快速开发**可维护的**高性能协议服务器和客户端**的**异步事件驱动**网络应用**框架**。

- 一张图

   ![components](资料\components.png)

  进入[http://netty.io/](http://netty.io/)首页，上图即映入眼帘。这张图涵盖了Netty的基本特性：

  - Protocol Support
    - 支持HTTP、WebSocket、Protobuf等主流协议
  - Transport Services
    - 支持Socket、Datagram等传输方式
  - Core
    - 扩展的事件驱动模型
      - 支持明确的关注点分离
    - 统一的通信API
      - 同时支持阻塞和非阻塞方式
    - 最小化不必要的内存Copy

  基于以上特性，Netty可以提供更大的吞吐量和更低的延迟以及更小的资源消耗的通信性能。此外，Netty还完整支持SSL和Start TLS。

## 解密2 Netty线程模型

- **Reactor模型**

  - 单线程模型

   ![0002_Reactor单线程模型](资料\0002_Reactor单线程模型.png)

  - 多线程模型




## 解密3 Netty的4个核心组件

Netty的核心组件包括以下4个部分：

- Channels
- Callbacks
- Futures
- Events and Handlers

这些组件用来表示应用中的不同构造类型：资源、逻辑、通知。我们的应用中将会使用这些组件来访问网络并让数据在其中流转。下面我们就逐一介绍一下这些组件并附上一些小demo：

- Channels

  - Channel是Java NIO的基本组成元素,用来表示和硬件设备、文件、网络Socket或者可以操作IO（读写数据）的程序组件等实体的一个连接
  - 不妨将Channel看作一个数据输入/输出的工具，可以打开/关闭，连接/断开连接

- Callbacks

  - 一个callback标识一个方法，即提供一个方法引用给另一个方法。回调机制可以让后者在合适的时机调用前者。回调机制尤其适用在某个操作完成时通知另一方。
  - Netty在处理事件时采用了回调机制，回调触发时，netty内部将事件交由ChannelHandler接口一个的实现来处理。下面看一个确立连接时的demo：

  ```java
  public class ConnectHandler extends ChannelInboundHandlerAdapter {
      @Override
      public void channelActive(ChannelHandlerContext ctx) {
          System.out.println("Client " + ctx.channel().remoteAddress() + "connected");
      }
  }
  ```

  ​				 ![0003_ConnectHandler类图](资料\0003_ConnectHandler类图.png)


- Futures

  - Netty是一个完全的异步和事件驱动的框架
  - ChannelFuture允许注册一个或多个ChannelFutureListener实例，用于某个操作完成时实现事件通知

- Events and handlers

  - 事件可以被一系列handler处理
  - 处理Business Logic ![0004_Handlers](资料\0004_Handlers.png)



- Selector

  - EventLoopGroup(线程池)

     				![0005_NioEventLoopGroup](资料\0005_NioEventLoopGroup.png)


- @Sharable

## 解密4 Netty的几个官方Demo

## 解密5 Netty源码赏析

- **Channel**
  - 封装socket操作

- **ChannelFuture**
- **EventLoopGroup**
- **ChannelHandler**
- **BootStrap**用于客户端启动一个Channel的工厂类
  - **bind()** 用于UDP连接
  - **connect()** 用于TCP连接
- **特性**
  - **Zero Copy**
  - **Epoll(Linux)**
    - 可扩展的I/O事件通知机制



## 解密6 综合实战——开发基于Netty的Http Server