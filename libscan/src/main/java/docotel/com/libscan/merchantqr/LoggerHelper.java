package docotel.com.libscan.merchantqr;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by docotel on 3/8/16.
 */
public class LoggerHelper {

    private static final int VERBOSE = Log.VERBOSE;
    private static final int DEBUG = Log.DEBUG;
    private static final int INFO = Log.INFO;
    private static final int WARN = Log.WARN;
    private static final int ERROR = Log.ERROR;
    private static final int WTF = Log.ASSERT;

    protected static String TAG = ApplicationHelper.getPackageName();

    protected static void log(@IntRange(from = VERBOSE, to = WTF) final int level, @Nullable final String msg) {

        final StackTraceElement[] elements = new Throwable().getStackTrace();
        String callerClassName = "?";
        String callerMethodName = "?";
        String callerLineNumber = "?";
        if (elements.length >= 3) {
            callerClassName = elements[2].getClassName();
            callerClassName = callerClassName.substring(callerClassName.lastIndexOf('.') + 1);
            if (callerClassName.indexOf("$") > 0) {
                callerClassName = callerClassName.substring(0, callerClassName.indexOf("$"));
            }
            callerMethodName = elements[2].getMethodName();
            callerMethodName = callerMethodName.substring(callerMethodName.lastIndexOf('_') + 1);
            if (callerMethodName.equals("<init>")) {
                callerMethodName = callerClassName;
            }
            callerLineNumber = String.valueOf(elements[2].getLineNumber());
        }

        final String stack = "[" + callerClassName + "." + callerMethodName + "():" + callerLineNumber + "]" + (TextUtils.isEmpty(msg) ? "" : " ");
        switch (level) {
            case VERBOSE:
                Log.v(TAG, stack + msg);
                break;
            case DEBUG:
                Log.d(TAG, stack + msg);
                break;
            case INFO:
                Log.i(TAG, stack + msg);
                break;
            case WARN:
                Log.w(TAG, stack + msg);
                break;
            case ERROR:
                Log.e(TAG, stack + msg);
                break;
            case WTF:
                Log.wtf(TAG, stack + msg);
                break;
            default:
                break;
        }
    }

    public static void debug(@NonNull final Object object) {
        log(DEBUG, LoggerHelper.jsonify(object));
    }

    public static void debug(@NonNull final String msg) {
        log(DEBUG, msg);
    }

    public static void verbose(@NonNull final Object object) {
        log(VERBOSE, LoggerHelper.jsonify(object));
    }

    public static void verbose(@NonNull final String msg) {
        log(VERBOSE, msg);
    }

    public static void info(@NonNull final Object object) {
        log(INFO, LoggerHelper.jsonify(object));
    }

    public static void info(@NonNull final String msg) {
        log(INFO, msg);
    }

    public static void warning(@NonNull final Object object) {
        log(WARN, LoggerHelper.jsonify(object));
    }

    public static void warning(@NonNull final String msg) {
        log(WARN, msg);
    }

    public static void error(@NonNull final Object object) {
        log(ERROR, LoggerHelper.jsonify(object));
    }

    public static void error(@NonNull final String msg) {
        log(ERROR, msg);
    }

    public static void wtf(@NonNull final Object object) {
        log(WTF, LoggerHelper.jsonify(object));
    }

    public static void wtf(@NonNull final String msg) {
        log(WTF, msg);
    }

    public static String jsonify(@NonNull final Object object) {
        return new Gson().toJson(object, object.getClass());
    }

}
