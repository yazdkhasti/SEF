package edu.rmit.sef.stocktradingserver.core.util;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.sef.user.model.SystemUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collections;

public class SecurityUtil {

    public static String getBearerToken(String header) {
        CommandUtil.assertNotNullArgument(header);
        return header.substring(7);
    }

    public static void setAuthentication(AbstractAuthenticationToken token) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(token);
        }
    }

    public static AbstractAuthenticationToken getToken(SystemUser user) {
        return new UsernamePasswordAuthenticationToken(
                user.toPrincipal(), null, Collections.singleton((GrantedAuthority) () -> "USER"));
    }
}
