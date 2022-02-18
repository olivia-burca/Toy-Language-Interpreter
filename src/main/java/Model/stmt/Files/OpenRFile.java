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
import java.io.FileNotFoundException;
import java.io.FileReader;


public class OpenRFile implements IStmt {
    IExp exp;

    public OpenRFile(IExp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state)  {
        IValue val = exp.eval(state.getSymTable(), state.getHeapTable());
        if (val.getType().equals(new StringType())) {
            String path = ((StringValue) val).getValue();
            try {
                BufferedReader b = new BufferedReader(new FileReader(path));
                IDict<StringValue, BufferedReader> fileTable = state.getFileTable();
                if (fileTable.exists((StringValue) val)) {
                    throw new FileException("File already opened!");
                }
                fileTable.put((StringValue) val, b);
            } catch (FileException | FileNotFoundException e) {
                throw new FileException("Error while trying to open the file\n");
            }
        } else {
            throw new FileException("Expression is not a string type!");
        }
        return null;
    }



    @Override
    public String toString() {
        return "openFile(" + exp.toString() + ")";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        IType typeExp = exp.typeCheck(typeEnv);
        if (typeExp.equals(new StringType())) return typeEnv;
        else throw new TypeException("The path is not a string value");
    }

}
