
package util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Title: ThreadPool.java
 * @Package com.chinapex.nexus.util
 * @Description: TODO(用一句话描述该文件做什么)
 * @author zhangjianjun
 * @date 2018年6月5日 下午1:58:44
 */
public class ThreadPool {

	private static int initThreads = 5;

	private static int maxThreads = 10;

	private static ExecutorService pool = new ThreadPoolExecutor(initThreads, maxThreads, 1, TimeUnit.MINUTES,
			new LinkedBlockingQueue<Runnable>(200), new ThreadPoolExecutor.CallerRunsPolicy());

	public static void addToThreadPool(Runnable task) {
		pool.submit(task);
	}

}
