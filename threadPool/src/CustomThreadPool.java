import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadPool {

    private final Object locker = new Object();
    private final LinkedList<Runnable> tasks;
    private final AtomicBoolean isShutdown;
    private final AtomicInteger activeTasks;
    private final AtomicBoolean isTerminated;

    public CustomThreadPool(int capacity) {
        if (capacity <= 0) {
            throw new RuntimeException("Емкость пула потоков должна быть больше 0");
        }
        tasks = new LinkedList<>();
        isShutdown = new AtomicBoolean(false);
        activeTasks = new AtomicInteger(0);
        isTerminated = new AtomicBoolean(false);

        for (int i = 0; i < capacity; i++) {
            WorkerThread worker = new WorkerThread("CustomPool, Thread #" + i);
            worker.start();
        }
    }

    public void execute(Runnable task) {
        if (isShutdown.get()) {
            throw new IllegalStateException("Активирован shutdown, новых задач не будет");
        }
        if (task == null) {
            throw new NullPointerException("Задача равна null");
        }
        synchronized (locker) {
            tasks.add(task);
            locker.notifyAll();
        }
    }

    public void shutdown() {
        isShutdown.set(true);
        synchronized (locker) {
            locker.notifyAll();
        }
    }

    public void awaitTermination() {
        try {
            synchronized (locker) {
                while (!tasks.isEmpty() || activeTasks.get() != 0) {
                    locker.wait();
                }
                isTerminated.set(true);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private class WorkerThread extends Thread {
        public WorkerThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Runnable task = null;
                    synchronized (locker) {
                        while (tasks.isEmpty() && !isShutdown.get()) {
                            locker.wait();
                        }
                        if (isShutdown.get() && tasks.isEmpty()) {
                            break;
                        }
                        if (!tasks.isEmpty()) {
                            task = tasks.removeFirst();
                        }
                    }
                    if (task != null) {
                        activeTasks.incrementAndGet();
                        task.run();
                        activeTasks.decrementAndGet();
                        synchronized (locker) {
                            locker.notifyAll();
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}