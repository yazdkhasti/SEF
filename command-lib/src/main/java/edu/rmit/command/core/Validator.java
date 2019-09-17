package edu.rmit.command.core;

import java.util.concurrent.Callable;

public interface Validator extends Callable<Boolean> {
    @Override
    Boolean call();
}
