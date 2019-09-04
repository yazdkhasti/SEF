package edu.rmit.sef.stocktradingclient;

import edu.rmit.sef.stocktradingclient.view.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StockTradingClientApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void start(Stage primaryStage) throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(StockTradingClientApplication.class);
        builder.web(WebApplicationType.NONE);
        context = builder.run(getParameters().getRaw().toArray(new String[0]));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();
        MainController mainController = loader.getController();
        mainController.init();
        primaryStage.setTitle("Internal Stock Trading");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
