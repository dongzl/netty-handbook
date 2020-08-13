package com.netty.code.self.resp;

import com.github.tonivade.resp.RespServer;
import com.github.tonivade.resp.command.CommandSuite;

/**
 * @author dongzonglei
 * @description
 * @date 2019-05-11 09:30
 */
public class RespServerTest {

    public static void main(String args[]) throws Exception {
        RespServer respServer = RespServer.builder().host("localhost").port(8080).commands(new CommandSuite()).build();
        respServer.start();
    }
}
