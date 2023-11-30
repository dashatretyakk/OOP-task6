import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

public class NonBlockingQueue<E> {
    private final AtomicReference<Node<E>> head, tail;

    public NonBlockingQueue() {
        Node<E> dummy = new Node<>(null); // створення фіктивного вузла
        head = new AtomicReference<>(dummy);
        tail = new AtomicReference<>(dummy);
    }

    public void enqueue(E item) {
        Node<E> newNode = new Node<>(item);
        while (true) {
            Node<E> curTail = tail.get();
            Node<E> tailNext = curTail.next.get();

            if (curTail == tail.get()) {
                if (tailNext != null) {
                    // Черга в непослідовному стані, спробувати зсунути хвіст
                    tail.compareAndSet(curTail, tailNext);
                } else {
                    // В черзі є місце для нового вузла
                    if (curTail.next.compareAndSet(null, newNode)) {
                        // Вставка вдалася, спробувати зсунути хвіст на новий вузол
                        tail.compareAndSet(curTail, newNode);
                        return;
                    }
                }
            }
        }
    }

    public E dequeue() {
        while (true) {
            Node<E> curHead = head.get();
            Node<E> curTail = tail.get();
            Node<E> headNext = curHead.next.get();

            if (curHead == head.get()) {
                if (curHead == curTail) {
                    if (headNext == null) {
                        throw new NoSuchElementException();
                    }
                    tail.compareAndSet(curTail, headNext);
                } else {
                    E item = headNext.item;
                    if (head.compareAndSet(curHead, headNext)) {
                        return item;
                    }
                }
            }
        }
    }

}
