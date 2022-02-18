package Model.exp;


import Exceptions.InvalidOperand;
import Exceptions.TypeException;
import Model.adt.IDict;
import Model.adt.IMyHeap;
import Model.types.BoolType;
import Model.types.IType;
import Model.value.BoolValue;
import Model.value.IValue;

public class LogicExp implements IExp {
    private IExp exp1,exp2;
    private String op;

    public LogicExp(IExp exp1, IExp exp2, String op) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.op = op;
    }

    @Override
    public IValue eval(IDict<String, IValue> table, IMyHeap<Integer,IValue> heap) {
        IValue v1,v2;
        v1 = exp1.eval(table,heap);
        if (v1.getType().equals(new BoolType())){
            v2 = exp2.eval(table,heap);
            if (v2.getType().equals(new BoolType())){
                BoolValue i1 = (BoolValue)v1;
                BoolValue i2 = (BoolValue)v2;
                boolean n1,n2;
                n1 = i1.getValue();
                n2 = i2.getValue();
                if (op=="and"){
                    return new BoolValue(n1 && n2);
                }
                if(op=="or"){
                    return new BoolValue(n1 || n2);
                }
                else throw new InvalidOperand("Invalid operand");
            }
            else
                throw new InvalidOperand("Second operand is not a bool.");
        }
        else
            throw new InvalidOperand("First second operand is not a bool.");
    }


    @Override
    public String toString() {
        return this.exp1.toString() + " " + this.op + " " + this.exp2.toString();
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        IType typ1, typ2;
        typ1 = exp1.typeCheck(typeEnv);
        typ2 = exp2.typeCheck(typeEnv);
        if (typ1.equals(new BoolType())) {
            if (typ2.equals(new BoolType())) {
                return new BoolType();
            } else {
                throw new TypeException("Second operand is not a boolean!");
            }
        } else {
            throw new TypeException("First operand is not a boolean!");
        }
    }


}