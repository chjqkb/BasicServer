package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    public static void main(String[] args) {
        int port = 8080;
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);


            System.out.println("Server Listening: " + serverSocket.getLocalPort());


            while (true) {
                try{
                    // 等待客户端的请求，并建立连接
                    Socket socket = serverSocket.accept();

                    System.out.println(socket.getLocalAddress() +"<--" + socket.getRemoteSocketAddress() );

                    //读取HTTP请求信息
                    InputStream inFromClient = socket.getInputStream();
                    Thread.sleep(500);
                    int size = inFromClient.available();
                    byte[] reqBuf = new byte[size];

                    inFromClient.read(reqBuf);

                    String request = new String(reqBuf);

                    System.out.println("inFromClient: \n" + request);


                    /*解析HTTP请求*/
                    //获得HTTP请求的第一行
                    int endIndex=request.indexOf("\r\n");
                    if(endIndex==-1)
                        endIndex=request.length();
                    String firstLineOfRequest=
                            request.substring(0,endIndex);

                    //解析HTTP请求的第一行
                    String[] parts=firstLineOfRequest.split(" ");
                    String uri="";
                    if(parts.length>=2)
                        uri=parts[1]; //获得HTTP请求中的uri


                    /*决定HTTP响应正文的类型*/
                    String contentType;
                    if(uri.indexOf("html")!=-1 || uri.indexOf("htm")!=-1)
                        contentType="text/html";
                    else if(uri.indexOf("jpg")!=-1 || uri.indexOf("jpeg")!=-1)
                        contentType="image/jpeg";
                    else if(uri.indexOf("gif")!=-1)
                        contentType="image/gif";
                    else
                        contentType="application/octet-stream";

                    /*创建HTTP响应结果 */
                    //HTTP响应的第一行
                    String responseFirstLine="HTTP/1.1 200 OK\r\n";
                    //HTTP响应头
                    String responseHeader="Content-Type:"+contentType+"\r\n\r\n";
                    //获得读取响应正文数据的输入流
                    InputStream in = HttpServer.class.getResourceAsStream("root/"+uri);


                    //发送HTTP响应结果
                    OutputStream outToClient = socket.getOutputStream();//获得输出流
                    //发送HTTP响应的第一行
                    outToClient.write(responseFirstLine.toString().getBytes());
                    //发送HTTP响应的头
                    outToClient.write(responseHeader.toString().getBytes());
                    //发送HTTP响应的正文
                    int len = 0;
                    byte [] buffer = new byte[128];
                    while ((len = in.read(buffer)) != 1){
                        outToClient.write(buffer, 0, len);
                    }

                    Thread.sleep(1000);
                    socket.close();//关闭TCP连接

                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

            }
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }

    }

}
