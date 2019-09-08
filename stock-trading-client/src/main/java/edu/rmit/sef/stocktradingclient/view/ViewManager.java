package edu.rmit.sef.stocktradingclient.view;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.IServiceResolver;
import edu.rmit.sef.stocktradingclient.core.event.EventBus;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ViewManager {

    @Autowired
    private EventBus eventBus;

    @Autowired
    private IServiceResolver serviceResolver;

    @Value("${edu.rmit.sef.stocktrading.client.title}")
    private String appTitle;

    private Map<String, Stage> stageMap;

    private String appStagePath;
    private String mainStagePath;

    public ViewManager() {
        stageMap = new HashMap<>();
        appStagePath = "/";
        mainStagePath = ViewNames.MAIN;
    }

    public void registerAppStage(Stage stage) {
        registerStage(appStagePath, stage);
    }

    public void registerMainStage(Stage stage) {
        registerStage(mainStagePath, stage);
    }

    public Stage getAppStage() {
        return getStage(appStagePath);
    }

    public Stage getMainStage() {
        return getStage(mainStagePath);
    }

    public void registerStage(String path, Stage stage) {
        stageMap.put(path, stage);
        configureStage(stage);
    }

    public void init(Stage appStage) {
        registerAppStage(appStage);
        showSplashScreen("/splash/splash");
    }

    private void configureStage(Stage stage) {
        //<Icons made by https://www.flaticon.com/authors/pause08" title="Pause08"
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/app-icon.png")));
    }

    public JavaFXController showSplashScreen(String path) {
        Stage appStage = getAppStage();
        appStage.centerOnScreen();
        appStage.initStyle(StageStyle.UNDECORATED);
        return show(path, appStage);
    }

    public JavaFXController showAppScreen(String path) {
        getAppStage().hide();
        JavaFXController controller = openView(path);
        Stage mainStage = getStage(path);
        mainStage.setOnCloseRequest((e) -> {
            close();
        });
        registerMainStage(mainStage);
        return controller;
    }


    public void close() {
        getAppStage().close();
        Platform.exit();
        System.exit(0);
    }

    private Stage getStage(String path) {
        Stage stage = stageMap.get(path);
        return stage;
    }


    public JavaFXController openView(String path, StageStyle stageStyle, Modality modality, Stage parentStage) {
        Stage stage = new Stage();
        stage.initOwner(parentStage);
        stage.initStyle(stageStyle);
        stage.initModality(modality);
        registerStage(path, stage);
        return show(path, stage);
    }

    public JavaFXController openView(String path, StageStyle stageStyle, Modality modality) {
        Stage appStage = getMainStage();
        return openView(path, stageStyle, modality, appStage);
    }

    public JavaFXController openModal(String path) {
        return openView(path, StageStyle.UTILITY, Modality.APPLICATION_MODAL);
    }

    public JavaFXController openView(String path) {
        return openView(path, StageStyle.DECORATED, Modality.NONE);
    }

    public JavaFXController show(String path) {
        Stage appStage = getMainStage();
        return show(path, appStage);
    }

    private JavaFXController show(String path, Stage stage) {

        FXMLLoader loader = getLoader(path);
        Parent root = loadFXML(loader);

        stage.setTitle(appTitle);
        Scene scene = new Scene(root);

        JavaFXController javaFXController = loader.getController();
        javaFXController.setPath(path);


        Platform.runLater(() -> {
            javaFXController.initialize(stage, scene);
            stage.setScene(scene);
        });


        return javaFXController;
    }

    public Parent loadSubScene(String path, String parentPath) {

        FXMLLoader loader = getLoader(path);
        Parent root = loadFXML(loader);

        Stage stage = getStage(parentPath);

        JavaFXController javaFXController = loader.getController();
        javaFXController.setPath(path);

        Platform.runLater(() -> {
            javaFXController.initialize(stage, stage.getScene());
        });


        return root;
    }

    private Parent loadFXML(FXMLLoader loader) {
        Parent root = null;
        try {
            root = loader.load();
        } catch (Exception ex) {
            CommandUtil.throwAppExecutionException(ex);
        }
        return root;
    }

    public FXMLLoader getLoader(String path) {
        String fxmlPath = getFxmlResourcePath(path);
        FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
        loader.setControllerFactory(serviceResolver::getService);
        return loader;
    }

    private String getFxmlResourcePath(String path) {
        return "/views" + path + ".fxml";
    }

}
