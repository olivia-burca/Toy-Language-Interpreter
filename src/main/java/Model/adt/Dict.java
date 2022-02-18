package Model.adt;
import Exceptions.VariableNotDefined;

import java.util.*;

public class Dict<T1,T2> implements IDict<T1,T2> {
    Map<T1, T2> dictionary;

    public Dict() {
        dictionary = new HashMap<T1,T2>();
    }

    @Override
    public boolean exists(T1 key) {
        return dictionary.containsKey(key);
    }

    @Override
    public T2 getValue(T1 key) {

        if(!exists(key))
            throw new VariableNotDefined("Variable not defined in our dictionary");
        return dictionary.get(key);
    }

    @Override
    public void setValue(T1 key, T2 value) {
        dictionary.put(key, value);
    }

    @Override
    public int size() {
        return dictionary.size();
    }

    @Override
    public T2 put(T1 v1, T2 v2) {
        return dictionary.put(v1,v2);
    }

    @Override
    public void update(T1 v1, T2 v2) {
        dictionary.put(v1,v2);
    }

    @Override
    public T2 lookup(T1 id) {
        return null;
    }

    @Override
    public boolean isDefined(String id) {
        return false;
    }

    @Override
    public String toString(){
        StringBuffer str = new StringBuffer();
        for(Map.Entry<T1, T2> e : dictionary.entrySet()){
            str.append("<"+e.getKey() + ", " + e.getValue() + ">; ");
        }
        return str.toString();
    }
    @Override
    public Iterable<T1> getAll() {
        return dictionary.keySet();
    }

    @Override
    public void remove(T1 key) {
        dictionary.remove(key);
    }

    @Override
    public List<T2> getAllValues() {
        List<T2> l = new ArrayList<>();

        for(T1 key: this.getAll())
            l.add(this.getValue(key));

        return l;
    }
    @Override
    public Set<T1> keySet() {
        return dictionary.keySet();
    }

    @Override
    public IDict<T1, T2> deepcopy() {
        IDict<T1, T2> symTable = new Dict<>();
        for (T1 key : this.keySet())
            symTable.put(key, this.getValue(key));

        return symTable;
    }

    @Override
    public Collection<T2> values() {
        return dictionary.values();
    }

    @Override
    public Set<Map.Entry<T1, T2>> getContent() {
        return dictionary.entrySet();
    }
}
