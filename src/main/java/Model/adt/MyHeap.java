package Model.adt;

import Model.value.IValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyHeap<T1,T2> implements IMyHeap<T1,T2>{
    private HashMap<T1, T2> heap;
    private Integer freeLocation;

    public MyHeap(){
        this.heap=new HashMap<T1, T2>();
        this.freeLocation=1;

    }

    @Override
    public void add(T2 value) {
        this.heap.put((T1)this.freeLocation,value);
        this.freeLocation++;
    }

    @Override
    public T1 getAddress(T2 value) {
        Integer key = 0;
        if(!this.heap.isEmpty())
            key = 1;
        for(Map.Entry<T1, T2> entry : heap.entrySet())
            if(entry.getValue().equals(value))
                key = (Integer)entry.getKey();

        return (T1)key;
    }

    @Override
    public void update(T1 key, T2 value) {
        if (isDefined(key))
            this.heap.put(key,value);
    }

    @Override
    public T2 getValue(T1 key) {
        if (isDefined(key))
            return this.heap.get(key);
        return null;
    }

    @Override
    public boolean isDefined(T1 key) {
        return this.heap.containsKey(key);
    }

    @Override
    public void setContent(Map<Integer, IValue> heapAfterCallingGarbageCollector) {
        this.heap.clear();

        for(Map.Entry<Integer,IValue> entry : heapAfterCallingGarbageCollector.entrySet())
            this.heap.put((T1)entry.getKey(), (T2)entry.getValue());
    }


    @Override
    public Set<Map.Entry<T1, T2>> getContent() {
        return heap.entrySet();
    }

    @Override
    public String toString() {
        String message = "Heap Table: \n" + "{";
        for(Map.Entry<T1, T2> entry : heap.entrySet())
            message += entry.getKey().toString() + "->" + entry.getValue().toString() + ",";

        if(this.heap.size() > 0)
            message = message.substring(0, message.length() - 1); //remove the last comma if there are elements

        message += "}";

        return message;
    }
}
