package Model.value;

import Model.types.IType;
import Model.types.RefType;

public class RefValue implements IValue {
    private int address;
    private IType locationType;

    public RefValue(int i, IType type) {
        this.address = i;
        this.locationType = type;
    }

    public int getAddress() {
        return this.address;
    }

    public IType getLocationType() {
        return locationType;
    }

    @Override
    public String toString() {
        return "(" + address + ", " + locationType + ")";
    }

    public IType getType() {
        return new RefType(locationType);
    }

    @Override
    public IValue deepCopy() {
        return new RefValue( address, locationType);
    }
}
