package edu.rmit.sef.stocktradingclient.view.order;


import edu.rmit.sef.order.command.CreateOrderCmd;
import edu.rmit.sef.order.command.GetAllOrderCmd;
import edu.rmit.sef.order.model.Order;
import edu.rmit.sef.stocktradingclient.core.javafx.controls.StyleHelper;
import edu.rmit.sef.stocktradingclient.view.JavaFXController;
import edu.rmit.sef.stocktradingclient.view.ViewNames;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class OrderList extends JavaFXController {


    public GridPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        Text title = new Text("Order List");
        StyleHelper.h2(title);
        root.setConstraints(title, 0, 0);


        Label stockIDLabel = new Label();
        stockIDLabel.setText("StockID");
        root.setConstraints(stockIDLabel, 0, 1);

        Label priceLabel = new Label();
        priceLabel.setText("Price");
        root.setConstraints(priceLabel, 1, 1);


        Label quantityLabel = new Label();
        quantityLabel.setText("Quantity");
        root.setConstraints(quantityLabel, 2, 1);


        Button createBtn = new Button();
        createBtn.setText("CreateOrder");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.getChildren().addAll(createBtn);
        root.setConstraints(buttonBox, 1, 4);


        GetAllOrderCmd cmd = new GetAllOrderCmd();
        int size = 10;
        int page = 1;
        cmd.setPage(page);
        cmd.setSize(size);
        getCommandService().execute(cmd).thenAccept(OrderListResp -> {

            for (int i = 0; i < size; i++) {
                Label stockIDText = new Label();
                root.setConstraints(stockIDText, 0, 1+i);
                List<Order> orderList = OrderListResp.getOrderList();
                stockIDText.setText(orderList.get(i).getStockId());
                root.getChildren().addAll(stockIDText);
            }
        });

        createBtn.setOnAction(event -> {
            getViewManager().showAppScreen(ViewNames.Order.CreateOrder);

        });


        root.getChildren().addAll(title,stockIDLabel, priceLabel, quantityLabel, buttonBox);

    }
}
