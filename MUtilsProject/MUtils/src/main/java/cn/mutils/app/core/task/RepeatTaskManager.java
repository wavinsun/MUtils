package cn.mutils.app.core.task;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.mutils.app.core.IClearable;
import cn.mutils.app.core.task.RepeatTask.IRepeatTaskListener;
import cn.mutils.app.core.text.StringUtil;

@SuppressWarnings("SimplifiableIfStatement")
public class RepeatTaskManager implements IClearable, IRepeatTaskListener {

    protected Map<String, List<RepeatTask>> mMap;

    public void add(RepeatTask task) {
        synchronized (this) {
            if (mMap == null) {
                mMap = new ConcurrentHashMap<String, List<RepeatTask>>();
            }
        }
        String name = task.getName();
        if (StringUtil.isEmpty(name)) {
            throw new UnsupportedOperationException();
        }
        List<RepeatTask> tasks = mMap.get(name);
        synchronized (this) {
            if (tasks == null) {
                tasks = new CopyOnWriteArrayList<RepeatTask>();
                mMap.put(name, tasks);
            }
        }
        tasks.add(0, task);
        startOneItem(tasks);
    }

    public void clear() {
        if (mMap == null) {
            return;
        }
        for (Map.Entry<String, List<RepeatTask>> entry : mMap.entrySet()) {
            List<RepeatTask> tasks = entry.getValue();
            for (RepeatTask task : tasks) {
                task.removeListener(this);
                task.stop();
            }
            tasks.clear();
        }
        mMap.clear();
    }

    protected void startOneItem(List<RepeatTask> tasks) {
        if (tasks == null || tasks.size() == 0) {
            return;
        }
        if (tasks.size() == 1) {
            RepeatTask task = tasks.get(0);
            if (!task.isRunning()) {
                task.addListener(this);
                task.start();
            }
        } else {
            boolean isRunning = false;
            for (RepeatTask task : tasks) {
                if (task.isRunning()) {
                    isRunning = true;
                    break;
                }
            }
            if (!isRunning) {
                RepeatTask task = tasks.get(0);
                tasks.clear();
                tasks.add(task);
                task.addListener(this);
                task.start();
            }
        }
    }

    @Override
    public void onStart(RepeatTask task) {

    }

    @Override
    public void onStop(RepeatTask task) {
        if (mMap == null) {
            return;
        }
        List<RepeatTask> tasks = mMap.get(task.getName());
        if (tasks == null) {
            return;
        }
        tasks.remove(task);
        startOneItem(tasks);
    }

    @Override
    public boolean onPreRepeat(RepeatTask task) {
        if (mMap == null) {
            return false;
        }
        List<RepeatTask> tasks = mMap.get(task.getName());
        if (tasks == null) {
            return false;
        }
        return tasks.indexOf(task) != 0;
    }

    @Override
    public void onRepeat(RepeatTask task) {

    }

}
