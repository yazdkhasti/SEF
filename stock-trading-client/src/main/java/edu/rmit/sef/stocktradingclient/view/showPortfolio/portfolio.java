package edu.rmit.sef.stocktradingclient.view.showPortfolio;
import edu.rmit.sef.portfolio.command.GetUserStockPortfolioCmd;
import edu.rmit.sef.portfolio.command.GetUserStockPortfolioResp;
import edu.rmit.sef.order.command.FindOrderByIdCmd;
import edu.rmit.sef.order.model.Order;
import edu.rmit.sef.order.command.GetAllOrderResp;
import edu.rmit.sef.stock.command.AddStockCmd;
import edu.rmit.sef.stock.command.UpdateStockCmd;
import edu.rmit.sef.stock.model.Stock;
import edu.rmit.sef.stocktradingclient.core.event.EntityChangedArg;
import edu.rmit.sef.stocktradingclient.core.javafx.StyleHelper;
import edu.rmit.sef.stocktradingclient.core.javafx.TempData;
import edu.rmit.sef.stocktradingclient.stock.StockEvents;
import edu.rmit.sef.stocktradingclient.view.JavaFXController;
import edu.rmit.sef.stocktradingclient.view.order.OrderList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import edu.rmit.sef.stocktradingclient.core.javafx.TableAction;
import edu.rmit.sef.stock.command.GetAllStocksCmd;
import javafx.application.Platform;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class portfolio extends JavaFXController{

    public GridPane root;
    private TableView portfolioTable;

    @Autowired
    StockEvents stockEvents;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        super.initialize(location, resources);

        Text header = new Text("The list of shares you owned");
        StyleHelper.h1(header);
        root.getChildren().add(header);

        HBox toolbar = new HBox();
        root.getChildren().add(toolbar);

        Button refreshBth = new Button("Refresh");
        refreshBth.setOnAction(event -> {
            getData();
        });
        toolbar.getChildren().add(refreshBth);


        portfolioTable = new TableView();

        TableColumn<portfolio, String> column1 = new TableColumn<>("Stock Name");
        column1.setPrefWidth(200);
        column1.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getClass().getName()));



        TableColumn<portfolio, String> column2 = new TableColumn<>("Quantity");
        column2.setPrefWidth(200);
        column2.setCellValueFactory(param -> new SimpleStringProperty());

        portfolioTable.getColumns().add(column1);
        portfolioTable.getColumns().add(column2);



        root.getChildren().add(portfolioTable);


    }



    @Override
    public void getData(){

        GetUserStockPortfolioCmd getUserStockPortfolioCmd = new GetUserStockPortfolioCmd();

//        getCommandService().execute(getUserStockPortfolioCmd).thenAccept(())
    }


}

