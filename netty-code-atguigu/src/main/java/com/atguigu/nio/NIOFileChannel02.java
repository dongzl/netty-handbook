package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author dongzonglei
 * @description
 * @date 2020/8/14 下午1:57
 */
public class NIOFileChannel02 {

    public static void main(String[] args) throws Exception {

        //创建文件的输入流
//        File file = new File("d:\\file01.txt");
        File file = new File("/data/file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        
        //通过 fileInputStream 获取对应的 FileChannel -> 实际类型 FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();
        
        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());
        
        //将通道的数据读入到 Buffer
        fileChannel.read(byteBuffer);
        
        //将 byteBuffer 的字节数据转成 String
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
