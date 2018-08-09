package com.rupeng.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UploadUtils {

    private static final Logger logger = LogManager.getLogger(UploadUtils.class);

    //获得文件扩展名，返回值包括点 .
    public static String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.'), filename.length());
    }

}
