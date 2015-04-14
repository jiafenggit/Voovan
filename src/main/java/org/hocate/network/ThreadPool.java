package org.hocate.network;

import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.hocate.tools.log.Logger;
import org.hocate.tools.TDateTime;



/**
 * 线程池
 * @author helyho
 *
 */
public class ThreadPool {
	private static ThreadPoolExecutor threadPool = createThreadPool();
	public static Hashtable<String,Object> temp = new Hashtable<String, Object>();
	
	private static ThreadPoolExecutor createThreadPool(){
		int cpuCoreCount = Runtime.getRuntime().availableProcessors();
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(cpuCoreCount, cpuCoreCount+2,1, TimeUnit.MINUTES,new LinkedBlockingQueue<Runnable>());
		//设置allowCoreThreadTimeOut,允许回收超时的线程
		threadPool.allowCoreThreadTimeOut(true);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(threadPool.isShutdown()){
					this.cancel();
					timer.cancel();
				}
				String threadPoolInfo = "PoolInfo:"+threadPool.getActiveCount()+"/"+threadPool.getCorePoolSize()+"/"+threadPool.getLargestPoolSize()+" TaskCount: "
						+threadPool.getCompletedTaskCount()+"/"+threadPool.getTaskCount()+" QueueSize:"+threadPool.getQueue().size();
				if(threadPool.getActiveCount()!=0){
					Logger.simple(TDateTime.now()+"-"+threadPool.isShutdown()+" "+threadPoolInfo);
				}
			}
		}, 1, 1000);
		return threadPool;
	}
	
	public static ThreadPoolExecutor getThreadPool(){
		return threadPool;
	}
}
