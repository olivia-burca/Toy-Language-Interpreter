package Model.stmt;

import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IMyHeap;
import Model.exp.IExp;
import Model.types.IType;
import Model.value.IValue;

public class AssignStmt implements IStmt{

    private String id;
    private IExp exp;

    public AssignStmt(String _id, IExp _exp){
        id = _id;
        exp = _exp;
    }

    @Override
    public String toString(){
        return id + "=" + exp + "; ";
    }

    @Override
    public PrgState execute(PrgState state ){
        IDict<String, IValue> dict = state.getSymTable();
        IMyHeap<Integer,IValue> heap = state.getHeapTable();
        IValue val = exp.eval(dict, heap);
        dict.setValue(id, val);
        return null;

    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        IType typevar = typeEnv.getValue(id);
        IType typexp = exp.typeCheck(typeEnv);
        if (typevar.equals(typexp)) return typeEnv;
        else
            throw new TypeException("Assignment Statement: right hand side and left hand side have different types ");
    }

}
