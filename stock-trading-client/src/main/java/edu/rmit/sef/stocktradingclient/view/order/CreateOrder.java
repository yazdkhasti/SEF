package edu.rmit.sef.stocktradingclient.view.order;



import edu.rmit.sef.order.command.CreateOrderCmd;
import edu.rmit.sef.stocktradingclient.core.javafx.controls.StyleHelper;
import edu.rmit.sef.stocktradingclient.view.JavaFXController;
import edu.rmit.sef.stocktradingclient.view.ViewNames;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class CreateOrder extends JavaFXController {


    public GridPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        Text title = new Text("Create Order");
        StyleHelper.h2(title);
        root.setConstraints(title, 0, 0);


        Label stockIDLabel = new Label();
        stockIDLabel.setText("StockID");
        root.setConstraints(stockIDLabel, 0, 1);


        TextField stockIDText = new TextField();
        stockIDText.setPromptText("StockID");
        root.setConstraints(stockIDText, 1, 1);

        Label priceLabel = new Label();
        priceLabel.setText("Price");
        root.setConstraints(priceLabel, 0, 2);


        TextField priceText = new TextField();
        priceText.setPromptText("Price");
        root.setConstraints(priceText, 1, 2);

        Label quantityLabel = new Label();
        quantityLabel.setText("Quantity");
        root.setConstraints(quantityLabel, 0, 3);


        TextField quantityText = new TextField();
        quantityText.setPromptText("Quantity");
        root.setConstraints(quantityText, 1, 3);

        Button createBtn = new Button();
        createBtn.setText("Create");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.getChildren().addAll(createBtn);
        root.setConstraints(buttonBox, 1, 4);

        Text errorLabel = new Text();
        errorLabel.setText("Invalid Order");
        errorLabel.setVisible(false);
        StyleHelper.error(errorLabel);
        root.setConstraints(errorLabel, 1, 5);


        createBtn.setOnAction(event -> {
            createBtn.setDisable(true);
            CreateOrderCmd createOrderCmd = new CreateOrderCmd();
            createOrderCmd.setStockId(stockIDText.getText());
            createOrderCmd.setQuantity((Integer.parseInt(quantityText.getText())));
            createOrderCmd.setPrice(Double.valueOf(priceLabel.getText()));

            getCommandService().execute(createOrderCmd).whenComplete((CreateEntityResp, ex) -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.show();
            });

        });

        root.getChildren().addAll(title,stockIDLabel, stockIDText, priceLabel, priceText, quantityLabel, quantityText, buttonBox, errorLabel);

    }
}
