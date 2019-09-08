package edu.rmit.sef.stocktradingserver.core.security;

import edu.rmit.command.core.CommandFilter;
import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.IExecutionContext;
import edu.rmit.command.security.CommandAuthority;
import edu.rmit.sef.user.model.SystemUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommandAuthorityFilter extends CommandFilter {

    @Override
    public void beforeExecution(IExecutionContext context) {

        Class<?> tClass = context.getCommand().getClass();

        if (tClass.isAnnotationPresent(CommandAuthority.class)) {

            CommandAuthority commandAuthority = tClass.getAnnotation(CommandAuthority.class);

            String[] authorities = commandAuthority.value();

            if (authorities.length > 0) {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (authentication == null || !authentication.isAuthenticated()) {
                    CommandUtil.throwSecurityException();
                }

                SystemUserPrincipal principal = (SystemUserPrincipal) authentication.getPrincipal();
                List<String> ownedAuthorities = principal.getAuthorities();

                for (String authority : authorities) {
                    if (!ownedAuthorities.contains(authority)) {
                        CommandUtil.throwSecurityException();
                    }
                }

            }
        }
    }
}
