package com.q.tomcat;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class TomcatServer {
    public static HashMap<String, HttpServlet> servletHashMap;
    public static void main(String[] args) throws Exception {
        //创建一个ServerSocket对象
        ServerSocket serverSocket = new ServerSocket(8090);
        TomcatWebServlet tomcatWebServlet = new TomcatWebServlet();
        servletHashMap = tomcatWebServlet.tomcatservlet();

        while (true){//能够不停接收每个请求
            System.out.println("服务器等待接收");
            //每当有一个请求过来都会创建一个Socket对象，当没有请求会被阻塞在这
            Socket socket = serverSocket.accept();//开启端口
            OutputStream outputStream = socket.getOutputStream();//输出流
            //服务器收到浏览器的请求之后，需要返回202，之后才能读取数据
            String begin="HTTP/1.1 202 Accepted\n" +
                    "Date: Mon, 27 Jul 2009 12:28:53 GMT\n" +
                    "Server: Apache\n";
            outputStream.write(begin.getBytes());
            outputStream.flush();
            //bytes用来存放接收浏览器input接收到的数据
            byte[] bytes = new byte[1024];
            //read 用来记录读取了多少个字节
            int read = 0;
            StringBuilder stringBuilder = new StringBuilder();
            //从输入流中可以读取到浏览器发给我们的消息
            InputStream inputStream = socket.getInputStream();//输入流
            while (true){
                read = inputStream.read(bytes);
                stringBuilder.append(new String(bytes,0,read));
                if (read<1024){
                    break;
                }
            }
            //HttpProtocol 自定义的Http协议类
            HttpProtocol httpProtocol = parseHttpStr(stringBuilder.toString());
            String servletUri = httpProtocol.getServletUri();
            System.out.println(servletUri);
            HttpServlet httpServlet = servletHashMap.get(servletUri);
            if("Get".toUpperCase().equals(httpProtocol.getRequestMethod())){
                httpServlet.doGet();
            }else if ("Post".toUpperCase().equals(httpProtocol.getRequestMethod())){
                httpServlet.doPost();
            }
            String s="<html><head></head><body><h1>HelloWorld</h1></body></html>";
            String str="HTTP/1.1 200 OK\n" +
                    // "Location: http://www.baidu.com\n"+
                    "Date: Mon, 27 Jul 2009 12:28:53 GMT\n" +
                    "Server: Apache\n" +
                    "Last-Modified: Wed, 22 Jul 2009 19:15:56 GMT\n" +
                    "ETag: \"34aa387-d-1568eb00\"\n" +
                    "Accept-Ranges: bytes\n" +
                    "Content-Length: "+s.length()+"\n" +
                    "Vary: Accept-Encoding\n" +
                    "Content-Type: text/html\n"+
                    "\n"+s;
            outputStream.write(str.getBytes());
            inputStream.close();
            outputStream.close();
        }
    }

    /**
     * 解析Http协议，并封装到HTTPProtocol类中
     */
    public static HttpProtocol parseHttpStr(String http){
        HttpProtocol httpProtocol = new HttpProtocol();
        String[] strings = http.split("\n");
        System.out.println(strings);
        for (int i = 0,len=strings.length; i < len; i++) {
            if (i==0) {
                String[] stateLinesParam = strings[0].split(" ");
                httpProtocol.setRequestMethod(stateLinesParam[0]);
                httpProtocol.setUrl(stateLinesParam[1]);
                String[] split = stateLinesParam[1].split("\\?");
                httpProtocol.setServletUri(split[0]);
            }
        }
        return httpProtocol;
    }
}
