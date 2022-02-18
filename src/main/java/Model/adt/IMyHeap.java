package Model.adt;

import Model.value.IValue;

import java.util.Map;
import java.util.Set;

public interface IMyHeap<T1, T2> {
    void add(T2 value);
    T1 getAddress(T2 value);
    void update(T1 key, T2 value);
    T2 getValue(T1 key);
    boolean isDefined(T1 key);
    String toString();
    void setContent(Map<Integer, IValue> heapAfterCallingGarbageCollector);
    Set<Map.Entry<T1, T2>> getContent();
}
