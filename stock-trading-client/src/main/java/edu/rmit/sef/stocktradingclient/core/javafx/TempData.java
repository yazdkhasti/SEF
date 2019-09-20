package edu.rmit.sef.stocktradingclient.core.javafx;

import java.util.HashMap;

public class TempData {
    private HashMap<String, Object> data;
    private final static String PRIMARY_DATA_KEY = "PRIMARY";

    public TempData() {
        data = new HashMap<>();
    }

    public TempData(Object primaryData) {
        this();
        AddPrimaryData(primaryData);
    }


    public void AddData(String key, Object value) {
        data.putIfAbsent(key, value);
    }

    public void AddPrimaryData(Object value) {
        AddData(PRIMARY_DATA_KEY, value);
    }

    public <T> T getPrimaryData() {
        return getData(PRIMARY_DATA_KEY);
    }

    public <T> T getData(String key) {
        return (T) data.get(key);
    }

}
