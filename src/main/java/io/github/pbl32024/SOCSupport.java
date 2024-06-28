package io.github.pbl32024;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SOCSupport {

    public static String trimSoc(String socCode) {
        if ("00-0000".equals(socCode)) {
            return "";
        } else if (socCode.endsWith("0000")) {
            return socCode.substring(0, 3);
        } else if (socCode.endsWith("00")) {
            return socCode.substring(0, 5);
        } else if (socCode.endsWith("0")) {
            return socCode.substring(0, 6);
        } else {
            return socCode;
        }
    }

}
