package Model.stmt;


import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.types.IType;
import Model.value.IValue;

public class VarDeclStmt implements IStmt{
    String name;
    IType type;

    public VarDeclStmt(String name, IType type){
        this.name = name;
        this.type = type;
    }
    @Override
    public PrgState execute(PrgState state) {
        IDict<String, IValue> dict = state.getSymTable();
        dict.update(name, type.defaultValue());

        return null;
    }
    @Override
    public String toString ()
    {
        return type.toString() + ' ' + name + "; ";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        typeEnv.put(name, type);
        return typeEnv;
    }
}
