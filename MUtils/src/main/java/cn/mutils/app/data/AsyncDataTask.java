package cn.mutils.app.data;

import android.os.AsyncTask;

import cn.mutils.app.queue.QueueItem;

@SuppressWarnings("unchecked")
public class AsyncDataTask<DATA> extends QueueItem<IAsyncDataTask<DATA>>
        implements IAsyncDataTask<DATA> {

    protected DATA mData;

    protected DataAsyncTask mTask;

    @Override
    public DATA getData() {
        return mData;
    }

    @Override
    public void setData(DATA data) {
        if (mStarted || mStopped) {
            return;
        }
        mData = data;
    }

    @Override
    public boolean start() {
        boolean result = super.start();
        if (result) {
            this.mTask = new DataAsyncTask();
            this.mTask.execute();
        }
        return result;
    }

    @Override
    public boolean stop() {
        boolean result = super.stop();
        if (result) {
            if (this.mTask != null) {
                this.mTask.cancel(true);
            }
        }
        return result;
    }

    class DataAsyncTask extends AsyncTask<String, Integer, Object> {

        protected Object doInBackground(String... paramters) {
            try {
                for (IAsyncDataTaskListener<DATA> listener : getListeners(IAsyncDataTaskListener.class)) {
                    listener.onDoInBackground(AsyncDataTask.this, getData());
                }
                return new Object();
            } catch (Exception e) {
                return e;
            }
        }

        protected void onPostExecute(Object object) {
            for (IAsyncDataTaskListener<DATA> listener : getListeners(IAsyncDataTaskListener.class)) {
                if (object instanceof Exception) {
                    listener.onException(AsyncDataTask.this, (Exception) object);
                } else {
                    listener.onComplete(AsyncDataTask.this);
                }
            }
            stop();
        }
    }

}
