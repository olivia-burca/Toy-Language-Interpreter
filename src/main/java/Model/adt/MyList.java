package Model.adt;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements IList<T> {
    //List <T> l = new ArrayList<>();

    private List<T> l;

    public MyList(){
        this.l=new ArrayList<T>();
    }

    public MyList(List<T> _l) { this.l = new ArrayList<T>(_l); }

    @Override
    public void add(T v) { l.add(v); }

    @Override
    public T pop() {return null;}

    @Override
    public List<T> getList() {
        return this.l;
    }

    public T getFirstElement() {return null;}

    @Override
    public boolean empty() {
        return this.l.isEmpty();
    }

    @Override
    public void clear(){
        this.l.clear();
    }

    @Override
    public String toString(){
        StringBuffer buff = new StringBuffer();

        for(T i: l)
            buff.append(i + " ");

        return buff.toString();
    }
    @Override
    public Iterable<T> getAll() {
        return l;
    }

    @Override
    public int size() {
        return l.size();
    }

    @Override
    public T getFromIndex(int index) {
        return l.get(index);
    }

}
