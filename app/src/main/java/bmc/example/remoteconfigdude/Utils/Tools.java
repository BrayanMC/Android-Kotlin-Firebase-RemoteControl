package bmc.example.remoteconfigdude.Utils;

import android.util.Log;

public class Tools {

    /**
     * Log a line
     * @param o The class that send the trace
     * @param line the line
     */
    public static void logLine(Object o, String line) {
        if (o instanceof String) {
            Log.d((String)o, line);
        } else {
            Log.d(o.getClass().getName(), line);
        }
    }
}
