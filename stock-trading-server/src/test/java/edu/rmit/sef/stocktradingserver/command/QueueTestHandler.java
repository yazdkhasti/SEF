package edu.rmit.sef.stocktradingserver.command;

import edu.rmit.command.core.ICommandHandler;
import edu.rmit.command.core.IQueueKeySelector;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class QueueTestHandler {


    private int value = 0;

    @Bean
    public IQueueKeySelector<QueuedTestCmd2> QueuedTestCmd2KeySelector() {

        return (command, tClass) -> {
            return "KeySelector";
        };

    }

    @Bean
    public IQueueKeySelector<QueuedTestCmd> QueuedTestCmdKeySelector() {

        return (command, tClass) -> {
            return "KeySelector";
        };

    }

    @Bean
    public ICommandHandler<QueuedTestCmd> queuedTestCmdICommandHandler() {

        return executionContext -> {
            QueuedTestCmd cmd = executionContext.getCommand();


            value += 1;

            cmd.setValue(value);

            QueueResp resp = new QueueResp();
            resp.setValue(value);

            cmd.setResponse(resp);

        };
    }

    @Bean
    public ICommandHandler<QueuedTestCmd2> queuedTestCmd2ICommandHandler() {

        return executionContext -> {
            QueuedTestCmd2 cmd = executionContext.getCommand();


            value += 1;

            cmd.setValue(value);

            QueueResp resp = new QueueResp();
            resp.setValue(value);

            cmd.setResponse(resp);

        };
    }


    @Bean
    public ICommandHandler<NotQueuedCmd> notQueuedCmdICommandHandler() {

        return executionContext -> {

            NotQueuedCmd cmd = executionContext.getCommand();

            value += 1;

            cmd.setValue(value);

            QueueResp resp = new QueueResp();
            resp.setValue(value);

            cmd.setResponse(resp);

        };
    }
}
