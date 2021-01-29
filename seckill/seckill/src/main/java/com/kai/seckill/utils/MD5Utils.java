package com.kai.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

/**
 * MD5工具类
 */
public class MD5Utils {

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }
    //与前端相统一的盐
    private static final String salt="1a2b3c4d";

    /**
     * 第一次加密,前端传到后端加密
     * @param inputPass
     * @return
     */
    public static String inputPassToFormPass(String inputPass){
        String str=""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    /**
     * 第二次加密,后端到数据库加密
     * @param formPass
     * @param salt
     * @return
     */
    public static String formPassToDBPass(String formPass,String salt){
        String str=""+salt.charAt(0)+salt.charAt(2)+formPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass,String salt){
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, salt);
        return dbPass;
    }

    @Test
    public void test(){

        System.out.println(inputPassToFormPass("123456"));
        //d3b1294a61a07da9b49b6e22b2cbd7f9

        System.out.println(formPassToDBPass("d3b1294a61a07da9b49b6e22b2cbd7f9","1a2b3c4d"));
        //b7797cce01b4b131b433b6acf4add449

        System.out.println(inputPassToDBPass("123456","1a2b3c4d"));
        //b7797cce01b4b131b433b6acf4add449
    }

}
