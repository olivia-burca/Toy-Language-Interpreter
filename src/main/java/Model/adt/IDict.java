package Model.adt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IDict<T1,T2>{

    T2 put(T1 v1, T2 v2);
    void update(T1 v1, T2 v2);
    T2 lookup(T1 id);
    boolean isDefined(String id);
    boolean exists(T1 key);
    void setValue(T1 key, T2 value);
    T2 getValue(T1 key);
    String toString();
    Iterable<T1> getAll();
    void remove(T1 key);
    List<T2> getAllValues();
    int size();

    Set<Map.Entry<T1, T2>> getContent(); //Map<T1, T2> getContent();
    public IDict<T1, T2> deepcopy();
    public Collection<T2> values();
    public Set<T1> keySet();

}
