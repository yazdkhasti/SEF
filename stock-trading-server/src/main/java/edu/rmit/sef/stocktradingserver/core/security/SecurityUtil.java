package edu.rmit.sef.stocktradingserver.core.security;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.sef.user.model.SystemUserPrincipal;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

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

    public static AbstractAuthenticationToken getToken(SystemUserPrincipal principal) {
        return new UsernamePasswordAuthenticationToken(
                principal, null, AuthorityUtils.createAuthorityList(principal.getAuthorities().toArray(new String[0])));
    }
}
