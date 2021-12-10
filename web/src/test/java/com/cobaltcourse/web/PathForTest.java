package com.cobaltcourse.web;

import java.util.Locale;

public interface PathForTest {

    static String getPath() {
        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if(OS.contains("win")) {
            return "/C/users";
        } else {
            return "/lib/jvm";
        }
    }
}
