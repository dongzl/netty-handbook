package com.netty.definitive.guide.chapter2.section1;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author dongzonglei
 * @description
 * @date 2019-04-14 21:31
 */
public class TimeServer {

    public static void main(String args[]) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (Exception e) {

            }
        }

        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port:" + port);
            Socket socket = null;
            while (true) {
                socket = server.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }
        } finally {
            if (server != null) {
                System.out.println("The time server close");
                server.close();
                server = null;
            }
        }
    }
}
