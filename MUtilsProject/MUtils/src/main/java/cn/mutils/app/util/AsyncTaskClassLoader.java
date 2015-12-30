package cn.mutils.app.util;

/**
 * Fix bug for AsyncTask.onPostExecute method can not execute because
 * InternalHandler creation is not in main thread.
 */
public class AsyncTaskClassLoader implements Runnable {

    public void run() {
        try {
            Class.forName("android.os.AsyncTask");
        } catch (Exception e) {

        }
    }

}
