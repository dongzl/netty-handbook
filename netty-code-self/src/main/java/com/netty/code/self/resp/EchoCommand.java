package com.netty.code.self.resp;

import com.github.tonivade.resp.annotation.Command;
import com.github.tonivade.resp.command.Request;
import com.github.tonivade.resp.command.RespCommand;
import com.github.tonivade.resp.protocol.RedisToken;

/**
 * @author dongzonglei
 * @description
 * @date 2019-05-11 09:39
 */
@Command("echo")
public class EchoCommand implements RespCommand {

    @Override
    public RedisToken execute(Request request) {
        return RedisToken.string(request.getParam(0));
    }
}
