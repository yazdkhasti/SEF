package edu.rmit.sef.stocktradingserver.core.security;

import edu.rmit.command.core.CommandFilter;
import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.IExecutionContext;
import edu.rmit.command.security.CommandAuthority;
import edu.rmit.sef.user.command.FindUserByIdCmd;
import edu.rmit.sef.user.command.FindUserByIdResp;
import edu.rmit.sef.user.model.SystemUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class CommandAuthorityFilter extends CommandFilter {

    private ConcurrentMap<String, List<String>> permissionCache = new ConcurrentHashMap<>();


    private List<String> getAuthorities(IExecutionContext context) {

        String userId = context.getUserId();

        if (userId == null) {
            CommandUtil.throwSecurityException();
        }

        List<String> ownedAuthorities = permissionCache.get(userId);

        if (ownedAuthorities == null) {
            FindUserByIdCmd findUserByIdCmd = new FindUserByIdCmd();
            findUserByIdCmd.setUserId(userId);
            FindUserByIdResp resp = context.getCommandService().execute(findUserByIdCmd).join();
            ownedAuthorities = resp.getUser().getAuthorities();
            permissionCache.putIfAbsent(userId, ownedAuthorities);
        }

        return ownedAuthorities;
    }

    @Override
    public void beforeExecution(IExecutionContext context) {

        Class<?> tClass = context.getCommand().getClass();

        if (tClass.isAnnotationPresent(CommandAuthority.class)) {

            CommandAuthority[] commandAuthorities = tClass.getAnnotationsByType(CommandAuthority.class);

            if (context.getUserId() == null) {
                CommandUtil.throwSecurityException();
            }

            if (SystemUser.SYSTEM_USER_ID.compareTo(context.getUserId()) == 0) {
                return;
            }

            List<String> ownedAuthorities = getAuthorities(context);

            for (CommandAuthority commandAuthority : commandAuthorities) {
                String authority = commandAuthority.value();

                if (!ownedAuthorities.contains(authority)) {
                    CommandUtil.throwSecurityException();
                }

            }

        }
    }
}
