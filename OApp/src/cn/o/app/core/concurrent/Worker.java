package cn.o.app.core.concurrent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("deprecation")
public abstract class Worker<JOB> implements IWoker<JOB> {

	protected List<JOB> mJobs;

	protected CopyOnWriteArrayList<JOB> mWorkingJobs;

	protected CopyOnWriteArrayList<Thread> mConcurrentWorkers;

	protected boolean mStarted;

	public Worker() {

	}

	public Worker(List<JOB> jobs) {
		this.delegate(jobs);
	}

	@Override
	public void delegate(List<JOB> jobs) {
		if (mStarted) {
			return;
		}
		mJobs = jobs;
		if (mJobs != null) {
			if (mWorkingJobs == null) {
				mWorkingJobs = new CopyOnWriteArrayList<JOB>(mJobs);
			} else {
				mWorkingJobs.clear();
				mWorkingJobs.addAll(jobs);
			}
		}
	}

	@Override
	public boolean start() {
		if (mStarted) {
			return false;
		}
		int workerCount = getConcurrentCount();
		if (workerCount < 1) {
			throw new UnsupportedOperationException();
		}
		mStarted = true;
		mConcurrentWorkers = new CopyOnWriteArrayList<Thread>();
		for (int i = 0; i < workerCount; i++) {
			WorkThread worker = new WorkThread();
			mConcurrentWorkers.add(worker);
			worker.start();
		}
		return true;
	}

	@Override
	public boolean stop() {
		if (!mStarted) {
			return false;
		}
		if (mConcurrentWorkers != null) {
			for (Thread worker : mConcurrentWorkers) {
				if (worker.isAlive()) {
					worker.stop();
				}
			}
			mConcurrentWorkers.clear();
			mConcurrentWorkers = null;
		}
		mJobs = null;
		return true;
	}

	@Override
	public int getConcurrentCount() {
		int cores = Runtime.getRuntime().availableProcessors();
		return cores > 1 ? cores - 1 : cores;
	}

	@Override
	public double getProgress() {
		if (mJobs == null || mConcurrentWorkers == null) {
			return 1;
		}
		double jobCount = mJobs.size();
		if (jobCount == 0) {
			return 1;
		}
		double progress = 1 - (mWorkingJobs.size() + 1) / jobCount;
		return progress < 0 ? 0 : progress;
	}

	class WorkThread extends Thread {

		@Override
		public void run() {
			while (true) {
				JOB job = null;
				try {
					job = mWorkingJobs.remove(0);
				} catch (Exception e) {
					break;
				}
				try {
					work(job);
				} catch (Exception e) {

				}
				yield();
			}
			mConcurrentWorkers.remove(this);
			if (mConcurrentWorkers.size() == 0) {
				mConcurrentWorkers = null;
				mJobs = null;
				done();
			}
		}

	}

}
