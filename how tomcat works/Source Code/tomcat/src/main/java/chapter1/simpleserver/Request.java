package chapter1.simpleserver;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by huxianyang on 2016/11/29.
 */
public class Request {
	private InputStream input;
	private String uri;

	public Request(InputStream input) {
		this.input = input;
	}

	//将请求输入流解析为字符串uri
	public void parse() {
		StringBuilder request = new StringBuilder(2048);

		byte[] buffer = new byte[2048];
		int i = 0;

		try {
			i = input.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			i = -1;
		}

		for (int j = 0; j < i; j++) {
			request.append((char) buffer[j]);
		}

		System.out.println(request.toString());
		setUri(parse2Uri(request.toString()));
	}

	//例request为GET /index.html HTTP/1.1
	private String parse2Uri(String request) {

		int firstSpaceIndex = request.indexOf(' ');
		int secondSpaceIndex = 0;


		if (-1 != firstSpaceIndex) {
			secondSpaceIndex = request.indexOf(' ', firstSpaceIndex + 1);
		}


		if (secondSpaceIndex > firstSpaceIndex) {

			return request.substring(firstSpaceIndex + 1, secondSpaceIndex);
		}
		return null;

	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
