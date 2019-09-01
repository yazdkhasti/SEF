package edu.rmit.command.core;

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
}
