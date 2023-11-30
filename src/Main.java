import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        final NonBlockingQueue<Integer> queue = new NonBlockingQueue<>();
        final int totalElements = 20; // Загальна кількість елементів для додавання

        // Виробник 1
        Thread producer1 = new Thread(() -> {
            for (int i = 0; i < totalElements / 2; i++) {
                queue.enqueue(i);
                System.out.println("Producer 1 enqueued: " + i);
                try {
                    Thread.sleep(100); // Невелика затримка для демонстрації паралельності
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Виробник 2
        Thread producer2 = new Thread(() -> {
            for (int i = totalElements / 2; i < totalElements; i++) {
                queue.enqueue(i);
                System.out.println("Producer 2 enqueued: " + i);
                try {
                    Thread.sleep(100); // Невелика затримка для демонстрації паралельності
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Споживач
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < totalElements; i++) {
                try {
                    System.out.println("Consumer dequeued: " + queue.dequeue());
                } catch (NoSuchElementException e) {
                    System.out.println("Consumer can't deque. Queue is empty");
                    try {
                        Thread.sleep(100); // Затримка перед наступною спробою видалення
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        producer1.start();
        producer2.start();
        consumer.start();

        try {
            producer1.join();
            producer2.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
