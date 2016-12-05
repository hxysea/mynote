package chapter2.servletcontainer;

import java.io.File;

/**
 * Created by huxianyang on 2016/12/5.
 */
public class Constants {
	public static boolean isNotShutDown = true;
	public static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
	public static final int BUFFER_SIZE = 1024;
	public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";
}
