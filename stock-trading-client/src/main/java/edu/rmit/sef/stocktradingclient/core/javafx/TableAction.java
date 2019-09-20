package edu.rmit.sef.stocktradingclient.core.javafx;

import javafx.scene.control.Button;
import javafx.util.Callback;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TableAction<S> {


    private Callback<S, Button> buttonFactory;
    private Consumer<S> consumer;

    public TableAction(Callback<S, Button> buttonFactory, Consumer<S> consumer) {
        this.buttonFactory = buttonFactory;
        this.consumer = consumer;
    }

    public Callback<S, Button> getButton() {
        return buttonFactory;
    }

    public void setButton(Callback<S, Button> buttonFactory) {
        this.buttonFactory = buttonFactory;
    }

    public Consumer<S> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<S> consumer) {
        this.consumer = consumer;
    }

}
