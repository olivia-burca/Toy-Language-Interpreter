package Model.stmt;


import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IList;
import Model.adt.IMyHeap;
import Model.exp.IExp;
import Model.types.IType;
import Model.value.IValue;

public class PrintStmt implements IStmt{

    IExp exp;

    public PrintStmt(IExp _exp){
        exp = _exp;
    }

    public PrgState execute(PrgState state){
        IList<IValue> output =  state.getOutput();
        IDict<String, IValue> dict = state.getSymTable();
        IMyHeap<Integer,IValue>  heap = state.getHeapTable();
        IValue val = exp.eval(dict, heap);
        output.add(val);
        return null;
    }
    @Override
    public String toString()
    {
        return "print " + exp.toString() + " ";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        exp.typeCheck(typeEnv);
        return typeEnv;
    }
}
