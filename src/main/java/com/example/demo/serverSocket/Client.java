package com.example.demo.serverSocket;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 网络编程--模拟客户端
 */
public class Client {

    public static void main(String[] args) throws IOException {
        // 创建socket
        Socket socket = new Socket();
        // 设置超时时间
        socket.setSoTimeout(3000);

        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 2000));

        System.out.println("已经发起服务器连接，并进入后续流程");
        System.out.println("客户端信息：" + socket.getLocalAddress() + "p:" + socket.getLocalPort());
        System.out.println("服务端信息：" + socket.getInetAddress() + "P" + socket.getPort());

        try {
            todo(socket);
        } catch (Exception e) {
            System.out.println("异常关闭");
        }
        // 释放资源
        socket.close();
        System.out.println("客户端退出");
    }

    private static void todo(Socket client) throws IOException {
        // 构建键盘输入流
        InputStream in = System.in;
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        // 得到socket输出流，并且转化为打印流
        OutputStream outputStream = client.getOutputStream();
        PrintStream socketPrintStream = new PrintStream(outputStream);
        // 得到socket输入流,并转化为bufferedReader
        InputStream inputStream = client.getInputStream();
        BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        boolean flag = true;

        do {
            // 键盘读取一行
            String str = input.readLine();
            // 发送到服务器
            socketPrintStream.println(str);

            // 从服务器读取一行
            String echo = socketBufferedReader.readLine();
            if ("bye".equalsIgnoreCase(echo)) {
                flag = false;
            } else {
                System.out.println(echo);
            }
        } while (flag);
        // 资源释放
        socketPrintStream.close();
        socketBufferedReader.close();
    }
}
