package edu.rmit.command.core;

import java.util.HashMap;
import java.util.Map;

public class ExecutionOptions {

    private boolean ignoreAsyncHandlers;
    private Map<String, Object> executionParameters = new HashMap<>();


    public boolean getIgnoreAsyncHandlers() {
        return ignoreAsyncHandlers;
    }

    public void setIgnoreAsyncHandlers(boolean ignoreAsyncHandlers) {
        this.ignoreAsyncHandlers = ignoreAsyncHandlers;
    }

    public <T> T getExecutionParameter(String key, T defaultValue) {
        T value = (T) executionParameters.get(key);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public void addExecutionParameter(String key, Object value) {
        executionParameters.put(key, value);
    }

}
