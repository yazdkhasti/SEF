package edu.rmit.sef.stocktradingserver.test.core.command;

public class CreateEntityResp  {

    private String Id;

    public CreateEntityResp(String id) {
        super();
        this.Id = id;
    }

    public String getId() {
        return Id;
    }

}
