package edu.rmit.sef.core.model;

import edu.rmit.command.core.CommandUtil;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.UUID;

public abstract class Entity {

    @Id
    private String id;
    private Date createdOn;
    private Date modifiedOn;
    private String createdBy;
    private String modifiedBy;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public static <T extends Entity> T newEntity(String userId, Class<T> tClass) {
        try {
            T entity = tClass.newInstance();
            Date date = new Date();
            entity.setCreatedBy(userId);
            entity.setModifiedBy(userId);
            entity.setCreatedOn(date);
            entity.setModifiedOn(date);
            entity.setId(UUID.randomUUID().toString());
            return entity;
        } catch (Exception ex) {
            CommandUtil.throwCommandExecutionException(ex);
        }
        return null;
    }

}
