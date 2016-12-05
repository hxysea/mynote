package chapter2.servletcontainer;

/**
 * Created by huxianyang on 2016/12/5.
 */
public class HttpServerTest {
	public static void main(String[] args){
		HttpServer httpServer = new HttpServer();
		httpServer.await();
	}
}
