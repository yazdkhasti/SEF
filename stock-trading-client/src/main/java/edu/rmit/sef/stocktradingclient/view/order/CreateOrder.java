package edu.rmit.sef.stocktradingclient.view.order;


import edu.rmit.sef.order.command.CreateOrderCmd;
import edu.rmit.sef.order.model.OrderType;
import edu.rmit.sef.stock.command.GetAllStocksCmd;
import edu.rmit.sef.stock.model.Stock;
import edu.rmit.sef.stock.model.StockState;
import edu.rmit.sef.stocktradingclient.core.javafx.StyleHelper;
import edu.rmit.sef.stocktradingclient.view.JavaFXController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Component
public class CreateOrder extends JavaFXController {


    public GridPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        Text title = new Text("Create Order");
        StyleHelper.h2(title);
        GridPane.setConstraints(title, 0, 0);


        Label stockSymbolLabel = new Label();
        stockSymbolLabel.setText("Stock Symbol");
        GridPane.setConstraints(stockSymbolLabel, 0, 1);


        ComboBox<StockSymbolData> stockSymbolBox = new ComboBox<>();
        stockSymbolBox.setItems(getStockSymbolList());
        GridPane.setConstraints(stockSymbolBox, 1, 1);


        Label priceLabel = new Label();
        priceLabel.setText("Price");
        GridPane.setConstraints(priceLabel, 0, 2);


        TextField priceText = new TextField();
        priceText.setPromptText("Price");
        GridPane.setConstraints(priceText, 1, 2);

        Label quantityLabel = new Label();
        quantityLabel.setText("Quantity");
        GridPane.setConstraints(quantityLabel, 0, 3);


        TextField quantityText = new TextField();
        quantityText.setPromptText("Quantity");
        GridPane.setConstraints(quantityText, 1, 3);

        Label orderType = new Label();
        quantityLabel.setText("Order Type");
        GridPane.setConstraints(quantityLabel, 0, 4);


        ChoiceBox<OrderType> orderTypeBox = new ChoiceBox<OrderType>(FXCollections.observableArrayList(
                OrderType.Sell, OrderType.Buy
        ));
        GridPane.setConstraints(orderTypeBox, 1, 4);

        Button createBtn = new Button();
        createBtn.setText("Create");

        Button cancleBtn = new Button();
        cancleBtn.setText("Cancle");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.getChildren().addAll(createBtn,cancleBtn);
        GridPane.setConstraints(buttonBox, 1, 5);

        Text errorLabel = new Text();
        errorLabel.setText("Invalid Order");
        errorLabel.setVisible(false);
        StyleHelper.error(errorLabel);
        GridPane.setConstraints(errorLabel, 1, 6);

        HashMap<String, Stock> map = getStockList();

        cancleBtn.setOnAction(event -> {
            close();
        });

        createBtn.setOnAction(event -> {
            String symbol = stockSymbolBox.getValue().getSymbol();
            String stockID = map.get(symbol).getId();
            CreateOrderCmd createOrderCmd = new CreateOrderCmd();
            createOrderCmd.setStockId(stockID);
            createOrderCmd.setQuantity((Long.valueOf(quantityText.getText())));
            createOrderCmd.setPrice(Double.valueOf(priceText.getText()));
            createOrderCmd.setOrderType(orderTypeBox.getValue());

            getCommandService().execute(createOrderCmd).whenComplete((CreateEntityResp, ex) -> {
                createBtn.setDisable(true);
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//                alert.show();
                if (createOrderCmd.getResponse() != null) {
                    close();
                } else {
                    errorLabel.setVisible(true);
                }
            });

        });

        root.getChildren().addAll(title, stockSymbolLabel, stockSymbolBox, priceLabel, priceText, quantityLabel, quantityText, buttonBox, errorLabel, orderType, orderTypeBox);

    }

    public ObservableList<StockSymbolData> getStockSymbolList() {

        ObservableList<StockSymbolData> list = FXCollections.observableArrayList();
        HashMap<String, Stock> map = getStockList();
        for (Map.Entry<String, Stock> entry : map.entrySet()) {
            Stock stock = entry.getValue();
            StockSymbolData stockSymbolData = new StockSymbolData(stock.getSymbol(),stock.getPrice());
            list.add(stockSymbolData);
        }
        return list;
    }

    public HashMap<String, Stock> getStockList() {
        HashMap<String, Stock> map = new HashMap<String, Stock>();
        GetAllStocksCmd getAllStocksCmd = new GetAllStocksCmd();
        getCommandService().execute(getAllStocksCmd).join();
        List<Stock> stockList = getAllStocksCmd.getResponse().getResult();

        for (int i = 0; i < stockList.size(); i++) {
            if (stockList.get(i).getStockState() == StockState.OnTrade) {
                map.put(stockList.get(i).getSymbol(), stockList.get(i));
            }
        }
        return map;
    }
}
