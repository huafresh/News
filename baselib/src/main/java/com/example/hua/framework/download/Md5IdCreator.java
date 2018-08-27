package com.example.hua.framework.download;

import com.example.hua.framework.download.core.DownloadRequest;
import com.example.hua.framework.download.core.IdCreator;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 默认id生成器实现
 * Created by hua on 2018/8/5.
 */

class Md5IdCreator implements IdCreator {
    @Override
    public String create(DownloadRequest request) {
        return getMD5Str(request.getUrl());
    }

    private String getMD5Str(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] byteArray = messageDigest.digest();
            StringBuilder md5StrBuilder = new StringBuilder();
            for (byte b : byteArray) {
                if (Integer.toHexString(0xFF & b).length() == 1) {
                    md5StrBuilder.append("0").append(Integer.toHexString(0xFF & b));
                } else {
                    md5StrBuilder.append(Integer.toHexString(0xFF & b));
                }
            }
            //16位加密，从第9位到25位
            return md5StrBuilder.substring(8, 24).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

}
