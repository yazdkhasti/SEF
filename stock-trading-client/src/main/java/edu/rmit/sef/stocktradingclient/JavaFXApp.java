package edu.rmit.sef.stocktradingclient;


import edu.rmit.sef.stocktradingclient.view.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;



@SpringBootApplication
public class JavaFXApp extends Application {

    private ConfigurableApplicationContext context;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        SpringApplicationBuilder builder = new SpringApplicationBuilder(JavaFXApp.class);
        context = builder.web(WebApplicationType.NONE).run(getParameters().getRaw().toArray(new String[0]));
        ViewManager viewManager = context.getBean(ViewManager.class);
        viewManager.init(stage);
    }


}
