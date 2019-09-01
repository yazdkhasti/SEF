package edu.rmit.command.core;

public class NullCmd extends Command<NullResp> {



    private Object target;

    public NullCmd(Object target) {
        if (target == null) {
            CommandUtil.assertNotNullArgument(target);
        }
        this.target = target;
    }

    public Object getTarget() {
        return target;
    }
}
