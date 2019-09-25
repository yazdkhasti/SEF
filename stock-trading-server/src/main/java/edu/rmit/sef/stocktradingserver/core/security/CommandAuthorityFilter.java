package edu.rmit.sef.stocktradingserver.core.security;

import edu.rmit.command.core.CommandFilter;
import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.IExecutionContext;
import edu.rmit.command.security.CommandAuthority;
import edu.rmit.sef.user.model.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandAuthorityFilter extends CommandFilter {


    @Autowired
    private SecurityUtil securityUtil;


    @Override
    public void beforeExecution(IExecutionContext context) {

        Class<?> tClass = context.getCommand().getClass();

        if (tClass.isAnnotationPresent(CommandAuthority.class)) {

            CommandAuthority[] commandAuthorities = tClass.getAnnotationsByType(CommandAuthority.class);

            if (context.getUserId() == null) {
                CommandUtil.throwSecurityException();
            }

            String userId = context.getUserId();

            if (SystemUser.SYSTEM_USER_ID.compareTo(userId) == 0) {
                return;
            }


            for (CommandAuthority commandAuthority : commandAuthorities) {
                String authority = commandAuthority.value();

                if (!securityUtil.hasAuthority(userId, authority)) {
                    CommandUtil.throwSecurityException();
                }

            }

        }
    }
}
