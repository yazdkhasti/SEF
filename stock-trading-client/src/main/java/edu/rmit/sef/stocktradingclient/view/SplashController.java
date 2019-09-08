package edu.rmit.sef.stocktradingclient.view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;


@Component
public class SplashController extends JavaFXController {

    @Value("${edu.rmit.sef.stocktrading.client.splashTimeout}")
    private int splashTimeout;

    @Value("${edu.rmit.sef.stocktrading.client.title}")
    private String appTitle;

    public VBox root;

    public Label appTitleLbl;


    @Override
    public void initialize(Stage stage, Scene scene) {

        root.setSpacing(10);


        appTitleLbl.setText(appTitle);
        appTitleLbl.setMaxWidth(Double.MAX_VALUE);
        appTitleLbl.setAlignment(Pos.CENTER);

        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    getViewManager().showAppScreen(ViewNames.LOGIN);
                });
            }

        };

        timer.schedule(task, splashTimeout);


        stage.show();
    }


}
