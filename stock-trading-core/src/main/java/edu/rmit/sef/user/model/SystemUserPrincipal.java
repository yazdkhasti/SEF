package edu.rmit.sef.user.model;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

public interface SystemUserPrincipal extends Principal {
    String getId();
    String getUsername();
    List<String> getAuthorities();
}
