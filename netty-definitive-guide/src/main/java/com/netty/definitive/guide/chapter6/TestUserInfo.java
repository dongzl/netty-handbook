package com.netty.definitive.guide.chapter6;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * @author dongzonglei
 * @description 测试jdk序列化性能
 * @date 2019-04-19 11:35
 */
public class TestUserInfo {

    public static void main(String args[]) throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.bindUserID(100).bindUserName("Welcome to netty");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(userInfo);
        os.flush();
        os.close();
        byte[] b = bos.toByteArray();
        System.out.println("The jdk Serializable length is :" + b.length);
        bos.close();
        System.out.println("==============================");
        System.out.println("The byte array Serializable length is :" + userInfo.codeC().length);
    }
}
