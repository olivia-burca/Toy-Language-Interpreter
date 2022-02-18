package Model.stmt;

import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IMyHeap;
import Model.adt.IStack;
import Model.exp.IExp;
import Model.types.BoolType;
import Model.types.IType;
import Model.value.BoolValue;
import Model.value.IValue;

public class IfStmt implements IStmt {

    private IExp exp;
    private IStmt thenS;
    private IStmt elseS;

    public IfStmt(IExp _exp, IStmt _thenS, IStmt _elseS){
        exp = _exp;
        thenS = _thenS;
        elseS = _elseS;
    }

    @Override
    public PrgState execute(PrgState state) {
        IDict <String, IValue> dict = state.getSymTable();
        IStack<IStmt> stack = state.getStack();
        IMyHeap<Integer,IValue> heap = state.getHeapTable();
        IValue val = exp.eval(dict, heap);

        //if(val != 0)
        if(((BoolValue) val).getValue())
            stack.push(thenS);
        else
            stack.push(elseS);

        return null;
    }

    @Override
    public String toString(){
        return "IF("+ exp + ") THEN(" +thenS + ")ELSE(" + elseS + ") ;";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        IType typexp = exp.typeCheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            thenS.typeCheck(typeEnv.deepcopy());
            elseS.typeCheck(typeEnv.deepcopy());
            return typeEnv;
        } else throw new TypeException("The condition of IF doesn't have the type bool");
    }

}