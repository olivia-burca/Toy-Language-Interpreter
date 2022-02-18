package Model.adt;

import java.util.List;

public interface IList<T> {
    void add(T v);
    T pop();
    String toString();
    boolean empty();
    void clear();
    Iterable<T> getAll();
    List<T> getList();
    int size();
    T getFromIndex(int index);
}

