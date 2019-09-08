package edu.rmit.sef.stocktradingclient.view.stock;

import edu.rmit.sef.stocktradingclient.view.JavaFXController;
import edu.rmit.sef.user.command.GetCurrentUserCmd;
import edu.rmit.sef.user.model.SystemUser;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class StockList extends JavaFXController {


    public AnchorPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);


        Button testBtn = new Button("Test");
        testBtn.setOnAction((e) -> {
            getCommandService().execute(new GetCurrentUserCmd()).thenAccept(getCurrentUserResp -> {
                SystemUser currentUser = getCurrentUserResp.getUser();
                System.out.println(currentUser);
            });

        });
        root.getChildren().add(testBtn);
    }
}
