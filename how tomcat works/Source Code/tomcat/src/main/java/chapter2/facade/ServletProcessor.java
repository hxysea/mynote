package chapter2.facade;

import chapter2.servletcontainer.Constants;
import chapter2.servletcontainer.Request;
import chapter2.servletcontainer.Response;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * Created by huxianyang on 2016/12/5.
 */
public class ServletProcessor {
	/**
	 * 1.从request中获取servlet名称
	 * 2.使用反射加载该Servlet类
	 * 3.执行service方法
	 * @param request
	 * @param response
	 */
	public void process(Request request, Response response){

		try {
			String uri = request.getUri();
			String servletName = uri.substring(uri.lastIndexOf("/") + 1);

			File classPath = new File(Constants.WEB_ROOT);
			String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();

			URL[] urls = new URL[1];
			URLStreamHandler streamHandler = null;
			urls[0] = new URL(null , repository, streamHandler);

			ClassLoader loader = new URLClassLoader(urls);
			Class<?> myClass = loader.loadClass("chapter2." + servletName);
			Servlet servlet = (Servlet)myClass.newInstance();

			RequestFacade requestFacade = new RequestFacade(request);
			ResponseFacade responseFacade = new ResponseFacade(response);
			servlet.service((ServletRequest)requestFacade, (ServletResponse)responseFacade);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}
}
