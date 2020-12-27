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
                    // �ȴ��ͻ��˵����󣬲���������
                    Socket socket = serverSocket.accept();

                    System.out.println(socket.getLocalAddress() +"<--" + socket.getRemoteSocketAddress() );

                    //��ȡHTTP������Ϣ
                    InputStream inFromClient = socket.getInputStream();
                    Thread.sleep(500);
                    int size = inFromClient.available();
                    byte[] reqBuf = new byte[size];

                    inFromClient.read(reqBuf);

                    String request = new String(reqBuf);

                    System.out.println("inFromClient: \n" + request);


                    /*����HTTP����*/
                    //���HTTP����ĵ�һ��
                    int endIndex=request.indexOf("\r\n");
                    if(endIndex==-1)
                        endIndex=request.length();
                    String firstLineOfRequest=
                            request.substring(0,endIndex);

                    //����HTTP����ĵ�һ��
                    String[] parts=firstLineOfRequest.split(" ");
                    String uri="";
                    if(parts.length>=2)
                        uri=parts[1]; //���HTTP�����е�uri


                    /*����HTTP��Ӧ���ĵ�����*/
                    String contentType;
                    if(uri.indexOf("html")!=-1 || uri.indexOf("htm")!=-1)
                        contentType="text/html";
                    else if(uri.indexOf("jpg")!=-1 || uri.indexOf("jpeg")!=-1)
                        contentType="image/jpeg";
                    else if(uri.indexOf("gif")!=-1)
                        contentType="image/gif";
                    else
                        contentType="application/octet-stream";

                    /*����HTTP��Ӧ��� */
                    //HTTP��Ӧ�ĵ�һ��
                    String responseFirstLine="HTTP/1.1 200 OK\r\n";
                    //HTTP��Ӧͷ
                    String responseHeader="Content-Type:"+contentType+"\r\n\r\n";
                    //��ö�ȡ��Ӧ�������ݵ�������
                    InputStream in = HttpServer.class.getResourceAsStream("root/"+uri);


                    //����HTTP��Ӧ���
                    OutputStream outToClient = socket.getOutputStream();//��������
                    //����HTTP��Ӧ�ĵ�һ��
                    outToClient.write(responseFirstLine.toString().getBytes());
                    //����HTTP��Ӧ��ͷ
                    outToClient.write(responseHeader.toString().getBytes());
                    //����HTTP��Ӧ������
                    int len = 0;
                    byte [] buffer = new byte[128];
                    while ((len = in.read(buffer)) != 1){
                        outToClient.write(buffer, 0, len);
                    }

                    Thread.sleep(1000);
                    socket.close();//�ر�TCP����

                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

            }
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }

    }

}
