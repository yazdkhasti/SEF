package edu.rmit.sef.stocktradingclient.core.javafx.controls;


import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import jfxtras.styles.jmetro8.JMetro;


public class StyleHelper {
    public static void ConfigureRoot(Scene scene) {

        Parent root = scene.getRoot();

        new JMetro(JMetro.Style.LIGHT).applyTheme(scene);

        root.getStylesheets().add(StyleHelper.class.getResource("/views/general/app.css").toExternalForm());
        root.getStyleClass().add("root");

        if (root instanceof GridPane) {
            root.getStyleClass().addAll("grid-pane");
        }
    }

    public static void h1(Node node) {
        addClass(node, "h1");
    }

    public static void h2(Node node) {
        addClass(node, "h2");
    }

    public static void h3(Node node) {
        addClass(node, "h3");
    }

    public static void bold(Node node) {
        addClass(node, "bold");
    }

    public static void error(Node node) {
        addClass(node, "error");
    }

    public static ImageView icon(String imagePath, double fitWidth) {
        Image image = new Image(StyleHelper.class.getResourceAsStream("/images/" + imagePath));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(fitWidth);
        return imageView;
    }

    public static void addClass(Node node, String... cssClass) {
        node.getStyleClass().addAll(cssClass);
    }
}
