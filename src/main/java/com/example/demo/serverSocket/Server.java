package com.example.demo.serverSocket;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 网络编程---模拟服务端
 */
public class Server {

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(2000);
        System.out.println("服务器准备就绪----");
        System.out.println(
                "服务器信息：" + serverSocket.getInetAddress() + "p:" + serverSocket.getLocalPort());
        for (; ; ) {
            // 等待客户端连接
            Socket client = serverSocket.accept();
            // 客户端构建异步线程
            ClientHandler clientHandler = new ClientHandler(client);
            // 启动线程
            clientHandler.start();
        }

        // todo(client);
    }

    /** 客户端消息处理 */
    private static class ClientHandler extends Thread {

        private Socket socket;
        private boolean flag = true;

        ClientHandler(Socket socket) {

            this.socket = socket;
        }
        @Override
        public void run() {
            super.run();
            System.out.println("客户端连接：" + socket.getInetAddress() + "p:" + socket.getPort());

            try {
                // 得到打印流，用于服务器输出；服务端回送数据
                PrintStream socketOutPut = new PrintStream(socket.getOutputStream());

                // 得到输入流
                BufferedReader socketInput =
                        new BufferedReader(new InputStreamReader(socket.getInputStream()));

                do {
                    // 从客户端拿到一条数据
                    String str = socketInput.readLine();
                    if ("bye".equalsIgnoreCase(str)) {
                        flag = false;
                        // 回送
                        socketOutPut.println("bye");

                    } else {
                        // 打印到屏幕并回送数据长度
                        System.out.println(str);
                        socketOutPut.println("回送：" + str.length());
                    }

                } while (flag);

                socketInput.close();
                socketOutPut.close();
            } catch (Exception e) {
                System.out.println("连接异常断开");
            } finally {
                // 连接关闭
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("客户端关闭" + socket.getInetAddress() + "p" + socket.getPort());
        }
    }
}