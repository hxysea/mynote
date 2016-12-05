package chapter2.servletcontainer;

import java.io.IOException;

/**
 * Created by huxianyang on 2016/12/5.
 */
public class StaticResouceProcessor {
	public void process(Request request, Response response){

		try {
			response.responseStaticRsource();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
