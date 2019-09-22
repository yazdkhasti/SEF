package edu.rmit.sef.stocktradingclient.view.stock;

import edu.rmit.sef.stock.command.ApproveStockCmd;
import edu.rmit.sef.stock.command.DisableStockCmd;
import edu.rmit.sef.stock.command.GetAllStocksCmd;
import edu.rmit.sef.stock.model.Stock;
import edu.rmit.sef.stocktradingclient.core.javafx.ActionButtonCell;
import edu.rmit.sef.stocktradingclient.core.javafx.StyleHelper;
import edu.rmit.sef.stocktradingclient.core.javafx.TableAction;
import edu.rmit.sef.stocktradingclient.core.javafx.TempData;
import edu.rmit.sef.stocktradingclient.stock.StockEvents;
import edu.rmit.sef.stocktradingclient.view.JavaFXController;
import edu.rmit.sef.stocktradingclient.view.ViewNames;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class StockList extends JavaFXController {


    public VBox root;
    private TableView stocksTable;
    private int pageNumber;

    @Autowired
    StockEvents stockEvents;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);


        Text header = new Text("The list of stocks available for trade");
        StyleHelper.h2(header);
        root.getChildren().add(header);


        HBox toolbar = new HBox();
        root.getChildren().add(toolbar);


        if (getPermissionManager().isAdmin()) {
            Button addStockBtn = new Button("Add Stock");
            addStockBtn.setOnAction(event -> {
                getViewManager().openModal(ViewNames.Stock.Stock, null);
            });
            toolbar.getChildren().add(addStockBtn);
        }


        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(event -> {
            getData();
        });
        toolbar.getChildren().add(refreshBtn);


        stocksTable = new TableView();

        TableColumn<Stock, String> column1 = new TableColumn<>("Name");
        column1.setPrefWidth(300);
        column1.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));


        TableColumn<Stock, String> column2 = new TableColumn<>("Symbol");
        column2.setPrefWidth(100);
        column2.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSymbol()));


        TableColumn<Stock, String> column3 = new TableColumn<>("Price");
        column3.setPrefWidth(100);
        column3.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getPrice())));


        TableColumn<Stock, String> column4 = new TableColumn<>("State");
        column4.setPrefWidth(200);
        column4.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStockState().toString()));


        stocksTable.getColumns().add(column1);
        stocksTable.getColumns().add(column2);
        stocksTable.getColumns().add(column3);
        stocksTable.getColumns().add(column4);


        if (getPermissionManager().isAdmin()) {

            List<TableAction<Stock>> tableActions = new ArrayList<>();


            TableAction<Stock> approveAction = new TableAction<>((stock) -> {
                Button btn = new Button("Approve");
                btn.disableProperty().bindBidirectional(new SimpleBooleanProperty(!stock.validForApprove()));
                return btn;
            }, stock -> {

                ApproveStockCmd approveStockCmd = new ApproveStockCmd();
                approveStockCmd.setStockId(stock.getId());
                getCommandService().execute(approveStockCmd).thenAccept(resp -> {
                    getData();
                });


            });
            tableActions.add(approveAction);


            TableAction<Stock> disableAction = new TableAction<>((stock) -> {
                Button btn = new Button("Disable");
                btn.disableProperty().bindBidirectional(new SimpleBooleanProperty(!stock.validForDisable()));
                return btn;
            }, stock -> {

                DisableStockCmd disableStockCmd = new DisableStockCmd();
                disableStockCmd.setStockId(stock.getId());
                getCommandService().execute(disableStockCmd).thenAccept(resp -> {
                    getData();
                });

            });

            tableActions.add(disableAction);


            TableAction<Stock> edit = new TableAction<>((stock) -> {
                Button btn = new Button("Edit");
                btn.disableProperty().bindBidirectional(new SimpleBooleanProperty(!stock.validForUpdate()));
                return btn;
            }, stock -> {
                TempData tempData = new TempData(stock);
                getViewManager().openModal(ViewNames.Stock.Stock, tempData);
            });
            tableActions.add(edit);


            TableColumn<Stock, HBox> column5 = new TableColumn<>("Actions");
            column5.setPrefWidth(300);
            column5.setCellFactory(ActionButtonCell.forTableColumn(tableActions));

            stocksTable.getColumns().add(column5);

        }


        root.getChildren().add(stocksTable);

        stockEvents.STOCK_ADDED.subscribe(entityChangedArg -> {
            getData();
        });

        stockEvents.STOCK_UPDATED.subscribe(entityChangedArg -> {
            getData();
        });


    }

    @Override
    public void getData() {

        GetAllStocksCmd getAllStocksCmd = new GetAllStocksCmd();
        getAllStocksCmd.setPageNumber(pageNumber);

        getCommandService().execute(getAllStocksCmd).thenAccept((result) -> {
            Platform.runLater(() -> {
                stocksTable.getItems().clear();
                stocksTable.refresh();
                stocksTable.getItems().addAll(result.getResult());
            });

        });
    }
}
