package edu.rmit.sef.stocktradingclient.core.command;

import edu.rmit.command.core.CommandFilter;
import edu.rmit.command.core.IExecutionContext;
import edu.rmit.sef.stocktradingclient.core.socket.SocketConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandProxyFilter extends CommandFilter {


    @Autowired
    private SocketConnection socketConnection;

    @Override
    public void afterExecution(IExecutionContext context) {

        super.afterExecution(context);


        if (context.getCommand().getResponse() == null) {
            socketConnection.executeCommand(context.getCommand());
        }
    }
}
