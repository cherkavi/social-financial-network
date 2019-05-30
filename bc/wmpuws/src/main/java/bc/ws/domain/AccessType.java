package bc.ws.domain;

public enum AccessType {
    READ(1),
    WRITE(2),
    EXECUTE(9);

    private final int correspondValue;

    AccessType(int correspondValue){
        this.correspondValue=correspondValue;
    }

    public static AccessType getByInteger(Integer value){
        if(value==null){
            return null;
        }
        for(AccessType eachValue:values()){
            if(eachValue.correspondValue==value){
                return eachValue;
            }
        }
        return null;
    }
}
