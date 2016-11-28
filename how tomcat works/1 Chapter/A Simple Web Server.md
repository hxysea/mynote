
 A Simple Web Server
====================

1. 两个重要的类
	- java.net.Socket
	- java.net.ServerSocket

2.  Http Request
	- 请求行	（Method-URI-Protocal/Version）,如：POST /examples/default.jsp HTTP/1.1
	- 请求头
	- 空行
	- 请求体
	- 请求示例报文 	

		POST /examples/default.jsp HTTP/1.1
		Accept: text/plain; text/html
		Accept-Language: en-gb
		Connection: Keep-Alive
		Host: localhost
		User-Agent: Mozilla/4.0 (compatible; MSIE 4.01; Windows 98)
		Content-Length: 33
		Content-Type: application/x-www-form-urlencoded
		Accept-Encoding: gzip, deflate
		**空行**
		lastName=Franks&firstName=Michael

3. Http Response

	- 响应行（Protocal-Status code-Description），如：HTTP/1.1 200 OK
	- 响应头
	- 空行
	- 响应体
	- 响应报文

	HTTP/1.1 200 OK
	Server: Microsoft-IIS/4.0
	Date: Mon, 5 Jan 2004 13:13:33 GMT
	Content-Type: text/html
	Last-Modified: Mon, 5 Jan 2004 13:13:12 GMT
	Content-Length: 112
	**空行**
	```
	<html>
	<head>
	<title>HTTP Response Example</title>
	</head>
	<body>
		Welcome to Brainy Software
	</body>
	</html>
	
	```
4. Socket Class(客户端，请求资源)

	- 代码片段

	```java
	 Socket socket = new Socket("127.0.0.1", "8080");
	 OutputStream os = socket.getOutputStream();
	 boolean autoflush = true;
	 PrintWriter out = new PrintWriter(
	 socket.getOutputStream(), autoflush);
	 BufferedReader in = new BufferedReader(
	 new InputStreamReader( socket.getInputstream() ));

	 // send an HTTP request to the web server
	 out.println("GET /index.jsp HTTP/1.1");
	 out.println("Host: localhost:8080");
	 out.println("Connection: Close");
	 out.println();

	 // read the response
	 boolean loop = true;
	 StringBuilder sb = new StringBuilder(8096);

	 while (loop) {
		 if ( in.ready() ) {
			 int i=0;
			 while (i!=-1) {
				 i = in.read();
				 sb.append((char) i);
			 }
			 loop = false;
		 }
		 Thread.currentThread() .sleep(50);
	 }
	 // display the response to the out console
	 System.out.println(sb.toString());
	 socket.close();
	
	``` 	

5. ServerSocket Class （服务端，响应资源）

	```java
	public ServerSocket(int port, int backLog, InetAddress bindingAddress);
	//例如：
	new ServerSocket(8080, 1, InetAddress.getByName("127.0.0.1"));
	```

6. The Application
	- HttpServer Class

	```java
	import java.net.Socket;
	import java.net.ServerSocket;
	import java.net.InetAddress;
	import java.io.InputStream;
	import java.io.OutputStream;
	import java.io.IOException;
	import java.io.File;
	public class HttpServer {

		 /** WEB_ROOT is the directory where our HTML and other files reside.
		 * For this package, WEB_ROOT is the "webroot" directory under the
		 * working directory.
		 * The working directory is the location in the file system
		 * from where the java command was invoked.
		 */
		 public static final String WEB_ROOT =
		 System.getProperty("user.dir") + File.separator + "webroot";

		 // shutdown command
		 private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

		 // the shutdown command received
		 private boolean shutdown = false;

		 public static void main(String[] args) {
			 HttpServer server = new HttpServer();
			 server.await();
		 }
		 public void await() {
				 ServerSocket serverSocket = null;
				 int port = 8080;
	
				 try {
					 serverSocket = new ServerSocket(port, 1,
					 InetAddress.getByName("127.0.0.1"));
				 }
				 catch (IOException e) {
					 e.printStackTrace();
					 System.exit(1);
				 }
	
				 // Loop waiting for a request
				 while (!shutdown) {
					 Socket socket = null;
					 InputStream input = null;
					 OutputStream output = null;

				 try {
					 socket = serverSocket.accept();
					 input = socket.getInputStream();
					 output = socket.getOutputStream();

					 // create Request object and parse
					 Request request = new Request(input);
					  request.parse();

					 // create Response object
					 Response response = new Response(output);
					 response.setRequest(request);
					 response.sendStaticResource();

					 // Close the socket
					 socket.close();

					 //check if the previous URI is a shutdown command
					 shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
				 }
				 catch (Exception e) {
					 e.printStackTrace ();
					 continue;
				 }
			 }
		 }
	}
	```	
	- Request Class

	```java
	import java.io.InputStream;
	import java.io.IOException;

	public class Request {
		 private InputStream input;
		 private String uri;

		 public Request(InputStream input) {
		 	this.input = input;
		 }

		 public void parse() {
		 	 // Read a set of characters from the socket
			 StringBuilder request = new StringBuilder(2048);
			 int i;
			 byte[] buffer = new byte[2048];
			 try {
			 i = input.read(buffer);
			 }
			 catch (IOException e) {
				 e.printStackTrace();
				 i = -1;
			 }
			 for (int j=0; j<i; j++) {
				 request.append((char) buffer[j]);
			 }

			 System.out.print(request.toString());
 			 uri = parseUri(request.toString());
		 }

		 //例：GET /index.html HTTP/1.1
		 private String parseUri(String requestString) {
			 int index1, index2;
			 index1 = requestString.indexOf(' ');

			 if (index1 != -1) {
				 index2 = requestString.indexOf(' ', index1 + 1);

			 if (index2 > index1)
				 return requestString.substring(index1 + 1, index2);
			 }
			 return null;
		 }

		 public String getUri() {
		 	return uri;
		 }
	}
	```
	- Response Class

	```java
	
	import java.io.OutputStream;
	import java.io.IOException;
	import java.io.FileInputStream;
	import java.io.File;
	/*
	 HTTP Response = Status-Line
	 *(( general-header | response-header | entity-header ) CRLF)
	 CRLF
	 [ message-body ]
	 Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
	*/
	public class Response {
		 private static final int BUFFER_SIZE = 1024;
		 Request request;
		 OutputStream output;
		 public Response(OutputStream output) {
			 this.output = output;
		 }
		 public void setRequest(Request request) {
			 this.request = request;
		 }
		 public void sendStaticResource() throws IOException {
			 byte[] bytes = new byte[BUFFER_SIZE];
			 FileInputStream fis = null;
			 try {
				 File file = new File(HttpServer.WEB_ROOT, request.getUri());
				 if (file.exists()) {
						 fis = new FileInputStream(file);
						 int ch = fis.read(bytes, 0, BUFFER_SIZE);
						 while (ch!=-1) {
							 output.write(bytes, 0, ch);
							 ch = fis.read(bytes, 0, BUFFER_SIZE);
						 }
			  	 }
				 else {
						 // file not found
						 String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
						 "Content-Type: text/html\r\n" +
						 "Content-Length: 23\r\n" +
						 "\r\n" +
						 "<h1>File Not Found</h1>";
						 output.write(errorMessage.getBytes());
				 }
		 }
		 catch (Exception e) {
				 // thrown if cannot instantiate a File object
				 System.out.println(e.toString() );
			 }
		 finally {
				 if (fis!=null)
				 fis.close();
			}
		 }
	}
	```

7.  Summary

	- HttpServer接收请求
	- 根据请求构造Request对象，解析到uri
	- 根据请求和Request对象构造Response对象，根据Request对象的uri响应客户端请求资源





