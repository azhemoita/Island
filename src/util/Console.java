package util;

public class Console {
    public static boolean DEBUG = false;

    public static void log(String message) {
        if(DEBUG) {
            System.out.println(message);
        }
    }
}
