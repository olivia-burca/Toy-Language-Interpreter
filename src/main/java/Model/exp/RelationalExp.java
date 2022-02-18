package Model.exp;

import Exceptions.InvalidOperand;
import Exceptions.InvalidOperator;
import Exceptions.TypeException;
import Model.adt.IDict;
import Model.adt.IMyHeap;
import Model.types.BoolType;
import Model.types.IType;
import Model.types.IntType;
import Model.value.BoolValue;
import Model.value.IValue;
import Model.value.IntValue;

public class RelationalExp implements IExp {
    private IExp exp1,exp2;
    private String op;

    @Override
    public String toString() {
        return  this.exp1.toString() + this.op + this.exp2.toString();
    }

    public RelationalExp(IExp exp1, IExp exp2,String op) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.op = op;
    }

    @Override
    public IValue eval(IDict<String, IValue> table, IMyHeap<Integer,IValue> heap)  {
        IValue value1, value2;
        value1 = exp1.eval(table, heap);
        if (value1.getType().equals(new IntType())){
            value2 = exp2.eval(table, heap);
            if(value2.getType().equals(new IntType())){
                IntValue toInteger1 = (IntValue)value1;
                IntValue toInteger2 = (IntValue)value2;
                int number1,number2;
                number1 = toInteger1.getValue();
                number2 = toInteger2.getValue();
                switch (op){
                    case ">=":
                        return new BoolValue(number1>=number2);
                    case ">":
                        return new BoolValue(number1>number2);
                    case "<=":
                        return new BoolValue(number1<=number2);
                    case "<":
                        return new BoolValue(number1<number2);
                    case "==":
                        return new BoolValue(number1==number2);
                    case "!=":
                        return new BoolValue(number1!=number2);
                    default:
                        throw new InvalidOperator("Invalid operator");
                }
            }
            else throw new InvalidOperand("Second operand is not an int.");
        }
        else throw new InvalidOperand("First operand is not an int.");
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        IType typ1, typ2;
        typ1 = exp1.typeCheck(typeEnv);
        typ2 = exp2.typeCheck(typeEnv);
        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new BoolType();
            } else {
                throw new TypeException("Second operand is not an integer!");
            }
        } else {
            throw new TypeException("First operand is not an integer!");
        }
    }


}

