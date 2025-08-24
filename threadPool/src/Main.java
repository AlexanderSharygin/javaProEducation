import java.util.Random;

public class Main {
    public static void main(String[] args) {

        CustomThreadPool pool = new CustomThreadPool(4);
        for (int i = 0; i < 10; i++) {
            final int taskNumber = i;
            pool.execute(() -> {
                System.out.println("Поток " + Thread.currentThread().getName() + " - выполняет задачу " + taskNumber);
                try {
                    Thread.sleep(new Random().nextInt(300, 500));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        pool.awaitTermination();
        pool.shutdown();
        pool.execute(() -> {
            System.out.println("Поток " + Thread.currentThread().getName() + " - выполняет задачу " + 123);
            try {
                Thread.sleep(new Random().nextInt(3000, 5000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}