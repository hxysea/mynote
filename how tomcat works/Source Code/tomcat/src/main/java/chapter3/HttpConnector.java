package chapter3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by huxianyang on 2016/12/24.
 */
public class HttpConnector implements Runnable {
	private String schame = "http";
	private boolean isStopped;

	private boolean isNotStopped;

	public String getSchame() {
		return schame;
	}

	public boolean isStopped() {
		return isStopped;
	}

	public void setIsStopped(boolean isStopped) {
		this.isStopped = isStopped;
	}

	public boolean isNotStopped() {
		return !isStopped;
	}

	@Override
	public void run() {

		ServerSocket serverSocket = null;
		int port = 80;
		try {
			serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		while (isNotStopped) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

			HttpProcessor httpProcessor = new HttpProcessor(this);
			httpProcessor.process(socket);

		}
	}

	public void start() {
		Thread httpConnector = new Thread(this);
		httpConnector.start();
	}
}
