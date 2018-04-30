package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class client1 {
    static String ip = "192.168.1.104";
    static class ReadThread extends Thread{
        Socket socket = null;
        ReadThread(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            String serverMessage = null;
            try{
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                while(!(serverMessage=br.readLine()).equals("bye")){
                    System.out.println("server:"+serverMessage);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    static class SendMessage extends Thread{
        Socket socket = null;
        SendMessage(Socket socket){
            try{
                this.socket = socket;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            String sendMessage = null;


        }
    }
    public static void main(String argv[]){
        try {
            //1、创建clientSocket，指定server端口号和地址
            Socket socket = new Socket(ip,10086);
            Writer writer=null;
            //2、获取输出流,向server发送信息
            try{
                // 建立连接后就可以往服务端写数据了
                if(writer == null) {
                    writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
                }
                Scanner scanner = new Scanner(System.in);
                String msg =  scanner.next();
                while(!msg.equals("bye")){
                    writer.write(msg);
                    writer.write("\n");
                    writer.flush();// 写完后要记得flush
                    System.out.println("Cliect[port:" + socket.getLocalPort() + "] 消息发送成功");
                    msg = scanner.next();
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try{
                    socket.shutdownOutput(); //关闭输出流
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String info = null;
            //循环读取
            while ((info = br.readLine()) != null){
                System.out.println("我是client:server说:" + info);
            }
            br.close();

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
