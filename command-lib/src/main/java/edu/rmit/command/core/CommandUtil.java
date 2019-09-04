package edu.rmit.command.core;

import edu.rmit.command.exception.AppExecutionException;
import edu.rmit.command.exception.CommandExecutionException;

public class CommandUtil {

    public static void assertNotNullArgument(Object arg) {
        if (arg == null) {
            throwCommandExecutionException("Argument cannot be null.");
        }
    }

    public static void throwCommandExecutionException(String msg) {
        throw new CommandExecutionException(msg);
    }

    public static void throwCommandExecutionException() {
        throwCommandExecutionException("An exception occured.");
    }

    public static void throwCommandExecutionException(Exception ex) {
        throw new CommandExecutionException(ex);
    }

    public static void throwAppExecutionException(Exception ex) {
        throw new AppExecutionException(ex);
    }
}
