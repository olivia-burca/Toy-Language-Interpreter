package Model.stmt;

import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.ExeStack;
import Model.adt.IDict;
import Model.adt.IStack;
import Model.types.IType;

public class ForkStmt implements IStmt {

    IStmt statement;

    public ForkStmt(IStmt statement) {
        this.statement = statement;
    }


    @Override
    public PrgState execute(PrgState state) {



        IStack<IStmt> newStack = new ExeStack<>();
        newStack.push(statement);

        PrgState newPrg =  new PrgState(newStack,state.getSymTable().deepcopy(),state.getOutput(),this.statement,state.getFileTable(),state.getHeapTable());
        return newPrg;
    }

    @Override
    public String toString(){
        return "fork("+statement+") ";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        return statement.typeCheck(typeEnv);
    }
}
