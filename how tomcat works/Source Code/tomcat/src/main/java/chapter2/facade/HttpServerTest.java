package chapter2.facade;

import chapter2.servletcontainer.HttpServer;

/**
 * Created by huxianyang on 2016/12/5.
 */
public class HttpServerTest {
	public static void main(String[] args){
		chapter2.servletcontainer.HttpServer httpServer = new HttpServer();
		httpServer.await();
	}
}
