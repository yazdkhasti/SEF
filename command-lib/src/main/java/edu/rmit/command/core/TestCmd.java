package edu.rmit.command.core;

public class TestCmd extends Command<TestResp> {



    private Object target;

    public TestCmd() { }

    public TestCmd(Object target) {
        setTarget(target);
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        if (target == null) {
            CommandUtil.assertNotNullArgument(target);
        }
        this.target = target;
    }

}
