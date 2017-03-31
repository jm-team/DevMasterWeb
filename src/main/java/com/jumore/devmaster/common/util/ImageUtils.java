package com.jumore.devmaster.common.util;

import java.io.File;

import org.apache.commons.lang.StringUtils;

public abstract class ImageUtils {
    public static final String[] EXTENSIONS = { "jpg", "jpeg", "png", "bmp", "gif", "psd", "pcx", "tiff", "tga", "exif", "fpx", "svg",
            "cdr", "pcd", "dxf", "ufo", "eps", "hdri", "ai", "raw", "wmf", "emf", "lic" };

    private static boolean isImageByExtension(String extension) {
        return !StringUtils.isBlank(extension) && extension.contains(extension.toLowerCase());
    }

    public static boolean isImage(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return false;
        }

        int index = fileName.lastIndexOf('.');

        if (index == -1) {
            return false;
        }

        String extension = fileName.substring(index + 1);

        return isImageByExtension(extension);
    }

    public static boolean isImage(File file) {
        if (file == null || file.isDirectory()) {
            return false;
        }

        return isImage(file.getName());
    }
}
