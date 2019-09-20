package edu.rmit.sef.stocktradingclient.view.stock;

import edu.rmit.sef.stock.command.AddStockCmd;
import edu.rmit.sef.stock.command.UpdateStockCmd;
import edu.rmit.sef.stock.model.Stock;
import edu.rmit.sef.stocktradingclient.core.event.EntityChangedArg;
import edu.rmit.sef.stocktradingclient.core.javafx.StyleHelper;
import edu.rmit.sef.stocktradingclient.core.javafx.TempData;
import edu.rmit.sef.stocktradingclient.stock.StockEvents;
import edu.rmit.sef.stocktradingclient.view.JavaFXController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockEdit extends JavaFXController {

    public GridPane root;

    @Autowired
    StockEvents stockEvents;

    @Override
    public void initialize(Stage stage, Scene scene) {
        super.initialize(stage, scene);

        Text title = new Text("Add/Edit Stock");
        StyleHelper.h2(title);
        GridPane.setConstraints(title, 0, 0);


        Label stockNameLbl = new Label();
        stockNameLbl.setText("Stock Name");
        GridPane.setConstraints(stockNameLbl, 0, 1);


        TextField stockNameTxt = new TextField();
        stockNameTxt.setPromptText("name");
        GridPane.setConstraints(stockNameTxt, 1, 1);

        Label stockSymbolLbl = new Label();
        stockSymbolLbl.setText("Stock Symbol");
        GridPane.setConstraints(stockSymbolLbl, 0, 2);


        TextField stockSymbolTxt = new TextField();
        stockSymbolTxt.setPromptText("symbol");
        GridPane.setConstraints(stockSymbolTxt, 1, 2);


        Label stockPriceLbl = new Label();
        stockPriceLbl.setText("Stock Price");
        GridPane.setConstraints(stockPriceLbl, 0, 3);


        TextField stockPriceTxt = new TextField();
        stockPriceTxt.setPromptText("price");
        GridPane.setConstraints(stockPriceTxt, 1, 3);


        Button saveBtn = new Button();
        saveBtn.setText("Save");


        Button closeBtn = new Button();
        closeBtn.setText("Close");
        closeBtn.setOnAction(event -> {
            stage.close();
        });

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.getChildren().addAll(saveBtn, closeBtn);
        GridPane.setConstraints(buttonBox, 1, 4);

        Text errorLabel = new Text();
        errorLabel.setText("Error occurred");
        errorLabel.setVisible(false);
        StyleHelper.error(errorLabel);
        GridPane.setConstraints(errorLabel, 1, 5);


        root.getChildren().addAll(title, stockNameLbl, stockNameTxt, stockSymbolLbl, stockSymbolTxt, stockPriceLbl, stockPriceTxt, buttonBox, errorLabel);


        TempData tempData = getTempData();


        if (tempData != null) {
            Stock stock = tempData.getPrimaryData();
            stockNameTxt.setText(stock.getName());
            stockSymbolTxt.setText(stock.getSymbol());
            stockPriceTxt.setText(String.valueOf(stock.getPrice()));
        }

        saveBtn.setOnAction(event -> {

            if (tempData == null) {

                AddStockCmd addStockCmd = new AddStockCmd();
                addStockCmd.setName(stockNameTxt.getText());
                addStockCmd.setSymbol(stockSymbolTxt.getText());
                addStockCmd.setPrice(Double.valueOf(stockPriceTxt.getText()));

                getCommandService().execute(addStockCmd).whenComplete((resp, throwable) -> {
                    stockEvents.STOCK_ADDED.publish(new EntityChangedArg());
                    close();
                });

            } else {

                Stock stock = tempData.getPrimaryData();

                UpdateStockCmd updateStockCmd = new UpdateStockCmd();
                updateStockCmd.setStockId(stock.getId());
                updateStockCmd.setName(stockNameTxt.getText());
                updateStockCmd.setSymbol(stockSymbolTxt.getText());
                updateStockCmd.setPrice(Double.valueOf(stockPriceTxt.getText()));

                getCommandService().execute(updateStockCmd).whenComplete((resp, throwable) -> {
                    stockEvents.STOCK_UPDATED.publish(new EntityChangedArg());
                    close();
                });

            }


        });


    }
}
