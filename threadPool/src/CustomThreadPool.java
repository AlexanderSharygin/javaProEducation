import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomThreadPool {

    private final Object locker = new Object();
    private final HashSet<WorkerThread> workers = new HashSet<>();
    private final LinkedList<Runnable> tasks;
    private final AtomicBoolean isShutdown;
    private final AtomicBoolean isTerminated;

    public CustomThreadPool(int capacity) {
        if (capacity <= 0) {
            throw new RuntimeException("Емкость пула потоков должна быть больше 0");
        }
        tasks = new LinkedList<>();
        isShutdown = new AtomicBoolean(false);
        isTerminated = new AtomicBoolean(false);
        for (int i = 0; i < capacity; i++) {
            WorkerThread worker = new WorkerThread("CustomPool, Thread #" + i);
            workers.add(worker);
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
            locker.notify();
        }
    }

    public void shutdown() {
        isShutdown.set(true);
        synchronized (locker) {
            locker.notifyAll();
        }
    }

    public boolean awaitTermination() {
        try {
            for (WorkerThread worker : workers) {
                worker.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        isTerminated.set(true);
        return true;
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
                        task.run();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}