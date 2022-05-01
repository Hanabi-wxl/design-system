package edu.dlu.bysj.base.util;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * @author sinre
 * @create 04 29, 2022
 * @since 1.0.0
 */
public class SimpleHashUtil {
    /*
     * @Description: 生成加密密码
     * @Author: sinre
     * @Date: 2022/4/29 14:15
     * @param originPassword 明文密码
     * @param userNumber 用户账号即为盐值
     * @return java.lang.String
     **/
    public static String generatePassword(String originPassword, String userNumber){
        String algorithmName = "MD5";//加密算法
        int hashIterations = 1024;//加密次数
        SimpleHash simpleHash = new SimpleHash(algorithmName,originPassword,userNumber,hashIterations);
        return simpleHash.toString();
    }
}
