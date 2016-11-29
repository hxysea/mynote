package chapter1.simpleserver;

/**
 * Created by huxianyang on 2016/11/29.
 */
public class HttpServerTest {
	public static void main(String[] args){

		HttpServer httpServer = new HttpServer();
		httpServer.await();
	}
}
