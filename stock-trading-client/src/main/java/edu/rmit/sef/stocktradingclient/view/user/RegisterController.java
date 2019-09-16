package edu.rmit.sef.stocktradingclient.view.user;

import edu.rmit.sef.stocktradingclient.core.javafx.controls.StyleHelper;
import edu.rmit.sef.stocktradingclient.view.JavaFXController;
import edu.rmit.sef.stocktradingclient.view.ViewNames;
import edu.rmit.sef.user.command.AuthenticateCmd;
import edu.rmit.sef.user.command.RegisterUserCmd;
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
public class RegisterController extends JavaFXController {

    public GridPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        Text title = new Text("Registration");
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


        Label firstNameLbl = new Label();
        firstNameLbl.setText("First name");
        GridPane.setConstraints(firstNameLbl, 0, 3);


        TextField firstNameTxt = new TextField();
        firstNameTxt.setPromptText("First name");
        GridPane.setConstraints(firstNameTxt, 1, 3);

        Label lastNameLbl = new Label();
        lastNameLbl.setText("Last name");
        GridPane.setConstraints(lastNameLbl, 0, 4);


        TextField lastNameTxt = new TextField();
        lastNameTxt.setPromptText("Last name");
        GridPane.setConstraints(lastNameTxt, 1, 4);


        Label companyNameLbl = new Label();
        companyNameLbl.setText("Company");
        GridPane.setConstraints(companyNameLbl, 0, 5);


        TextField companyNameTxt = new TextField();
        companyNameTxt.setPromptText("Company");
        GridPane.setConstraints(companyNameTxt, 1, 5);


        Button loginBtn = new Button();
        loginBtn.setText("Login");
        loginBtn.setOnAction(event -> {
            getViewManager().show(ViewNames.LOGIN);
        });

        Button registerBtn = new Button();
        registerBtn.setText("Register");


        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.getChildren().addAll(registerBtn, loginBtn);
        GridPane.setConstraints(buttonBox, 1, 6);

        Text errorLabel = new Text();
        errorLabel.setText("User Already exists");
        errorLabel.setVisible(false);
        StyleHelper.error(errorLabel);
        GridPane.setConstraints(errorLabel, 1, 7);


        root.getChildren().addAll(title, usernameLbl, usernameTxt, passwordLbl,
                passwordTxt, firstNameLbl, firstNameTxt, lastNameLbl,
                lastNameTxt, companyNameLbl, companyNameTxt, buttonBox, errorLabel);

        root.setAlignment(Pos.TOP_CENTER);




        registerBtn.setOnAction(event -> {

            registerBtn.setDisable(true);

            RegisterUserCmd registerUserCmd = new RegisterUserCmd();
            registerUserCmd.setUsername(usernameTxt.getText());
            registerUserCmd.setPassword(passwordTxt.getText());
            registerUserCmd.setFirstName(firstNameTxt.getText());
            registerUserCmd.setLastName(lastNameTxt.getText());
            registerUserCmd.setCompany(companyNameTxt.getText());

            getCommandService().execute(registerUserCmd).whenComplete((registerUserResp, ex) -> {

                if (ex != null) {

                    errorLabel.setVisible(true);

                } else {

                    AuthenticateCmd authenticateCmd = new AuthenticateCmd();
                    authenticateCmd.setUsername(usernameTxt.getText());
                    authenticateCmd.setPassword(passwordTxt.getText());

                    getCommandService().execute(authenticateCmd).thenAccept((authenticateResp) -> {
                        getViewManager().show(ViewNames.MAIN);
                    });

                }

                registerBtn.setDisable(false);

            });


        });
    }

    @Override
    public void initialize(Stage stage, Scene scene) {
        super.initialize(stage, scene);
    }
}
