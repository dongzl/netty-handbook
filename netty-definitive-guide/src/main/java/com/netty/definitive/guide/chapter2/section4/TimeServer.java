package com.netty.definitive.guide.chapter2.section4;

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
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        new Thread(timeServer, "AIO-AsyncTimeServerHandler-001").start();
    }
}
