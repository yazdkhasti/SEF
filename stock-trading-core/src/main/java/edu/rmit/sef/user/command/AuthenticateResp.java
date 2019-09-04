package edu.rmit.sef.user.command;

import edu.rmit.sef.core.command.CommandResp;

import java.util.Date;

public class AuthenticateResp extends CommandResp {



    private String firstName;
    private String lastName;
    private String token;
    private Date lastSeenOn;


    public AuthenticateResp() {
    }

    public AuthenticateResp(String firstName, String lastName, String token, Date lastSeenOn) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.token = token;
        this.lastSeenOn = lastSeenOn;
    }

    public String getToken() {
        return token;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getLastSeenOn() {
        return lastSeenOn;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setLastSeenOn(Date lastSeenOn) {
        this.lastSeenOn = lastSeenOn;
    }



}
