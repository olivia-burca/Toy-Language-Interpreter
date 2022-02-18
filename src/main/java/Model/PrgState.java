package Model;

import Exceptions.EmptyStack;
import Model.adt.IDict;
import Model.adt.IList;
import Model.adt.IMyHeap;
import Model.adt.IStack;
import Model.stmt.IStmt;
import Model.value.IValue;
import Model.value.StringValue;

import java.io.BufferedReader;

public class PrgState {

    IStack<IStmt> stack;
    IDict<String, IValue> symTable;
    IList<IValue> out;
    IStmt originalProgram;
    IDict<StringValue, BufferedReader> fileTable;
    IMyHeap<Integer,IValue>  heapTable;
    private final int id;
    private static int count = 0;



    public PrgState(IStack<IStmt> s, IDict<String, IValue> d, IList<IValue> l,IStmt oP,
                    IDict<StringValue, BufferedReader> fT, IMyHeap<Integer,IValue>  heap){
        stack = s;
        symTable = d;
        out = l;
        fileTable = fT;
        this.heapTable = heap;
        originalProgram = oP;
        this.id = getID();


    }

    public IStack<IStmt> getStack() {
        return stack;
    }

    public IList<IValue> getOutput() {
        return out;
    }

    public IDict<String, IValue> getSymTable() {
        return symTable;
    }

    public IDict<StringValue, BufferedReader> getFileTable() {return fileTable;}

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    public IMyHeap<Integer,IValue>  getHeapTable() {
        return heapTable;
    }

    public void setHeapTable(IMyHeap<Integer,IValue>  hT) { heapTable= hT;}
    public void setStack(IStack<IStmt> s){ stack = s;}
    public void setSymTable(IDict<String, IValue> d){ symTable = d;}
    public void setOutput(IList<IValue> l) { out = l;}
    public void setOriginalProgram(IStmt s){ originalProgram = s;}
    public void setFileTable(IDict<StringValue, BufferedReader> d){ fileTable = d;}

    public synchronized static int getID() {
        count++;
        return count-1;
    }
    public int getid() { return id;}

    public boolean isNotCompleted(){
        return !stack.isEmpty();
    }

    public PrgState oneStep(){

        if(stack.isEmpty())
            //return null;
            throw new EmptyStack("Stack is Empty\n");

        IStmt st = stack.pop();

        return st.execute(this);
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        //str.append("ProgramState { \n");
        //str.append("ExeStack: \n").append(stack).append("\n");
        //str.append("SymTable: \n").append(symTable).append(" \n");
        //str.append("Output: \n").append(out).append(" \n");
        //str.append("FileTable: \n").append(fileTable).append(" \n");
        //str.append("originalProgram{").append(originalProgram).append("}\n}\n");

        str.append("ID: ").append(id);

        str.append("\nExeStack: \n").append("\n");
        for(IStmt S: stack.getAll())
            str.append("").append(S).append('\n');

        str.append("\nSymTable:\n");
        for(String key: symTable.getAll())
            str.append(key).append("-->").append(symTable.getValue(key)).append('\n');

        str.append("\nOut:\n");
        for(IValue i: out.getAll())
            str.append("").append(i).append("\n");


        str.append("\nFileTable:\n");
        for(StringValue i: fileTable.getAll())
            str.append("" + i + fileTable.getValue(i) + "\n");

        str.append(heapTable.toString());

        str.append("\n");


        return str.toString();
    }

}