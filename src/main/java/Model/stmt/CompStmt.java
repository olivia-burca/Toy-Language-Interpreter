package Model.stmt;

import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IStack;
import Model.types.IType;

public class CompStmt implements IStmt{

    private IStmt first, second;
    public CompStmt(IStmt f, IStmt s){
        first = f;
        second = s;
    }

    @Override
    public PrgState execute(PrgState state) {
        IStack<IStmt> stack = state.getStack();
        stack.push(second);
        stack.push(first);

        return null;
    }

    @Override
    public String toString(){
        return "(" + first + second + ")";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        return second.typeCheck(first.typeCheck(typeEnv));
    }
}
