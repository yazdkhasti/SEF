package edu.rmit.sef.core.command;

public class CreateEntityResp  {

    private String Id;

    public CreateEntityResp() {

    }
    public CreateEntityResp(String id) {
        this.Id = id;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

}
