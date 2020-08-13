package com.netty.code.self.resp;

import com.github.tonivade.resp.annotation.Command;
import com.github.tonivade.resp.command.Request;
import com.github.tonivade.resp.command.RespCommand;
import com.github.tonivade.resp.protocol.RedisToken;

/**
 * @author dongzonglei
 * @description
 * @date 2019-05-11 09:37
 */
@Command("ping")
public class PingCommand implements RespCommand {

    @Override
    public RedisToken execute(Request request) {
        return RedisToken.status("PONG");
    }
}
