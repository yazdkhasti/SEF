package edu.rmit.command.core;

public class TestCmd extends Command<TestResp> {



    private Object target;

    public TestCmd(Object target) {
        if (target == null) {
            CommandUtil.assertNotNullArgument(target);
        }
        this.target = target;
    }

    public Object getTarget() {
        return target;
    }
}
