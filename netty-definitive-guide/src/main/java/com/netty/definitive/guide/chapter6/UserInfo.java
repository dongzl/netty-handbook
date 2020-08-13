package com.netty.definitive.guide.chapter6;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @author dongzonglei
 * @description 用户信息实体类
 * @date 2019-04-19 11:31
 */
public class UserInfo implements Serializable {


    private static final long serialVersionUID = 2788933160156712256L;

    private String userName;

    private int userID;

    public UserInfo bindUserID(int userID) {
        this.userID = userID;
        return this;
    }

    public UserInfo bindUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public byte[] codeC() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] value = this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.userID);
        buffer.flip();
        value = null;
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }

    public byte[] codeC(ByteBuffer buffer) {
        buffer.clear();
        byte[] value = this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.userID);
        buffer.flip();
        value = null;
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }
}
