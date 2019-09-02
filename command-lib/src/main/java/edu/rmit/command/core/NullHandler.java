package edu.rmit.command.core;

import org.springframework.stereotype.Service;

@Service
public class NullHandler implements ICommandHandler<TestCmd> {

    @Override
    public void handle(ICommandExecutionContext<TestCmd> executionContext) {
        TestCmd cmd = executionContext.getCommand();
        TestResp resp = new TestResp(cmd.getTarget());
        cmd.setResponse(resp);
    }
}
