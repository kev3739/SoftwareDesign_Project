package util;

import java.time.LocalDateTime;

public class SystemClock {

    private static SystemClock instance;

    private SystemClock() {
    }

    public static SystemClock getInstance() {
        if (instance == null) {
            instance = new SystemClock();
        }
        return instance;
    }

    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}