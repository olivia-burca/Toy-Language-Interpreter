package Model.exp.Heap;

import Exceptions.HeapException;
import Exceptions.TypeException;
import Model.adt.IDict;
import Model.adt.IMyHeap;
import Model.exp.IExp;
import Model.types.IType;
import Model.types.RefType;
import Model.value.IValue;
import Model.value.RefValue;

public class HeapReading implements IExp {
    IExp e;

    public HeapReading(IExp e) {
        this.e = e;

    }

    @Override
    public IValue eval(IDict<String, IValue> table, IMyHeap<Integer,IValue> heap) {
        IValue val=this.e.eval(table,heap);
        IValue valuefromHeap;
        if(val.getType() instanceof RefType){

            RefValue refValue= (RefValue) val;
            int addr=refValue.getAddress();
            if(heap.isDefined(addr)){
                valuefromHeap=heap.getValue(addr);
            } else throw new HeapException("the address does not exist in the heap");

        }else throw new HeapException("Not ref type");
        return valuefromHeap;
    }


    @Override
    public String toString() {
        return "rH(" + e.toString() + ")";
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        IType typ = e.typeCheck(typeEnv);
        if (typ instanceof RefType) {
            RefType refType = (RefType) typ;
            return refType.getInner();
        } else
            throw new TypeException("The heap reading argument is not a Ref Type");
    }

}
