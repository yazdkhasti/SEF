package edu.rmit.command.core;

public abstract class CommandFilter {
    void beforeExecution(IExecutionContext context) { }

    void afterExecution(IExecutionContext context) { }

}
