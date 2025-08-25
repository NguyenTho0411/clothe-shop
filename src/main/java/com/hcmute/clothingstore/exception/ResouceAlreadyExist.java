package com.hcmute.clothingstore.exception;

public class ResouceAlreadyExist extends RuntimeException{
    String sourceName;
    String field;
    String fieldName;

    Long fieldId;


    public ResouceAlreadyExist(String sourceName, String field, String fieldName) {
        super(String.format("%s has aalready existed with %: %", sourceName,field,fieldName));
        this.sourceName = sourceName;
        this.field = field;
        this.fieldName = fieldName;
    }
    public ResouceAlreadyExist(String sourceName, String field, Long fieldId) {
        super(String.format("%s has aalready existed with %: %", sourceName,field,fieldId));
        this.sourceName = sourceName;
        this.field = field;
        this.fieldName = fieldName;
    }
}
