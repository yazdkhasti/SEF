package edu.rmit.sef.stocktradingserver.user.model;

import edu.rmit.sef.stocktradingserver.test.core.model.Entity;

public class SystemUser extends Entity {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String company;
    private String lastSeenOn;

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

}
