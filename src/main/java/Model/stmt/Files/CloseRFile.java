package Model.stmt.Files;

import Exceptions.FileException;
import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.exp.IExp;
import Model.stmt.IStmt;
import Model.types.IType;
import Model.types.StringType;
import Model.value.IValue;
import Model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFile implements IStmt {
    private IExp exp;

    public CloseRFile(IExp exp) {
        this.exp = exp;
    }


    @Override
    public PrgState execute(PrgState state)  {
        IValue val = exp.eval(state.getSymTable(), state.getHeapTable());
        if (val.getType().equals(new StringType())) {
            StringValue stringValue = (StringValue) val;
            IDict<StringValue, BufferedReader> fileTable = state.getFileTable();

            if (!fileTable.exists(stringValue)) {
                throw new FileException("There is no file opened with that name!");
            } else {
                BufferedReader b = fileTable.getValue(stringValue);
                try {
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileTable.remove(stringValue);
            }
        } else {
            throw new FileException("Expression is not string type!");
        }
        return null;
    }



    @Override
    public String toString() {
        return "close(" + exp.toString() + ")";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        IType typeExp = exp.typeCheck(typeEnv);
        if (typeExp.equals(new StringType())) {
            return typeEnv;
        } else
            throw new TypeException("The path is not a string value");
    }

}
