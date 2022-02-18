package Model.stmt.Heap;


import Exceptions.HeapException;
import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IMyHeap;
import Model.exp.IExp;
import Model.stmt.IStmt;
import Model.types.IType;
import Model.types.RefType;
import Model.value.IValue;
import Model.value.RefValue;

public class HeapWriting implements IStmt {
    String variableName;
    IExp e;

    public HeapWriting(String variableName, IExp e) {
        this.variableName = variableName;
        this.e = e;
    }

    @Override
    public PrgState execute(PrgState state)  {
        IDict<String, IValue> symTable=state.getSymTable();
        IMyHeap<Integer,IValue> heap=state.getHeapTable();
        if(symTable.exists(variableName)){
            if( symTable.getValue(variableName).getType() instanceof RefType){
                RefValue val_var_name= (RefValue) symTable.getValue(variableName);
                int addr=val_var_name.getAddress();
                if(heap.isDefined(addr)) {
                    IValue v = this.e.eval(symTable, heap);
                    RefType referenceType = (RefType)symTable.getValue(variableName).getType() ;
                    IType innerType= referenceType.getInner();
                    if(v.getType().equals(innerType))
                    {
                        heap.update(addr, v);
                    } else throw new HeapException("The type of the variable and the type of the expression do not match");
                }else
                    throw new HeapException("The address doesn't exist");

            }else
                throw new HeapException("The type of the variable is not reference type");

        } else throw new HeapException("The variable is not defined in the symbol table");


        return null;

    }


    @Override
    public String toString() {
        return "wH(" + variableName + ", " + e.toString() + ")";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        IType typeVar = typeEnv.getValue(variableName);
        IType typeExp = e.typeCheck(typeEnv);
        if (new RefType(typeExp).equals(typeVar)) return typeEnv;
        else throw new TypeException("Write heap: left side is not of type ref ");
    }

}
