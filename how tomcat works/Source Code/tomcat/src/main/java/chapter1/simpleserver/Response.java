package chapter1.simpleserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by huxianyang on 2016/11/29.
 */
public class Response {
	private Request request;
	private OutputStream output;

	private static final int BUFFER_SIZE = 1024;

	public Response(OutputStream output) {
		this.output = output;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	/**
	 * 1.从request中获取资源地址
	 * 2.读取请求资源文件
	 * 3.将资源以流的形式写回给客户端
	 */
	public void sendStaticResponse() {

		byte[] buffer = new byte[BUFFER_SIZE];
		FileInputStream fis = null;
		try {
			File file = new File(HttpServer.WEB_ROOT, request.getUri());

			if(file.exists()){

				fis = new FileInputStream(file);
				int byteCount = fis.read(buffer, 0, BUFFER_SIZE);
				while (-1 != byteCount){
					output.write(buffer, 0 , byteCount);
					byteCount = fis.read(buffer, 0, BUFFER_SIZE);
				}
			}else{
				String errorMessage =  "HTTP/1.1 404 File Not Found\r\n" +
						"Content-Type: text/html\r\n" +
						"Content-Length: 23\r\n" +
						"\r\n" +
						"<h1>File Not Found</h1>";

				output.write(errorMessage.getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fis){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
