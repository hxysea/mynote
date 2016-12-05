package chapter1.simpleserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by huxianyang on 2016/11/29.
 */
public class HttpServer {
	private boolean shutDown = false;
	public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";
	private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
	/**
	 * 阻塞等待客户端请求
	 * <p>
	 * 1.创建ServerSocket绑定到服务地址及端口号
	 * 2.阻塞监听绑定端口号
	 * 3.根据请求输入流封装请求
	 * 4.循环等待请求
	 */
	public void await() {
		int port = 8080;
		try (ServerSocket serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"))) {


			while (!shutDown){
				try {
					Socket socket = serverSocket.accept();
					InputStream input = socket.getInputStream();
					OutputStream output = socket.getOutputStream();

					Request req = new Request(input);
					req.parse();

					Response rsp = new Response(output);
					rsp.setRequest(req);
					rsp.sendStaticResponse();

					socket.close();

					shutDown = SHUTDOWN_COMMAND.equals(req.getUri());
				} catch (IOException e) {
					e.printStackTrace();
				}

			}




		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
