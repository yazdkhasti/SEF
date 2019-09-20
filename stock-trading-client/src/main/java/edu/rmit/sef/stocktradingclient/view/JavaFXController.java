package edu.rmit.sef.stocktradingclient.view;

import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.ICommandServiceFactory;
import edu.rmit.sef.stocktradingclient.core.event.IEventBus;
import edu.rmit.sef.stocktradingclient.core.javafx.StyleHelper;
import edu.rmit.sef.stocktradingclient.core.javafx.TempData;
import edu.rmit.sef.stocktradingclient.core.user.PermissionManager;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public abstract class JavaFXController implements Initializable {


    @Autowired
    private ICommandServiceFactory commandServiceFactory;

    @Autowired
    private IEventBus eventBus;

    @Autowired
    private ViewManager viewManager;

    @Autowired
    private PermissionManager permissionManager;

    private TempData tempData;

    private Stage stage;

    private Scene scene;

    private String path;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public IEventBus getEventBus() {
        return eventBus;
    }

    public ICommandService getCommandService() {
        return commandServiceFactory.createService();
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public ViewManager getViewManager() {
        return viewManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void initialize(Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;
        ConfigureRoot(scene);
        stage.show();
        getData();
    }

    public void getData() {
    }

    public void close() {
        Platform.runLater(() -> stage.close());
    }

    public void ConfigureRoot(Scene scene) {
        StyleHelper.ConfigureRoot(scene);
    }

    public TempData getTempData() {
        return tempData;
    }

    public void setTempData(TempData tempData) {
        this.tempData = tempData;
    }


}
