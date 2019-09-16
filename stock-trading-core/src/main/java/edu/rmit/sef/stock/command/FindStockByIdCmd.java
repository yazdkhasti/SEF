package edu.rmit.sef.stock.command;

        import edu.rmit.command.core.Command;

public class FindStockByIdCmd extends Command<FindStockByIdResp> {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
