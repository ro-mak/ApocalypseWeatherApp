package android.util;

public class Log {
    public static int println(int priority, String tag, String msg) {
        System.out.println(tag + ": " + msg);
        return 0;
    }
}
