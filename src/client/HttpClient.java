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
            //�����������ݿ�
            //String sb = "Hello Server.";

            /*����HTTP���� */
            StringBuffer sb=new StringBuffer("GET "+uri+" HTTP/1.1\r\n");
            sb.append("Accept: */*\r\n");
            sb.append("Accept-Language: zh-cn\r\n");
            sb.append("Accept-Encoding: gzip, deflate\r\n");
            sb.append("User-Agent: HTTPClient\r\n");
            sb.append("Host: localhost:8080\r\n");
            sb.append("Connection: Keep-Alive\r\n\r\n");
            OutputStream outToServer = socket.getOutputStream(); //��������

            //�������󵽷�����
            outToServer.write(sb.toString().getBytes());

            Thread.sleep(2000); //˯��2�룬�ȴ���Ӧ���

            //������Ϣ
            InputStream inFromServer = socket.getInputStream();
            int size = inFromServer.available();
            byte[] buffer = new byte[size];
            inFromServer.read(buffer);

            System.out.println(new String(buffer)); //��ӡ��Ӧ���



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
