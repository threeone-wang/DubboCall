package com.yitulin.dubbocall.infrastructure.utils;

import com.yitulin.dubbocall.infrastructure.enums.ErrorCode;

/**
 * @author : ⚡️
 * description :
 * date : Created in 2021/9/26 11:10 上午
 * modified : 💧💨🔥
 */
public class AssertWithMessageUtil {

    public static boolean assertTrue(boolean b,ErrorCode errorCode,String message){
        if (!b){
            AlertMessageUtil.alertErrorInfo(message, errorCode.getTitle());
        }
        return b;
    }

    public static boolean assertFalse(boolean b,ErrorCode errorCode,String message){
        if (b){
            AlertMessageUtil.alertErrorInfo(message, errorCode.getTitle());
            return false;
        }
        return true;
    }

}
