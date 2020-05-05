package net.xicp.tarbitrary.seckill.util;

import com.alibaba.druid.util.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author tarbitrary
 */
public class MD5Util {
    public static final String DEFAULT_SALT = "tarbitrary";

    public static final String md5(String originStr) {
        final String md5Result = DigestUtils.md5Hex(originStr);
        return md5Result;
    }

    public static final String md5WithSalt(String orginStr, String salt) {
        if (StringUtils.isEmpty(salt)) {
            return md5(orginStr);
        }
        int len = salt.length();
        int middle = len >>> 1;
        String assembleStr = salt.substring(0, middle) + orginStr + salt.substring(middle, len);
        String md5Str = md5(assembleStr);
        return md5Str;
    }

    public static final String md5WithDefaultSalt(String originStr) {
        return md5WithSalt(originStr, DEFAULT_SALT);
    }

    public static void main(String[] args) {
//        final String s = md5WithSalt("123456", DEFAULT_SALT);
        final String s = md5WithDefaultSalt("123456");
        System.out.println(s);
        System.out.println(DEFAULT_SALT.substring(0, 0));
        System.out.println(md5WithSalt(s, "tq"));

    }


}
