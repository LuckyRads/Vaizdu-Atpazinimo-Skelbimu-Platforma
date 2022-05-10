package com.lucky.smartadplatform.infrastructure.util;

import java.io.File;
import java.util.List;

public class FileUtil {

    private FileUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String getFilePath(List<String> path) {
        StringBuilder pathBuilder = new StringBuilder();
        String operatingSystem = System.getProperty("os.name");
        boolean isWindowsOs = operatingSystem.toLowerCase().contains("windows");

        pathBuilder.append(isWindowsOs ? "C:" : "").append(File.separator)
                .append(String.join(File.separator, path));
        return pathBuilder.toString();
    }

    public static String getDirPath(List<String> path) {
        return FileUtil.getFilePath(path) + File.separator;
    }

}
