package edu.rmit.sef.user.model;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.sef.core.model.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SystemUser extends Entity {

    public final static String SYSTEM_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000000").toString();

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String company;
    private Date lastSeenOn;
    private Date previousLastSeenOn;
    private List<String> authorities;


    public SystemUser() {
        authorities = new ArrayList<String>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getLastSeenOn() {
        return lastSeenOn;
    }

    public void setLastSeenOn(Date lastSeenOn) {
        this.previousLastSeenOn = this.lastSeenOn;
        this.lastSeenOn = lastSeenOn;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public Date getPreviousLastSeenOn() {
        return previousLastSeenOn;
    }

    public void setPreviousLastSeenOn(Date secondLastSeenOn) {
        this.previousLastSeenOn = secondLastSeenOn;
    }

    public void validate() {
        CommandUtil.must(() -> username != null && username.length() >= 8,
                "Username cannot be less than 8 characters.");
        CommandUtil.assertNotNull(lastName,
                "lastName cannot be null.");
        CommandUtil.assertNotNull(firstName,
                "FirstName cannot be null.");

        CommandUtil.assertNotNull(password,
                "Password cannot be null.");

        CommandUtil.assertNotNull(company,
                "Company cannot be null.");

    }


}
