package edu.rmit.sef.stocktradingserver.user.service;

import edu.rmit.command.core.ICommandExecutionContext;
import edu.rmit.command.core.ICommandHandler;
import edu.rmit.sef.stocktradingserver.user.command.AuthenticateCmd;
import edu.rmit.sef.stocktradingserver.user.command.AuthenticateResp;
import edu.rmit.sef.stocktradingserver.user.exception.DisabledUserException;
import edu.rmit.sef.stocktradingserver.user.exception.InvalidUserCredentialsException;
import edu.rmit.sef.stocktradingserver.user.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserAuthService implements UserDetailsService, ICommandHandler<AuthenticateCmd> {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return User.builder()
                .username("test")
                .password("$2a$10$6JGExpZSVNuvN.Rfsf2haOpQZnXZUYxXS/14TsSC0ivHSo0xZmLj2")
                .authorities("default")
                .build();
    }


    @Override
    public void handle(ICommandExecutionContext<AuthenticateCmd> executionContext) {

        AuthenticateCmd cmd = executionContext.getCommand();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(cmd.getUsername(), cmd.getPassword()));
        } catch (DisabledException e) {
            throw new DisabledUserException("User Inactive");
        } catch (BadCredentialsException e) {
            throw new InvalidUserCredentialsException("Invalid Credentials");
        }

        UserDetails userDetails = this.loadUserByUsername(cmd.getUsername());

        String token = jwtUtil.generateToken(userDetails);

        AuthenticateResp resp = new AuthenticateResp(userDetails.getUsername(), token);

        cmd.setResponse(resp);
    }
}
