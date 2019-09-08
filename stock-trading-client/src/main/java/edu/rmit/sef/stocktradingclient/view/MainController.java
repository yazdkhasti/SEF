package edu.rmit.sef.stocktradingclient.view;


import edu.rmit.sef.stocktradingclient.core.javafx.controls.StyleHelper;
import edu.rmit.sef.user.command.GetCurrentUserCmd;
import edu.rmit.sef.user.model.SystemUser;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;


import java.net.URL;
import java.util.ResourceBundle;

@Component
public class MainController extends JavaFXController {


    public BorderPane root;

    @Override
    public void initialize(Stage stage, Scene scene) {
        super.initialize(stage, scene);

        StyleHelper.addClass(root, "main-pane");


        root.setLeft(getLeftNode());

        Parent centerNode = getViewManager().loadSubScene(ViewNames.Stock.StockList, this.getPath());
        StyleHelper.addClass(centerNode, "center-pane");
        root.setCenter(centerNode);
    }

    private Parent getLeftNode() {

        VBox leftPane = new VBox(10);
        StyleHelper.addClass(leftPane, "left-pane");
        leftPane.setPrefWidth(200);

        Text welcomeLbl = new Text("Welcome");
        StyleHelper.h3(welcomeLbl);
        StyleHelper.addClass(welcomeLbl, "client-name");
        leftPane.getChildren().add(welcomeLbl);

        Text lastSeenLbl = new Text("Last seen on");
        StyleHelper.bold(lastSeenLbl);
        leftPane.getChildren().add(lastSeenLbl);

        Text lastSeenOn = new Text();
        StyleHelper.addClass(lastSeenOn, "last-seen");
        leftPane.getChildren().add(lastSeenOn);

        Separator line = new Separator();
        line.prefWidth(Double.MAX_VALUE);
        leftPane.getChildren().add(line);

        Button stocks = new Button();
        VBox btnBox = new VBox(10);
        Group btnGroup = new Group(btnBox);
        btnBox.getChildren().add(StyleHelper.icon("bar-chart.png", 30));
        btnBox.getChildren().add(new Label("Test"));
        stocks.setGraphic(btnGroup);
        StyleHelper.addClass(stocks, "link");
        leftPane.getChildren().add(stocks);

        getCommandService().execute(new GetCurrentUserCmd()).thenAccept(getCurrentUserResp -> {
            SystemUser currentUser = getCurrentUserResp.getUser();
            welcomeLbl.setText(welcomeLbl.getText() + " " + currentUser.getFirstName() + " !");
            lastSeenOn.setText(currentUser.getPreviousLastSeenOn().toString());
        });


        return leftPane;
    }
}
