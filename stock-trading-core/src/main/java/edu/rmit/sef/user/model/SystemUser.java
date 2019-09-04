package edu.rmit.sef.user.model;

import edu.rmit.sef.core.model.Entity;

import java.security.Principal;
import java.util.Date;

public class SystemUser extends Entity {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String company;
    private Date lastSeenOn;

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
        this.lastSeenOn = lastSeenOn;
    }


    public SystemUserPrincipal toPrincipal() {
        return new SystemUserPrincipal() {
            public String getId() {
                return SystemUser.this.getId();
            }
            public String getUsername() {
                return SystemUser.this.getUsername();
            }
            public String getName() {
                return SystemUser.this.getId();
            }
        };
    }


}
