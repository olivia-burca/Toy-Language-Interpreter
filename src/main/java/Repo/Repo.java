package Repo;

import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IList;
import Model.adt.IMyHeap;
import Model.adt.IStack;
import Model.stmt.IStmt;
import Model.value.IValue;
import Model.value.StringValue;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Repo implements IRepo {


    List<PrgState> list ; //= new ArrayList<>();
    String logFilePath;

    public Repo(String logFilePath) {
        this.list = new ArrayList<>();
        this.logFilePath = logFilePath;
    }

    //@Override
    //public void addPrg(PrgState x) {
      //  list.add(x);
    //}

    @Override
    public void addProgram(PrgState newPrg) {
        list.add(newPrg);
    }
    @Override
    public PrgState getCrtPrg() {
        return list.get(0);
    }

    @Override
    public List<PrgState> getPrgList() {
        return list;
    }

    @Override
    public void setPrgList(List<PrgState> list) {
        this.list = list;
    }


    @Override
    public void logPrgStateExec(PrgState state)  {
        PrintWriter logFile = null;
       try {
           logFile = new PrintWriter(new BufferedWriter(new FileWriter(this.logFilePath,true)));
           //PrgState state = this.getCrtPrg();
           IStack<IStmt> stack = state.getStack();
           IDict<String, IValue> dict = state.getSymTable();
           IList<IValue> out = state.getOutput();
           IMyHeap<Integer,IValue> heap = state.getHeapTable();
           IDict<StringValue, BufferedReader> fileTable = state.getFileTable();

           logFile.println("ID: ");
           logFile.println(state.getid());

           logFile.println("\nExeStack:\n");
           for(IStmt S: stack.getAll())
               logFile.println(""+ S);


           logFile.println("\nSymTable:\n");
           for(String key: dict.getAll())
               logFile.println(key + "->" + dict.getValue(key) +'\n');

           logFile.println("\nOut:\n");
           for(IValue i: out.getAll())
               logFile.println(""+i+"\n" );


           logFile.println("\nFileTable:\n");
           for(StringValue i: fileTable.getAll())
               logFile.println("" + i + fileTable.getValue(i) + "\n");

           //dkkd
          logFile.println(heap.toString());


           logFile.println("\n");

       } catch (IOException e) {
           e.printStackTrace();
       }
       assert logFile != null;
       logFile.close();


    }
}
