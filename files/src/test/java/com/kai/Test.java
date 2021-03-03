package com.kai;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Test {

    @org.junit.jupiter.api.Test
    public void test01(){
        System.out.println(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                + UUID.randomUUID().toString().replace("-", "")
                + "kai.cpp";
        System.out.println(newFileName);
    }
}
