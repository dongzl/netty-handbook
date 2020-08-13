package com.netty.definitive.guide.chapter6;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 * @author dongzonglei
 * @description jdk序列化性能测试
 * @date 2019-04-19 11:41
 */
public class PerformTestUserInfo {

    public static void main(String args[]) throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.bindUserID(100).bindUserName("Welcome to netty");
        int loop = 1000000;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;
        long startTime = System.currentTimeMillis();
        for(int i = 0; i < loop; i++) {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            os.writeObject(userInfo);
            os.flush();
            os.close();
            byte[] b = bos.toByteArray();
            bos.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("The jdk Serializable length is :" + (endTime - startTime) + " ms");
        System.out.println("==============================");
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        startTime = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            byte[] b = userInfo.codeC(buffer);
        }
        endTime = System.currentTimeMillis();
        System.out.println("The byte array Serializable length is :" + (endTime - startTime) + " ms");
    }
}
