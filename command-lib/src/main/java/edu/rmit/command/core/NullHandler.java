package edu.rmit.command.core;

import org.springframework.stereotype.Service;

@Service
public class NullHandler implements ICommandHandler<NullCmd> {

    @Override
    public void handle(ICommandExecutionContext<NullCmd> executionContext) {
        NullCmd cmd = executionContext.getCommand();
        NullResp resp = new NullResp(cmd.getTarget());
        cmd.setResponse(resp);
    }
}
