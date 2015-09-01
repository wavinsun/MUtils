package cn.o.app.core.runtime;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Thread utility of framework
 */
public class ThreadUtil {

	/** Main thread group */
	public static final String GROUP_MAIN = "main";

	/**
	 * Get main thread group
	 * 
	 * @return
	 */
	public static Thread[] getMainGroup() {
		return getGroup(GROUP_MAIN);
	}

	/**
	 * Get threads for group
	 * 
	 * @param name
	 * @return
	 */
	public static Thread[] getGroup(String name) {
		Thread[] activeThreads = new Thread[Thread.activeCount()];
		int enumCount = Thread.enumerate(activeThreads);
		LinkedList<Thread> group = new LinkedList<Thread>();
		for (int i = 0; i < enumCount; i++) {
			Thread t = activeThreads[i];
			ThreadGroup tGroup = t.getThreadGroup();
			if (tGroup == null) {
				continue;
			}
			String gName = tGroup.getName();
			if (gName != null && gName.equals(name)) {
				group.add(t);
			}
		}
		Arrays.fill(activeThreads, null);
		Thread[] groupThreads = new Thread[group.size()];
		group.toArray(groupThreads);
		group.clear();
		return groupThreads;
	}

}
