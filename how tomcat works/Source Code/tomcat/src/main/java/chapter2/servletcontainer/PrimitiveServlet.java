package chapter2.servletcontainer;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by huxianyang on 2016/12/5.
 */
public class PrimitiveServlet implements Servlet {
	@Override
	public void init(ServletConfig config) throws ServletException {
		System.out.println("init...");
	}

	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {

		System.out.println("start service ...");
		PrintWriter writer = res.getWriter();
		writer.println("<h1>Hello.This is first word.</h1>");
		writer.print("This is second word.");
		System.out.println("end service...");
	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void destroy() {

		System.out.println("destroy...");
	}
}
