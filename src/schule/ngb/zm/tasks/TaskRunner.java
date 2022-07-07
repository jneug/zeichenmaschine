package schule.ngb.zm.tasks;

import schule.ngb.zm.util.Log;

import javax.swing.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Führt Aufgaben (Tasks) parallel zum Hauptprogramm aus.
 */
public class TaskRunner {

	private static final int POOL_SIZE = 4;

	private static final int SHUTDOWN_TIME = 100;


	private static TaskRunner runner;

	public static TaskRunner getTaskRunner() {
		if( runner == null ) {
			runner = new TaskRunner();
		}
		return runner;
	}

	public static Future<?> run( Runnable task ) {
		TaskRunner r = getTaskRunner();
		return r.pool.submit(task);
	}

	public static <T> Future<T> run( Runnable task, T result ) {
		TaskRunner r = getTaskRunner();
		return r.pool.submit(task, result);
	}

	public static Future<?>  schedule( Runnable task, int ms ) {
		TaskRunner r = getTaskRunner();
		return r.pool.schedule(task, ms, TimeUnit.MILLISECONDS);
	}

	public static void invokeLater( Runnable task ) {
		SwingUtilities.invokeLater(task);
	}

	public static void shutdown() {
		if( runner != null ) {
			runner.pool.shutdown();
			try {
				runner.pool.awaitTermination(SHUTDOWN_TIME, TimeUnit.MILLISECONDS);
			} catch( InterruptedException ex ) {

			} finally {
				if( !runner.pool.isTerminated() ) {
					runner.pool.shutdownNow();
				}
			}
		}
	}

	ScheduledExecutorService pool;

	private TaskRunner() {
		//pool = new ScheduledThreadPoolExecutor(4);
		pool = Executors.newScheduledThreadPool(POOL_SIZE, new ThreadFactory() {
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
