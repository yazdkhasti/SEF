package edu.rmit.sef.user.model;

import java.security.Principal;

public interface SystemUserPrincipal extends Principal {
    String getId();
    String getUsername();

}
