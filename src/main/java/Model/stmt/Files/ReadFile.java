package Model.stmt.Files;

import Exceptions.FileException;
import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IMyHeap;
import Model.exp.IExp;
import Model.stmt.IStmt;
import Model.types.IType;
import Model.types.IntType;
import Model.types.StringType;
import Model.value.IValue;
import Model.value.IntValue;
import Model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFile implements IStmt {
    private IExp exp;
    private String varName;

    public ReadFile(IExp exp, String var) {
        this.exp = exp;
        this.varName = var;
    }

    public String getVarName() {
        return varName;
    }

    public IExp getPath() {
        return exp;
    }

    @Override
    public PrgState execute(PrgState state)  {
        IDict<String, IValue> symTable = state.getSymTable();
        IMyHeap<Integer,IValue>  heap = state.getHeapTable();
        if (symTable.exists(varName)) {
            IValue val = symTable.getValue(varName);
            if (val.getType().equals(new IntType())) {
                IValue pathVal = exp.eval(symTable, heap);
                if (pathVal.getType().equals(new StringType())) {

                    if (state.getFileTable().exists((StringValue) pathVal)) {
                        BufferedReader b = state.getFileTable().getValue((StringValue) pathVal);
                        try {
                            String line = b.readLine();
                            if (line == null) {
                                state.getSymTable().put(varName, new IntValue(0));
                            } else {
                                state.getSymTable().put(varName, new IntValue(Integer.parseInt(line)));
                            }
                            state.getFileTable().put((StringValue) pathVal, b);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        throw new FileException("There is no file in fileTable with that name!");
                    }
                } else {
                    throw new FileException("Path variable is not a string type!");
                }

            } else {
                throw new FileException("Variable is not an integer!");
            }
        } else {
            throw new FileException("Variable is not declared");
        }
        return null;
    }



    @Override
    public String toString() {
        return "readFile(" + exp.toString() + ", " + varName + ")";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        IType typeExp = exp.typeCheck(typeEnv);
        if (typeExp.equals(new StringType())) return typeEnv;
        else throw new TypeException("The path is not a string value");
    }
}
