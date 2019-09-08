package edu.rmit.sef.stocktradingclient.view.user;

import ch.qos.logback.core.joran.action.ActionUtil;
import edu.rmit.sef.stocktradingclient.core.javafx.controls.StyleHelper;
import edu.rmit.sef.stocktradingclient.view.JavaFXController;
import edu.rmit.sef.stocktradingclient.view.ViewNames;
import edu.rmit.sef.user.command.AuthenticateCmd;
import edu.rmit.sef.user.model.SystemUser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
        root.setConstraints(title, 0, 0);


        Label usernameLbl = new Label();
        usernameLbl.setText("Username");
        root.setConstraints(usernameLbl, 0, 1);


        TextField usernameTxt = new TextField();
        usernameTxt.setPromptText("username");
        root.setConstraints(usernameTxt, 1, 1);

        Label passwordLbl = new Label();
        passwordLbl.setText("Password");
        root.setConstraints(passwordLbl, 0, 2);


        PasswordField passwordTxt = new PasswordField();
        passwordTxt.setPromptText("password");
        root.setConstraints(passwordTxt, 1, 2);


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
        root.setConstraints(buttonBox, 1, 3);

        Text errorLabel = new Text();
        errorLabel.setText("Invalid credentials");
        errorLabel.setVisible(false);
        StyleHelper.error(errorLabel);
        root.setConstraints(errorLabel, 1, 4);


        root.getChildren().addAll(title, usernameLbl, usernameTxt, passwordLbl, passwordTxt, buttonBox, errorLabel);

        root.setAlignment(Pos.CENTER);



        loginBtn.setOnAction(event -> {
            loginBtn.setDisable(true);
            AuthenticateCmd authenticateCmd = new AuthenticateCmd();
            authenticateCmd.setUsername(usernameTxt.getText());
            authenticateCmd.setPassword(passwordTxt.getText());

            getCommandService().execute(authenticateCmd).whenComplete((authenticateResp, ex) -> {
                if (ex != null) {
                    errorLabel.setVisible(true);
                } else {
                    getViewManager().show(ViewNames.MAIN);
                }
                loginBtn.setDisable(false);
            });

        });
    }


}
