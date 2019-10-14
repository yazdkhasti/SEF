package edu.rmit.sef.stocktradingclient.view;


import edu.rmit.sef.stocktradingclient.core.javafx.StyleHelper;
import edu.rmit.sef.user.command.LogoutCmd;
import edu.rmit.sef.user.model.SystemUser;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;


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
        StyleHelper.whiteText(welcomeLbl);
        leftPane.getChildren().add(welcomeLbl);

        Text lastSeenLbl = new Text("Last seen on");
        StyleHelper.bold(lastSeenLbl);
        leftPane.getChildren().add(lastSeenLbl);

        Text lastSeenOn = new Text();
        StyleHelper.addClass(lastSeenOn, "last-seen");
        StyleHelper.whiteText(lastSeenOn);
        leftPane.getChildren().add(lastSeenOn);

        Button logoutBtn = new Button("Sign out");
        logoutBtn.setPrefWidth(Double.MAX_VALUE);
        logoutBtn.setOnAction(e -> {
            getCommandService().execute(new LogoutCmd()).join();
            getViewManager().show(ViewNames.LOGIN);
        });

        leftPane.getChildren().add(logoutBtn);

        Separator line = new Separator();
        line.prefWidth(Double.MAX_VALUE);
        leftPane.getChildren().add(line);

        VBox actionGroupBox = new VBox(10);
        actionGroupBox.setAlignment(Pos.TOP_CENTER);
        leftPane.getChildren().add(actionGroupBox);

        Button stocksBtn = getNavActionButton("Stocks", "stocks.png");
        actionGroupBox.getChildren().add(stocksBtn);

        Button portfolioBtn = getNavActionButton("Portfolio", "portfolio.png");
        actionGroupBox.getChildren().add(portfolioBtn);

        stocksBtn.setOnAction(event -> {
            Parent node = getViewManager().loadSubScene(ViewNames.Stock.StockList,this.getPath());
            StyleHelper.addClass(node,"center-pane");
            root.setCenter(node);
        });

        portfolioBtn.setOnAction(event -> {
            Parent portfolioNode = getViewManager().loadSubScene(ViewNames.Portfolio.Portfolio,this.getPath());
            StyleHelper.addClass(portfolioNode,"center-pane");
            root.setCenter(portfolioNode);
        });

        Button ordersBtn = getNavActionButton("Orders", "orders.png");
        actionGroupBox.getChildren().add(ordersBtn);

        ordersBtn.setOnAction(event -> {
            Parent orderNode = getViewManager().loadSubScene(ViewNames.Order.OrderList, this.getPath());
            StyleHelper.addClass(orderNode, "center-pane");
            root.setCenter(orderNode);
        });

        SystemUser currentUser = getPermissionManager().getCurrentUser();
        welcomeLbl.setText(welcomeLbl.getText() + " " + currentUser.getFirstName() + " !");
        lastSeenOn.setText(currentUser.getPreviousLastSeenOn().toString());


        return leftPane;
    }

    private Button getNavActionButton(String text, String iconPath) {
        Button nabBtn = new Button();
        VBox btnBox = new VBox(10);

        Group btnGroup = new Group(btnBox);

        ImageView icon = StyleHelper.icon(iconPath, 40);
        StyleHelper.whiteText(icon);
        btnBox.getChildren().add(icon);

        Label btnLbl = new Label(text);
        StyleHelper.whiteText(btnLbl);
        StyleHelper.bold(btnLbl);
        btnBox.getChildren().add(btnLbl);

        nabBtn.setGraphic(btnGroup);
        nabBtn.setPrefWidth(Double.MAX_VALUE);
        StyleHelper.addClass(nabBtn, "link");

        return nabBtn;
    }
}
