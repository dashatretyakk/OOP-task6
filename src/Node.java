import java.util.concurrent.atomic.AtomicReference;

public class Node<E> {
    final E item;
    final AtomicReference<Node<E>> next;

    public Node(E item) {
        this.item = item;
        this.next = new AtomicReference<>(null);
    }
}
