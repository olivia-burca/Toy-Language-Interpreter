package Dtos;


import Model.value.IValue;

public class SymDto {
    private String varName;
    private IValue value;

    public SymDto(String varName, IValue value) {
        this.varName = varName;
        this.value = value;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public IValue getValue() {
        return value;
    }

    public void setValue(IValue value) {
        this.value = value;
    }
}