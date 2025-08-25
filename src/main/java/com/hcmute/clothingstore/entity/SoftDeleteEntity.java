package com.hcmute.clothingstore.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@FilterDef(name="deletedFilter", parameters = @ParamDef(name="isDeleted", type = Boolean.class))
@Filter(name="deletedFilter", condition = "is_deleted = :isDeleted")
public class SoftDeleteEntity extends AbstractEntity{

    private boolean isDeleted = false;
}
