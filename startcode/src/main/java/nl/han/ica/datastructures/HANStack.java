package nl.han.ica.datastructures;

public class HANStack<T> implements IHANStack<T> {
    private Node<T> top;

    // Node class for the stack
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    @Override
    public void push(T value) {
        Node<T> newNode = new Node<>(value);
        newNode.next = top;
        top = newNode;
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        T poppedValue = top.data;
        top = top.next;
        return poppedValue;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return top.data;
    }

    private boolean isEmpty() {
        return top == null;
    }
}
