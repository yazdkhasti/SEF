package edu.rmit.sef.stocktradingserver.core.security;

import edu.rmit.command.core.CommandServiceFactory;
import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.ICommandService;
import edu.rmit.sef.user.command.HasAuthorityCmd;
import edu.rmit.sef.user.command.HasAuthorityResp;
import edu.rmit.sef.user.model.SystemUser;
import edu.rmit.sef.user.model.SystemUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    @Autowired
    CommandServiceFactory commandServiceFactory;

    private ICommandService getCommandService() {
        return commandServiceFactory.createService(SystemUser.SYSTEM_USER_ID);
    }

    public String getBearerToken(String header) {
        CommandUtil.assertNotNullArgument(header);
        return header.substring(7);
    }

    public void setAuthentication(AbstractAuthenticationToken token) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(token);
        }
    }

    public AbstractAuthenticationToken getToken(SystemUserPrincipal principal) {
        return new UsernamePasswordAuthenticationToken(
                principal, null, AuthorityUtils.createAuthorityList(principal.getAuthorities().toArray(new String[0])));
    }

    public boolean hasAuthority(String userId, String authority) {
        return hasAuthority(userId, new String[]{authority});
    }

    public boolean hasAuthority(String userId, String[] authority) {
        HasAuthorityCmd hasAuthorityCmd = new HasAuthorityCmd(userId, authority);
        HasAuthorityResp hasAuthorityResp = getCommandService().execute(hasAuthorityCmd).join();
        return hasAuthorityResp.getResult();
    }


}
