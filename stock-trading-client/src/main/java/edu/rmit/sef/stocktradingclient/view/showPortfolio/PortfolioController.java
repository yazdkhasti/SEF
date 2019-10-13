package edu.rmit.sef.stocktradingclient.view.showPortfolio;

import edu.rmit.sef.portfolio.command.GetUserAllStockPortfolioResp;
import edu.rmit.sef.portfolio.command.GetUserStockPortfolioCmd;
import edu.rmit.sef.portfolio.command.UserAllStockPortfolio;
import edu.rmit.sef.stocktradingclient.core.javafx.StyleHelper;
import edu.rmit.sef.stocktradingclient.stock.StockEvents;
import edu.rmit.sef.stocktradingclient.view.JavaFXController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class PortfolioController extends JavaFXController{

    public GridPane root;
    private TableView portfolioTable;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        Text header = new Text("                      The list of shares you owned");
        StyleHelper.h2(header);
        root.getChildren().add(header);


        HBox toolbar = new HBox();
        root.getChildren().add(toolbar);

        Button refreshBth = new Button("Refresh");
        refreshBth.setOnAction(event -> {
            getData();
        });
        toolbar.getChildren().add(refreshBth);


//        portfolioTable = new TableView();
//
//        TableColumn<UserAllStockPortfolio, String> column1 = new TableColumn<>("Stock Name");
//        column1.setPrefWidth(200);
//        column1.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStockName()));
//
//
//        TableColumn<UserAllStockPortfolio, String> column2 = new TableColumn<>("Quantity");
//        column2.setPrefWidth(200);
//        column2.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getQuantity())));
//
//        portfolioTable.getColumns().add(column1);
//        portfolioTable.getColumns().add(column2);
//
//
//        root.getChildren().add(portfolioTable);

//        stockEvents.STOCK_ADDED.subscribe(entityChangedArg -> {
//            getData();
//        });
//
//
//
//    }
//
//
//
//    @Override
//    public void getData(){
//
//        GetUserAllStockPortfolioResp getUserAllStockPortfolioResp = new GetUserAllStockPortfolioResp();
//
//        getCommandService().execute(getUserAllStockPortfolioResp).thenAccept(getUserStockPortfolioResp -> {
//            Platform.runLater(() ->{
//                portfolioTable.getItems().clear();
//                portfolioTable.refresh();
//                portfolioTable.getItems().add(getUserStockPortfolioResp.getResult());
//            });
//        });
    }


}

