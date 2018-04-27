package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    static class ReadThread extends Thread{
        Socket socket = null;
        ReadThread(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            String clientMessage = null;
            try{
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                while(!(clientMessage=br.readLine()).equals("bye")){
                    System.out.println("clientMessage:"+br);
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    static class SendThread extends Thread{
        PrintWriter pw = null;

        SendThread(Socket socket){
            try{
                OutputStream os = socket.getOutputStream();
                pw = new PrintWriter(os);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            try{
                String sendMessage = null;
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                sendMessage = br.readLine();
                while(!sendMessage.equals("bye")){
                    pw.write(sendMessage);
                    pw.flush();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public static void main(String argv[]){
        try{
/**
 * 基于TCP协议的Socket通信，实现用户登录，服务端
 */
//1、创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口
            ServerSocket serverSocket =new ServerSocket(10086);//1024-65535的某个端口
//2、调用accept()方法开始监听，等待客户端的连接
            Socket socket = serverSocket.accept();
            System.out.println(socket);
            ReadThread readThread = new ReadThread(socket);
            SendThread sendThread = new SendThread(socket);
//3、获取输入流，并读取客户端信息
            readThread.start();
            sendThread.start();
            sendThread.join();

//5、关闭资源
            socket.close();
            serverSocket.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }


    }
}
