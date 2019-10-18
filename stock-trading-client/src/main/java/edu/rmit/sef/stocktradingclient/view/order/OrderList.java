package edu.rmit.sef.stocktradingclient.view.order;


import edu.rmit.sef.order.command.GetAllOrderCmd;
import edu.rmit.sef.order.model.Order;
import edu.rmit.sef.stock.command.FindStockByIdCmd;
import edu.rmit.sef.stock.model.Stock;
import edu.rmit.sef.stocktradingclient.core.javafx.StyleHelper;
import edu.rmit.sef.stocktradingclient.view.JavaFXController;
import edu.rmit.sef.stocktradingclient.view.ViewNames;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableMapValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class OrderList extends JavaFXController {


    public GridPane root;
    private TableView orderTable;
    private ObservableList<OrderListData> orderList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        Text title = new Text("Order List");
        StyleHelper.h2(title);
        GridPane.setConstraints(title, 0, 0);


        orderTable = new TableView();
        getData();

        TableColumn<OrderListData, String> column1 = new TableColumn<>("Order ID");
        column1.setPrefWidth(200);
        column1.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getOrder().getId()));


        TableColumn<OrderListData, String> column2 = new TableColumn<>("Stock Symbol");
        column2.setPrefWidth(200);
        column2.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStockSymbol()));



        TableColumn<OrderListData, String> column3 = new TableColumn<>("Price");
        column3.setPrefWidth(100);
        column3.setCellValueFactory(param -> new SimpleStringProperty(Double.toString(param.getValue().getOrder().getPrice())));

        TableColumn<OrderListData, String> column4 = new TableColumn<>("Quantity");
        column4.setPrefWidth(100);
        column4.setCellValueFactory(param -> new SimpleStringProperty(Long.toString(param.getValue().getOrder().getQuantity())));

        TableColumn<OrderListData, String> column5 = new TableColumn<>("Order Type");
        column5.setPrefWidth(100);
        column5.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getOrder().getOrderType().toString()));

        TableColumn<OrderListData, String> column6 = new TableColumn<>("Order State");
        column6.setPrefWidth(200);
        column6.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getOrder().getOrderState().toString()));

        orderTable.getColumns().addAll(column1,column2,column3,column4,column5,column6);
        GridPane.setConstraints(orderTable, 0, 1);

        Button createBtn = new Button();
        createBtn.setText("CreateOrder");

        Button refreshbtn = new Button();
        refreshbtn.setText("Refresh");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.getChildren().addAll(createBtn,refreshbtn);
        GridPane.setConstraints(buttonBox, 0, 2);




        createBtn.setOnAction(event -> {
            getViewManager().showAppScreen(ViewNames.Order.CreateOrder);

        });

        refreshbtn.setOnAction(event -> {
            getData();
        });

        root.getChildren().addAll(title, buttonBox, orderTable);

    }

    public void getData() {

        GetAllOrderCmd cmd = new GetAllOrderCmd();
        int page = 0;
        cmd.setPageNumber(page);
        getCommandService().execute(cmd).thenAccept(OrderListResp -> {
            Platform.runLater(() -> {
                List<Order> list = OrderListResp.getOrderList();
                orderList = FXCollections.observableArrayList();
                if (list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Order order = list.get(i);
                        OrderListData orderListData = new OrderListData();
                        String stockID = order.getStockId();
                        FindStockByIdCmd findStockByIdCmd = new FindStockByIdCmd();
                        findStockByIdCmd.setId(stockID);
                        getCommandService().execute(findStockByIdCmd).join();
                        String stockSymbol = findStockByIdCmd.getResponse().getStock().getSymbol();

                        orderListData.setOrder(order);
                        orderListData.setStockSymbol(stockSymbol);

                        orderList.add(orderListData);

                    }
                }

                orderTable.setItems(orderList);
            });
        });

    }
}
