package datastructures;

public interface Position<E> {
    E getElement() throws IllegalStateException;
}