package com.cdlibs.message;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chend on 2017/8/1.
 */
public class MessageUtil {
    private static final int BODY_BLOCK_LENGTH = 4;
    private static final int HEADER_BLOCK_LENGTH = 4;
    private static final int TOTAL_FIXED_LENGTH = BODY_BLOCK_LENGTH + HEADER_BLOCK_LENGTH;

    public static final Charset MESSAGE_CHARSET = Charset.forName("UNICODE");

    //消息转换为byte
    @SuppressWarnings("unchecked")
    public static byte[] encode(Message message) {

        //获取消息头的byte数组
        StringBuilder sb = new StringBuilder();

        message.getHeaders().forEach((k, v) -> sb.append(k).append(":").append(v).append("\n"));
        byte[] headerBytes = sb.toString().getBytes(MESSAGE_CHARSET);

        //获取消息实体的byte数组
        byte[] bodyBytes = message.getBodyBytes();
        int bodyLength = bodyBytes == null ? 0 : bodyBytes.length;

        //创建消息byte数组
        byte[] messageBytes = new byte[headerBytes.length + bodyLength + TOTAL_FIXED_LENGTH];


        //消息头部长度
        System.arraycopy(intToByteArray(headerBytes.length), 0, messageBytes, 0, HEADER_BLOCK_LENGTH);
        //消息头部实体
        System.arraycopy(headerBytes, 0, messageBytes, TOTAL_FIXED_LENGTH, headerBytes.length);
        //消息体长度
        System.arraycopy(intToByteArray(bodyLength), 0, messageBytes, HEADER_BLOCK_LENGTH, BODY_BLOCK_LENGTH);

        if (bodyBytes != null)
            System.arraycopy(bodyBytes, 0, messageBytes, TOTAL_FIXED_LENGTH + headerBytes.length, bodyLength);

        return messageBytes;
    }

    //byte转换为消息
    @SuppressWarnings("unchecked")
    public static <T> T decode(byte[] messageBytes) {
        int headerLength = byteArrayToInt(messageBytes);
        int bodyLength = byteArrayToInt(messageBytes, HEADER_BLOCK_LENGTH);

        byte[] headerBytes = new byte[headerLength];
        System.arraycopy(messageBytes, TOTAL_FIXED_LENGTH, headerBytes, 0, headerLength);
        String[] headerArray = new String(headerBytes, MESSAGE_CHARSET).split("\n");
        Map<String, String> headers = new HashMap<>();
        for (String header : headerArray) {
            String[] h = header.split(":");
            headers.put(h[0], h[1]);
        }

        byte[] bodyBytes = null;
        if (0 != bodyLength) {
            bodyBytes = new byte[bodyLength];
            System.arraycopy(messageBytes, TOTAL_FIXED_LENGTH + headerLength, bodyBytes, 0, bodyLength);
        }
        T message;
        try {
            Class<?> clazz = Message.class.getClassLoader().loadClass(headers.get("class-name"));
            message = (T) clazz.newInstance();
            ((Message) message).setHeaders(headers);
            ((Message) message).setBody(bodyBytes);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        return message;
    }


    public static int byteArrayToInt(byte[] b) {
        return byteArrayToInt(b, 0);
    }

    public static int byteArrayToInt(byte[] b, int startPos) {
        return b[startPos + 3] & 0xFF |
                (b[startPos + 2] & 0xFF) << 8 |
                (b[startPos + 1] & 0xFF) << 16 |
                (b[startPos] & 0xFF) << 24;
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

}
