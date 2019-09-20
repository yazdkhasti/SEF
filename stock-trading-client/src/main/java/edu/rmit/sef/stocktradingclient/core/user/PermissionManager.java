package edu.rmit.sef.stocktradingclient.core.user;

import edu.rmit.sef.core.security.Authority;
import edu.rmit.sef.user.command.AuthenticateCmd;
import edu.rmit.sef.user.model.SystemUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class PermissionManager {

    private SystemUser currentUser;

    public SystemUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(SystemUser user) {
        currentUser = user;
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,
                null, AuthorityUtils.createAuthorityList(user.getAuthorities().toArray(new String[0])));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        boolean result = false;
        if (isAuthenticated()) {
            result = getCurrentUser().getAuthorities().contains(Authority.ADMIN);
        }
        return result;
    }


}
