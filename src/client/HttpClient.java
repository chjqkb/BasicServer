package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class HttpClient {

    public static void main(String [] args) {

        String hostname = "localhost";
        int port = 8080;
        Socket socket = null;
        try {
            socket = new Socket(hostname, port);

            System.out.println(socket.getRemoteSocketAddress() + "<--" + socket.getLocalAddress());


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try{
            String uri = "index.htm";
            //创建发送数据块
            //String sb = "Hello Server.";

            /*创建HTTP请求 */
            StringBuffer sb=new StringBuffer("GET "+uri+" HTTP/1.1\r\n");
            sb.append("Accept: */*\r\n");
            sb.append("Accept-Language: zh-cn\r\n");
            sb.append("Accept-Encoding: gzip, deflate\r\n");
            sb.append("User-Agent: HTTPClient\r\n");
            sb.append("Host: localhost:8080\r\n");
            sb.append("Connection: Keep-Alive\r\n\r\n");
            OutputStream outToServer = socket.getOutputStream(); //获得输出流

            //发送请求到服务器
            outToServer.write(sb.toString().getBytes());

            Thread.sleep(2000); //睡眠2秒，等待响应结果

            //接收信息
            InputStream inFromServer = socket.getInputStream();
            int size = inFromServer.available();
            byte[] buffer = new byte[size];
            inFromServer.read(buffer);

            System.out.println(new String(buffer)); //打印响应结果



        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            try {
                socket.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }
}
