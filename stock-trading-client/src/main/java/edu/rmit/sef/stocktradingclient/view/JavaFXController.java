package edu.rmit.sef.stocktradingclient.view;

import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.ICommandServiceFactory;
import edu.rmit.sef.stocktradingclient.JavaFXApp;
import edu.rmit.sef.stocktradingclient.core.event.IEventBus;
import edu.rmit.sef.stocktradingclient.core.javafx.controls.StyleHelper;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class JavaFXController implements Initializable {


    @Autowired
    private ICommandServiceFactory commandServiceFactory;

    @Autowired
    private IEventBus eventBus;

    @Autowired
    private ViewManager viewManager;


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

    public ViewManager getViewManager() {
        return viewManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void initialize(Stage stage, Scene scene) {
        ConfigureRoot(scene);
        stage.show();
    }

    public void ConfigureRoot(Scene scene) {
        StyleHelper.ConfigureRoot(scene);
    }


}
