package Model.stmt;

import Exceptions.InvalidOperand;
import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IMyHeap;
import Model.exp.IExp;
import Model.types.BoolType;
import Model.types.IType;
import Model.value.BoolValue;
import Model.value.IValue;

public class WhileStmt implements IStmt {
    IExp e;
    IStmt s;

    public WhileStmt(IExp exp, IStmt stmt) {
        this.e = exp;
        this.s = stmt;
    }

    @Override
    public PrgState execute(PrgState state) {
        IDict<String, IValue> symTable = state.getSymTable();
        IMyHeap<Integer,IValue> heapTable = state.getHeapTable();

        IValue exprValue = e.eval(symTable, heapTable);
        if (exprValue.getType().equals(new BoolType())) {
            if (exprValue.equals(new BoolValue(true))) {
                state.getStack().push(new WhileStmt(e, s));
                state.getStack().push(s);
                return null;
            }
        } else {
            throw new InvalidOperand("Expresion is not bool!");
        }
        return null;
    }



    @Override
    public String toString() {
        return "while( " + e.toString() + " ) { " + s.toString() + " }";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        IType typeExp = e.typeCheck(typeEnv);
        if (typeExp.equals(new BoolType())) {
            s.typeCheck(typeEnv.deepcopy());
            return typeEnv;
        } else
            throw new TypeException("The condition of while doesn't have the type bool");
    }

}