### Netty 连接报错：

**Problem：**

java.io.IOException: Connection reset by peer

```java
    at sun.nio.ch.FileDispatcherImpl.read0(Native Method)
    at sun.nio.ch.SocketDispatcher.read(SocketDispatcher.java:39)
    at sun.nio.ch.IOUtil.readIntoNativeBuffer(IOUtil.java:223)
    at sun.nio.ch.IOUtil.read(IOUtil.java:192)
    at sun.nio.ch.SocketChannelImpl.read(SocketChannelImpl.java:380)
    at io.netty.buffer.PooledUnsafeDirectByteBuf.setBytes(PooledUnsafeDirectByteBuf.java:288)
    at io.netty.buffer.AbstractByteBuf.writeBytes(AbstractByteBuf.java:1108)
    at io.netty.channel.socket.nio.NioSocketChannel.doReadBytes(NioSocketChannel.java:345)
    at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:126)
    at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:645)
    at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:580)
    at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:497)
    at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:459)
    at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:858)
```
**Answer：**

- 因为 connect 成功以后, client 段先会触发 connect 成功的 listener
- 这个时候 server 段虽然断开了 channel, 也触发 channel 断开的事件 (它会触发一个客户端 read 事件, 但是这个 read 会返回 -1, -1 代表 channel 关闭, client 的 channelInactive 跟 channel  active 状态的改变都是在这时发生的),
- 但是这个事件是在 connect 成功的 listener 之后执行, 所以这个时候 listener 里的 channel 并不知道自己已经断开, 它还是会继续进行 write 跟 flush 操作, 在调用 flush 后, eventloop 会进入 OP_READ 事件里, 这时候 unsafe.read() 就会抛出 connection reset 异常.

**Problem:**

由服务端造成的 ClosedChannelException

**Answer:**

在 write 之前主动调用了 close, 那么 write 必然会知道 是 close 状态, 最后 write 就会失败, 并且 future 里的 cause 就是 ClosedChannelException







