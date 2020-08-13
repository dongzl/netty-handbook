package com.netty.definitive.guide.chapter8;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

/**
 * @author dongzonglei
 * @description
 * @date 2019-04-30 18:46
 */
public class TestSubscribeReqProto {

    private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqID(1);
        builder.setUserName("LiLinfeng");
        builder.setProductName("Netty book");
        List<String> address = new ArrayList<>();
        address.add("Nanjing yuhuatai");
        address.add("Beijing liulichang");
        address.add("Shenzhen hongshulin");
        builder.setAddress(address.toString());
        return builder.build();
    }

    public static void main(String args[]) throws Exception {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();
        System.out.println("Before encode : " + req.toString());
        SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
        System.out.println("After decode : " + req.toString());
        System.out.println("After equal : --> " + req2.equals(req));
    }
}
