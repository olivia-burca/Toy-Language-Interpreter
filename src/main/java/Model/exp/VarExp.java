package Model.exp;
import Exceptions.TypeException;
import Model.adt.IDict;
import Model.adt.IMyHeap;
import Model.types.IType;
import Model.value.IValue;

public class VarExp implements IExp{
    String id;

    public VarExp(String _id){
        id = _id;
    }

    @Override
    public IValue eval(IDict<String,IValue> symTable, IMyHeap<Integer,IValue> heap) {
        return symTable.getValue(id);
    }

    public String toString() {return id;}

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        return typeEnv.getValue(id);
    }

}
