//package com.cdlibs.cdtp.message;
//
//import java.io.ByteArrayOutputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
///**
// * Created by chend on 2017/8/1.
// * 消息前八位为头部长度
// */
//public abstract class AbstractMessage<T> extends Message {
//
//
//    public void setEntity(T entity) {
//        this.entity = entity;
//        setMessageBodyLength();
//    }
//
//    public T getEntity() {
//        return entity;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        return o != null && o instanceof Message && o.hashCode() == this.hashCode();
//    }
//
//    @Override
//    public int hashCode() {
//        return id.hashCode();
//    }
//
//    //返回8为长度和headerbyte数组
//    protected byte[] getHeaderByte() {
//        byte[] headerByte = headerToByte();
//        byte[] allHeader = new byte[headerByte.length + HEADER_LENGTH];
//
//        System.arraycopy(intToByteArray(headerByte.length), 0, allHeader, 0, HEADER_LENGTH);
//
//        System.arraycopy(headerByte, 0, allHeader, HEADER_LENGTH, headerByte.length);
//
//        return allHeader;
//    }
//
//    private byte[] intToByteArray(int i) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
//        try {
//            dataOutputStream.writeInt(i);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        byte[] buf = byteArrayOutputStream.toByteArray();
//        try {
//            dataOutputStream.close();
//            byteArrayOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return buf;
//    }
//
//    //消息头部转换为byte
//    private byte[] headerToByte() {
//        StringBuilder sb = new StringBuilder();
//        headers.forEach((k, v) -> {
//            sb.append(k).append(":").append(v).append("\n");
//        });
//        return sb.toString().getBytes(charset);
//    }
//
//    public void addHeader(String key, String value) {
//        headers.put(key, value);
//    }
//
//    public String removeHeader(String key) {
//        return headers.remove(key);
//    }
//
//    public void setMessageBodyLength(int length) {
//        headers.put("body-length", String.valueOf(length));
//    }
//
//    public abstract void setMessageBodyLength();
//
//}
