package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class server {
    static LinkedList<Socket> list = new LinkedList<>();
    static             int count = 0;
    static class ReadThread extends Thread{
        Socket socket = null;
        ReadThread(Socket socket){
            this.socket = socket;
            list.add(socket);
        }
        @Override
        public void run() {
            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            OutputStream os  = null;
            PrintWriter pw =null;
            try{
                is = socket.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                String info = null;
                while((info=br.readLine())!=null){
                    System.out.println("Client"+socket.getInetAddress()+":"+info);
                }
                socket.shutdownInput();
                os = socket.getOutputStream();
                pw = new PrintWriter(os);
                pw.write("结束通信!");
                pw.flush();
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                System.out.println(socket.getInetAddress()+"线程结束");
                try{
                    if(pw!=null){
                        pw.close();
                    }
                    if(os!=null){
                        os.close();
                    }
                    if(is!=null){
                        is.close();
                    }
                    if(isr!=null){
                        isr.close();
                    }
                    if(socket!=null){
                        socket.close();
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
                count--;
                list.remove(socket);
            }
        }
    }

    static class SendThread extends Thread {

        public SendThread() {
        }

        @Override
        public void run() {
            String serverMessage = null;
            Scanner scanner = new Scanner(System.in);
            String sendMessage = scanner.next();
            while (sendMessage.equals("bye")) {
                try {
                    for (Socket socket : list) {
                        Writer writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
                        writer.write(sendMessage);
                        writer.flush();
                        System.out.println("广播:" + sendMessage);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendMessage = scanner.next();
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
            Socket socket = null;
            System.out.println("服务器启动,等待连接");
            while(true){
                socket = serverSocket.accept();
                ReadThread readThread = new ReadThread(socket);
                readThread.start();
                SendThread sendThread = new SendThread();
                sendThread.start();
                count++;
                System.out.println("已连接数量:"+count);
                InetAddress address = socket.getInetAddress();
                System.out.println("当前client的IP: "+address.getHostAddress());
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }


    }
}
