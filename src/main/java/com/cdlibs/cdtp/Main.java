package com.cdlibs.cdtp;

import com.cdlibs.cdtp.message.TextMessage;
import com.cdlibs.cdtp.util.MessageUtil;

/**
 * Created by chend on 2017/8/1.
 */
public class Main {
    public static void main(String[] args) {
        TextMessage m = new TextMessage();
        m.setText("你好");
        byte[] a=MessageUtil.encode(m);

        TextMessage m2= (TextMessage) MessageUtil.decode(a);

        System.out.println(m2);

    }
}
