package com.mycompany.myapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by c4r0n0s on 16.03.16.
 */
public class RegexpUtils {

    public static String getImageFormat(String mimeType) {
        Pattern pattern = Pattern.compile("\\w+$");
        Matcher matcher = pattern.matcher(mimeType);
        boolean isFound = matcher.find();
        if (isFound) {
            return matcher.group();
        }

        return null;
    }
}
