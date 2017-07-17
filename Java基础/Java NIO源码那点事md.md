----------
6/15/2017 9:33:06 AM 

----------
## Java NIO那点事 ##
1. 什么是NIO？
	- New Input/Output
	- 基于通道和缓冲区的I/O方式
	- 可使用Native函数库直接分配堆外内存
	- 通过存储于Java堆的DirectByteBuffer对象直接操作已分配的堆外内存
	- 同步非阻塞模型
		- 同步是指不间断轮询IO是否就绪
			- 核心是Selector
				- Selector代替了线程本身轮询IO事件，避免了阻塞，同时避免了不必要的线程消耗（如线程等待）
		- 非阻塞是指，等待IO过程中，可以同时做其他任务
			- 核心是通道和缓冲区
				- IO就绪时，通过通道写入缓冲区保证IO成功，无需阻塞等待
	- 与传统IO区别
		- 传统IO面向流，NIO面向缓冲区
		- 传统IO是阻塞式的，NIO是非阻塞的 
2. 几个重要概念
	- Buffer 缓冲区
		- ByteBuffer
		- CharBuffer
		- DoubleBuffer
		- FloatBuffer
		- IntBuffer
		- LongBuffer
		- ShortBuffer
	- Channel 通道
		- SocketChannel
		- ServerSocketChannel
		- FileChannel
		- DatagramChannel
	- Selector 选择器
		- 监管IO事件
		- Channel注册到Selector
		- 单线程处理多个Channel

3. 实例说事（干货）

- FileChannel


```java
package com.abuge.nio.demo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by huxianyang on 2017/6/15.
 */
public class FileChannelDemo {
    public static void main(String[] args) {
        normalIOReadFile();

        nioReadFile();
    }

    private static void normalIOReadFile() {

        System.out.println("-----------------start to read file by normal IO-----------------");
        try (InputStream in = new BufferedInputStream(new FileInputStream("src/com/abuge/nio/resource/normal_io.txt"))) {
            byte[] buffer = new byte[1024];
            int byteNum = in.read(buffer);
            while (-1 != byteNum) {
                for (int i = 0; i < byteNum; i++) {
                    System.out.print((char) buffer[i]);
                }
                byteNum = in.read(buffer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-----------------end to read file by normal IO-----------------");
    }

    private static void nioReadFile() {
        System.out.println("-----------------start to read file by NIO-----------------");

        try(FileChannel fileChannel = new FileInputStream("src/com/abuge/nio/resource/nio.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int byteReadNum = fileChannel.read(buffer);
            System.out.println("read file byte is " + byteReadNum);

            while(-1 != byteReadNum){
                buffer.flip();
                while (buffer.hasRemaining()){
                    System.out.print((char)buffer.get());
                }
                buffer.compact();
                byteReadNum = fileChannel.read(buffer);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("-----------------end to read file by NIO-----------------");

    }

}

```

- SocketChannel
	- 客户端使用NIO，使用非阻塞模式发送数据
	- 服务端使用IO，阻塞接收数据
	
```java
package com.abuge.nio.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * Created by huxianyang on 2017/6/16.
 */
public class SocketChannelDemo {
    public static void main(String[] args) {

        sendByNIO();
    }


    private static void sendByNIO() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.configureBlocking(false);
            System.out.println("------------- start to connect server -------------");
            boolean connect = socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
            System.out.println("--------------connect is " + connect + "------------");
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            if (socketChannel.finishConnect()) {
                System.out.println("------------- connect successful -------------");
                int i = 0;
                while (true) {
                    TimeUnit.SECONDS.sleep(1);
                    String info = "I'm " + (i++) + "-th information from client.";

                    buffer.clear();
                    buffer.put(info.getBytes());
                    buffer.flip();

                    while (buffer.hasRemaining()) {
                        System.out.println("client send " + buffer);
                        socketChannel.write(buffer);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


package com.abuge.nio.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by huxianyang on 2017/6/16.
 */
public class ServerSocketDemo {
    public static void main(String[] args){
        acceptByIO();
    }

    private static void acceptByIO() {
        InputStream in = null;
        try (ServerSocket serverSocket = new ServerSocket(8080);) {
            byte[] buffer = new byte[1024];
            int recvMsgSize = 0;
            while (true) {
                System.out.println("------------- start to receive msg -------------");
                Socket clientSocket = serverSocket.accept();
                SocketAddress clientAddress = clientSocket.getRemoteSocketAddress();
                System.out.println("handling client at " + clientAddress);

                in = clientSocket.getInputStream();

                while (-1 != (recvMsgSize = in.read(buffer))) {
                    byte[] tmp = new byte[recvMsgSize];
                    System.arraycopy(buffer, 0, tmp, 0, recvMsgSize);
                    System.out.println(new String(buffer));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

```
- 服务端的NIO写法
	- ServerSocketChannel
	- Selector

```java
package com.abuge.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * Created by huxianyang on 2017/6/19.
 */
public class ServerConnect {
    private static final int PORT = 8090;
    private static final int TIMEOUT = 3000;
    private static final int BUF_SIZE = 1024;

    public static void main(String[] arsg) {

        selector();
    }

    private static void selector() {
        System.out.println("-------------------start to execute select-------------------");

        try (Selector selector = Selector.open(); ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();) {
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//表示此通道关注的是连接事件，有连接事件时，selector触发通道执行操作


            while (true) {
                if (0 == selector.select(TIMEOUT)) {
                    System.out.println("============there is no channel is ready to execute IO operations============");
                    continue;
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                int i = 0;
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isAcceptable()) {//表示此key对应的通道可以接收连接
                        System.out.println("============the key represent channel is acceptable============ " + i);

                        handleAccept(key);//将通道置为了关注读就绪事件
                    }

                    if (key.isReadable()) {//表示此key对应的通道可以接收读数据
                        System.out.println("============the key represent channel is readable============" + (i++));
                        handleRead(key);
                    }

                    if (key.isWritable()) {//表示此key对应的通道可以接收读数据
                        System.out.println("============the key represent channel is writable============");
                        handleWrite(key);
                    }
                    if (key.isConnectable()) {//表示连接就绪，通常用于client端请求建立连接时
                        System.out.println("============the key represent channel is connectable============");
                    }

                    iterator.remove();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("-------------------end to execute select-------------------");
    }

    private static void handleWrite(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();
        buf.flip();

        while (buf.hasRemaining()) {
            socketChannel.write(buf);
            System.out.print((char) buf.get());
        }

        buf.compact();
    }

    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();
        int bytesRead = socketChannel.read(buf);

        while (0 < bytesRead) {
            buf.flip();
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }
            System.out.println("====================================");
            buf.clear();
            bytesRead = socketChannel.read(buf);

            if (-1 == bytesRead) {
                socketChannel.close();
            }
        }
    }

    private static void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketcChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketcChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(BUF_SIZE));
    }


}

```


**PS:Idea常用快捷键**
- Ctrl + Q 查看注释
- Ctrl + N 查找类
- 双击Shift 查找Everything