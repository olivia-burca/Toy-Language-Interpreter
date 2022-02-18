package Model.exp;
import Exceptions.DivisionByZero;
import Exceptions.InvalidOperator;
import Exceptions.TypeException;
import Model.adt.IDict;
import Model.adt.IMyHeap;
import Model.types.IType;
import Model.types.IntType;
import Model.value.IValue;
import Model.value.IntValue;

public class ArithExp implements IExp{
    char op;
    IExp e1, e2;

    //constructor
    public ArithExp(char _op, IExp _e1, IExp _e2){
        op = _op;
        e1 = _e1;
        e2 = _e2;
    }

    @Override
    public IValue eval(IDict<String, IValue> symTable, IMyHeap<Integer,IValue> heap) {

        IValue ve1 = e1.eval(symTable, heap);
        IValue ve2 = e2.eval(symTable, heap);

        IntValue toInt1 = (IntValue) ve1;
        IntValue toInt2 = (IntValue) ve2;
        int nr1, nr2;
        nr1 = toInt1.getValue();
        nr2 = toInt2.getValue();
        switch (op){
            case '+':
                return new IntValue(nr1 + nr2);
            case '-':
                return new IntValue(nr1 - nr2);
            case '*':
                return new IntValue(nr1 * nr2);
            case '/':
                if(nr2 == 0)
                    throw new DivisionByZero("You cannot divide by 0");
                return new IntValue(nr1 / nr2);
        }

        throw new InvalidOperator("Invalid Operator, please use only [+, -, *, /]");
    }

    public char getOp() {return this.op;}

    public IExp getFirst() {
        return this.e1;
    }

    public IExp getSecond() {
        return this.e2;
    }

    @Override
    public String toString(){
        return "" + e1 + op + e2;
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws TypeException {
        IType typ1, typ2;
        typ1 = e1.typeCheck(typeEnv);
        typ2 = e2.typeCheck(typeEnv);
        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new IntType();
            } else {
                throw new TypeException("Second operand is not an integer!");
            }
        } else {
            throw new TypeException("First operand is not an integer!");
        }
    }
}
