package Model.stmt;

import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.types.IType;

public interface IStmt {
    public PrgState execute(PrgState state);
    IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws TypeException;

}
