package com.hcmute.clothingstore.exception;

public class ResourceNotFoundException extends RuntimeException{


    String sourceName;
    String field;
    String fieldName;
    Long fieldId;



    public ResourceNotFoundException(String sourceName, String field, String fieldName) {
        super(String.format("%s can not be found with: %s is %s",sourceName,field,fieldName));
        this.sourceName = sourceName;
        this.field = field;
        this.fieldName = fieldName;
    }
    public ResourceNotFoundException( String sourceName, String field, Long fieldId) {
        super(String.format("%s can not be found with: %s is %s",sourceName,field,fieldId));
        this.sourceName = sourceName;
        this.field = field;
        this.fieldId = fieldId;
    }
}
