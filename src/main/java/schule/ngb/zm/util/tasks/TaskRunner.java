package schule.ngb.zm.util.tasks;

import schule.ngb.zm.util.Log;

import javax.swing.*;
import java.util.concurrent.*;

/**
 * Führt Aufgaben (Tasks) parallel zum Hauptprogramm aus.
 */
public class TaskRunner {

	private static TaskRunner runner;

	public static TaskRunner getTaskRunner() {
		if( runner == null ) {
			runner = new TaskRunner();
		}
		return runner;
	}

	public static Future<?> run( Task task ) {
		TaskRunner r = getTaskRunner();
		return r.pool.submit(task);
	}

	public static Future<?> run( Runnable task ) {
		TaskRunner r = getTaskRunner();
		return r.pool.submit(task);
	}

	public static <T> Future<T> run( Runnable task, T result ) {
		TaskRunner r = getTaskRunner();
		return r.pool.submit(task, result);
	}

	public static Future<?> invokeLater( Runnable task ) {
		FutureTask<Object> future = new FutureTask<>(task, null);
		SwingUtilities.invokeLater(future);
		return future;
	}

	public static <T> Future<T> invokeLater( Runnable task, T result ) {
		FutureTask<T> future = new FutureTask<>(task, result);
		SwingUtilities.invokeLater(future);
		return future;
	}

	public static void shutdown() {
		if( runner != null ) {
			/*runner.pool.shutdown();
			try {
				runner.pool.awaitTermination(SHUTDOWN_TIME, TimeUnit.MILLISECONDS);
			} catch( InterruptedException ex ) {

			} finally {
				if( !runner.pool.isTerminated() ) {
					runner.pool.shutdownNow();
				}
			}*/
			runner.pool.shutdownNow();
		}
	}

	ExecutorService pool;

	private TaskRunner() {
		//pool = new ScheduledThreadPoolExecutor(4);
		/*pool = Executors.newScheduledThreadPool(POOL_SIZE, new ThreadFactory() {
			private final ThreadFactory threadFactory = Executors.defaultThreadFactory();

			@Override
			public Thread newThread( Runnable r ) {
				Thread t = threadFactory.newThread(r);
				t.setName("TaskRunner-" + t.getName());
				t.setDaemon(true);
				return t;
			}
		});*/
		pool = Executors.newCachedThreadPool(new ThreadFactory() {
			private final ThreadFactory threadFactory = Executors.defaultThreadFactory();

			@Override
			public Thread newThread( Runnable r ) {
				Thread t = threadFactory.newThread(r);
				t.setName("TaskRunner-" + t.getName());
				t.setDaemon(true);
				return t;
			}
		});
	}

	private static final Log LOG = Log.getLogger(TaskRunner.class);

}
