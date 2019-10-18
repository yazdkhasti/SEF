package edu.rmit.sef.stocktradingclient.view.portfolio;

import edu.rmit.sef.stocktradingclient.view.JavaFXController;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class PortfolioListController extends JavaFXController {

    public GridPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);


        root.getChildren().add(new Text("Carol"));
    }
}
