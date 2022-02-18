package Dtos;


import Model.value.IValue;

public class HeapDto {
    private Integer address;
    private IValue value;

    public HeapDto(Integer adr, IValue val){
        this.address = adr;
        this.value = val;
    }

    public Integer getAddress() {
        return address;
    }

    public IValue getValue() {
        return value;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }

    public void setValue(IValue value) {
        this.value = value;
    }
}