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

public class HeapAllocation implements IStmt {
    private String varName;
    IExp expr;

    public HeapAllocation(String varName, IExp expr) {
        this.varName = varName;
        this.expr = expr;
    }


    @Override
    public PrgState execute(PrgState state) {
        IDict<String, IValue> symTable=state.getSymTable();
        IMyHeap<Integer,IValue> heap=state.getHeapTable();
        if(symTable.exists(varName)){
            if( symTable.getValue(varName).getType() instanceof RefType){
                IValue v=expr.eval(symTable,heap);
                RefType refvar= (RefType) symTable.getValue(varName).getType();
                if(v.getType().equals(refvar.getInner())){
                    heap.add(v);
                    RefValue new_refval=new RefValue(heap.getAddress(v),v.getType());
                    symTable.update(varName,new_refval);

                }
                else throw new HeapException("The type of the variable and the type of the expression do not match");

            } else
                throw new HeapException("The type of the variable is not a reference type");

        }
        else
            throw new HeapException("The variable name is not defined in the symbol table");

        return null;

    }



    @Override
    public String toString() {
        return "new(" + varName + ", " + expr.toString() + ")";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        IType typeVar = typeEnv.getValue(varName);
        IType typeExp = expr.typeCheck(typeEnv);
        if (typeVar.equals(new RefType(typeExp))) return typeEnv;
        else
            throw new TypeException("Heap Allocation Statement: right hand side and left hand side have different types.");
    }

}
