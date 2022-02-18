package Model.exp;
import Exceptions.TypeException;
import Model.adt.IDict;
import Model.adt.IMyHeap;
import Model.types.IType;
import Model.value.IValue;

public class ValueExp implements IExp {
    IValue value;
    public ValueExp(IValue val) {
        value = val;
    }
    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public IValue eval(IDict<String, IValue> d, IMyHeap<Integer,IValue> heap) {
        return value;
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        return value.getType();
    }
}
