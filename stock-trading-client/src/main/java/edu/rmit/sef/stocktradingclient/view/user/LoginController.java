package edu.rmit.sef.stocktradingclient.view.user;

import edu.rmit.command.core.ICommandService;
import edu.rmit.sef.stocktradingclient.core.javafx.StyleHelper;
import edu.rmit.sef.stocktradingclient.view.JavaFXController;
import edu.rmit.sef.stocktradingclient.view.ViewNames;
import edu.rmit.sef.user.command.AuthenticateCmd;
import edu.rmit.sef.user.command.GetCurrentUserCmd;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class LoginController extends JavaFXController {


    public GridPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);


        Text title = new Text("Welcome");
        StyleHelper.h2(title);
        GridPane.setConstraints(title, 0, 0);


        Label usernameLbl = new Label();
        usernameLbl.setText("Username");
        GridPane.setConstraints(usernameLbl, 0, 1);


        TextField usernameTxt = new TextField();
        usernameTxt.setPromptText("username");
        GridPane.setConstraints(usernameTxt, 1, 1);

        Label passwordLbl = new Label();
        passwordLbl.setText("Password");
        GridPane.setConstraints(passwordLbl, 0, 2);


        PasswordField passwordTxt = new PasswordField();
        passwordTxt.setPromptText("password");
        GridPane.setConstraints(passwordTxt, 1, 2);


        Button loginBtn = new Button();
        loginBtn.setText("Login");


        Button registerBtn = new Button();
        registerBtn.setText("Register");
        registerBtn.setOnAction(event -> {
            getViewManager().show(ViewNames.REGISTER);
        });

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.getChildren().addAll(registerBtn, loginBtn);
        GridPane.setConstraints(buttonBox, 1, 3);

        Text errorLabel = new Text();
        errorLabel.setText("Invalid credentials");
        errorLabel.setVisible(false);
        StyleHelper.error(errorLabel);
        GridPane.setConstraints(errorLabel, 1, 4);


        root.getChildren().addAll(title, usernameLbl, usernameTxt, passwordLbl, passwordTxt, buttonBox, errorLabel);

        root.setAlignment(Pos.CENTER);


        loginBtn.setOnAction(event -> {
            loginBtn.setDisable(true);
            AuthenticateCmd authenticateCmd = new AuthenticateCmd();
            authenticateCmd.setUsername(usernameTxt.getText());
            authenticateCmd.setPassword(passwordTxt.getText());

            ICommandService commandService = getCommandService();

            commandService.execute(authenticateCmd).whenComplete((authenticateResp, ex) -> {
                if (ex != null) {
                    errorLabel.setVisible(true);
                } else {
                    commandService.execute(new GetCurrentUserCmd()).thenAccept(getCurrentUserResp -> {
                        getViewManager().show(ViewNames.MAIN);
                    });
                }
                loginBtn.setDisable(false);
            });

        });
    }


}
