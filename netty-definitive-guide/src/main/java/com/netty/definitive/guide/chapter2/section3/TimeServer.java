package com.netty.definitive.guide.chapter2.section3;

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
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
    }
}
