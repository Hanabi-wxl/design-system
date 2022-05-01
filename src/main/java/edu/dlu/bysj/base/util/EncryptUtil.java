package edu.dlu.bysj.base.util;

import org.apache.shiro.crypto.hash.SimpleHash;


/**
 * shiro 对应的加密工具
 * @author XiangXinGang
 * @date 2021/10/12 15:51
 */
public class EncryptUtil {
    private static final String HASH_ALGORITHM_NAME = "MD5";
    public static String encrypt(String password, String salt) {
        int hashIterations = 1024;
        Object obj = new SimpleHash(HASH_ALGORITHM_NAME, password, salt, hashIterations);
        System.out.println(obj);
        return obj.toString();
    }
}
