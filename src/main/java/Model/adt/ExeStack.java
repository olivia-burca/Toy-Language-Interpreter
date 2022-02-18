package Model.adt;
import java.util.ArrayDeque;
import java.util.Deque;

public class ExeStack<T> implements IStack<T> {
    Deque<T> stack = new ArrayDeque<>();


    @Override
    public T pop() {
        return stack.pop();
    }

    @Override
    public void push(T v) {
        stack.push(v);
    }

    @Override
    public boolean isEmpty() {
        return stack.size()==0;
    }

    @Override
    public String toString(){
        StringBuffer str = new StringBuffer();
        for(T i : stack){
            str.append(i);
        }
        return str.toString();
    }

    @Override
    public Iterable<T> getAll() {
        return stack;
    }

}