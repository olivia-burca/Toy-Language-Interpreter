package Model.exp;
import Exceptions.TypeException;
import Model.adt.IDict;
import Model.adt.IMyHeap;
import Model.types.IType;
import Model.value.IValue;

public interface IExp {
     IValue eval(IDict<String, IValue> d, IMyHeap<Integer,IValue> heap);
     IType typeCheck(IDict<String, IType> typeEnv) throws TypeException;
}
