package chapter2.servletcontainer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by huxianyang on 2016/12/5.
 */
public class HttpServer {

	/**
	 * 1.创建ServerSocket绑定到固定地址和端口号
	 * 2.循环阻塞等待客户端请求
	 * 3.创建Request对象并解析请求uri
	 * 4.根据uri选择不同的处理方式
	 * 5.创建Response对象并设置Request属性
	 */
	public void await() {

		int port = 80;
		try (ServerSocket serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"))) {

			try {
				while (Constants.isNotShutDown) {
					Socket socket = serverSocket.accept();

					InputStream input = socket.getInputStream();
					OutputStream output = socket.getOutputStream();


					Request req = new Request(input);
					req.parse();

					String uri = req.getUri();
					Response rsp = new Response(output);
					rsp.setRequest(req);


					if (uri.startsWith("/servlet/")) {

						ServletProcessor servletProcessor = new ServletProcessor();
						servletProcessor.process(req, rsp);
					} else {
						StaticResouceProcessor staticResouceProcessor = new StaticResouceProcessor();
						staticResouceProcessor.process(req, rsp);
					}


					socket.close();
					Constants.isNotShutDown = Constants.SHUTDOWN_COMMAND.equals(uri) ? false : true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}
}
