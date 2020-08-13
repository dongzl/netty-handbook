package com.netty.code.self.resp;

import com.github.tonivade.resp.RespCallback;
import com.github.tonivade.resp.RespClient;
import com.github.tonivade.resp.protocol.RedisToken;

/**
 * @author dongzonglei
 * @description
 * @date 2019-05-11 09:48
 */
public class RespClientTest {

    public static void main(String args[]) throws Exception {
        RespClient respClient = new RespClient("127.0.0.1", 8080, new RespCallback() {
            @Override
            public void onConnect() {
                System.out.println("onConnect==========");
            }

            @Override
            public void onDisconnect() {
                System.out.println("onDisconnect==========");
            }

            @Override
            public void onMessage(RedisToken redisToken) {
                System.out.println("onMessage==========");
            }
        });
        respClient.start();
        respClient.send("ECHO");
    }
}
