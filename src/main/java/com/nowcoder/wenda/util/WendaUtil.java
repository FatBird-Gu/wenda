package com.nowcoder.wenda.util;



import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class WendaUtil {
    //生成随机字符串
    public static String gennerateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    // MD5 加密
    // hello -> abc123def456 不能解密
    // hello + 3ead -> abc123def456abc
    public static String md5(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
