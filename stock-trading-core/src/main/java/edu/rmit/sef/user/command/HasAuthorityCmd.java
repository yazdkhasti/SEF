package edu.rmit.sef.user.command;

import edu.rmit.command.core.Command;

public class HasAuthorityCmd extends Command<HasAuthorityResp> {

    private String userId;
    private String[] authorities;


    public HasAuthorityCmd() {
    }

    public HasAuthorityCmd(String userId, String authority) {
        this(userId, new String[]{authority});
    }

    public HasAuthorityCmd(String userId, String[] authorities) {
        this.userId = userId;
        this.authorities = authorities;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }

}
