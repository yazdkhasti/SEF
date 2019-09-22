package edu.rmit.command.core;

import edu.rmit.command.exception.AppExecutionException;
import edu.rmit.command.exception.CommandExecutionException;

public class CommandUtil {

    public static void assertNotNullArgument(Object arg) {
        if (arg == null) {
            throwCommandExecutionException("Argument cannot be null.");
        }
    }

    public static void assertNotNull(Object arg, String message) {
        must(() -> arg != null, message);
    }

    public static void throwCommandExecutionException(String msg) {
        throw new CommandExecutionException(msg);
    }

    public static void throwRecordNotFoundException() {
        throw new AppExecutionException("The requested record does not exist.");
    }

    public static void throwCommandExecutionException() {
        throwCommandExecutionException("An exception occurred.");
    }

    public static void throwCommandExecutionException(Exception ex) {
        throw new CommandExecutionException(ex);
    }

    public static void throwAppExecutionException(Exception ex) {
        throw new AppExecutionException(ex);
    }

    public static void throwAppExecutionException(String message) {
        throw new AppExecutionException(message);
    }

    public static void must(Validator rule, String message) {
        boolean isValid = false;
        isValid = rule.call();
        if (!isValid) {
            throwCommandExecutionException(message);
        }
    }

    public static void throwSecurityException() {
        throw new SecurityException("Unauthorized.");
    }
}
