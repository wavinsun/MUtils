package cn.o.app.core.concurrent;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("deprecation")
public abstract class Worker<JOB> implements IWoker<JOB> {

	protected List<JOB> mJobs;

	protected List<JOB> mWorkingJobs;

	protected List<Thread> mConcurrentWorkers;

	protected boolean mStarted;

	protected boolean mPrepared;

	public Worker() {

	}

	public Worker(List<JOB> jobs) {
		this.delegate(jobs);
	}

	@Override
	public synchronized void delegate(List<JOB> jobs) {
		if (mStarted) {
			return;
		}
		mJobs = jobs;
	}

	@Override
	public synchronized boolean start() {
		if (mStarted) {
			return false;
		}
		int workerCount = getConcurrentCount();
		if (workerCount < 1) {
			throw new UnsupportedOperationException();
		}
		mStarted = true;
		mConcurrentWorkers = new LinkedList<Thread>();
		for (int i = 0; i < workerCount; i++) {
			WorkThread worker = new WorkThread();
			mConcurrentWorkers.add(worker);
			worker.start();
		}
		return true;
	}

	@Override
	public synchronized boolean stop() {
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
		try {
			double progress = 1 - (mWorkingJobs.size() + 1.0) / mJobs.size();
			progress = progress > 1 ? 1 : progress;
			progress = progress < 0 ? 0 : progress;
			return progress;
		} catch (Exception e) {
			return mStarted ? 1 : 0;
		}
	}

	@Override
	public synchronized boolean prepare(List<JOB> jobs) {
		if (mPrepared || !mConcurrentWorkers.contains(Thread.currentThread())) {
			return false;
		}
		mPrepared = true;
		mWorkingJobs = new LinkedList<JOB>();
		if (jobs != null) {
			mWorkingJobs.addAll(jobs);
		}
		return true;
	}

	@Override
	public synchronized int getId(JOB job) {
		try {
			return mJobs.indexOf(job);
		} catch (Exception e) {
			return -1;
		}
	}

	class WorkThread extends Thread {

		@Override
		public void run() {
			prepare(mJobs);
			while (true) {
				JOB job = null;
				try {
					synchronized (Worker.this) {
						job = mWorkingJobs.remove(0);
					}
				} catch (Exception e) {
					break;
				}
				try {
					work(job);
				} catch (Exception e) {

				}
				yield();
			}
			synchronized (Worker.this) {
				mConcurrentWorkers.remove(this);
				if (mConcurrentWorkers.size() == 0) {
					done();
					mConcurrentWorkers = null;
					mJobs = null;
				}
			}
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			} else {
				if (o != null && (o instanceof Thread)) {
					return this.getId() == ((Thread) o).getId();
				} else {
					return false;
				}
			}
		}

	}

}
