package edu.rmit.sef.stocktradingclient.core.javafx;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.List;

public class ActionButtonCell<S> extends TableCell<S, HBox> {

    private HBox toolBar;

    private final List<TableAction<S>> actions;

    public ActionButtonCell(List<TableAction<S>> actions) {

        this.actions = actions;
        this.toolBar = new HBox();

    }




    public static <S> Callback<TableColumn<S, HBox>, TableCell<S, HBox>> forTableColumn(List<TableAction<S>> actions) {
        return param -> new ActionButtonCell<>(actions);
    }

    @Override
    public void updateItem(HBox item, boolean empty) {
        super.updateItem(item, empty);

        int totalCount = getTableView().getItems().size();
        int index = getIndex();

        if (totalCount > index && index >= 0) {

            S currentItem = getTableView().getItems().get(index);

            for (TableAction<S> action : actions) {
                Button actionBtn = action.getButton().call(currentItem);
                toolBar.getChildren().add(actionBtn);
                actionBtn.setOnAction(event -> {
                    action.getConsumer().accept(currentItem);
                });
            }
        }

        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(toolBar);
        }
    }
}
