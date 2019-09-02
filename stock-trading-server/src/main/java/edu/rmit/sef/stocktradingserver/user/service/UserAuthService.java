package edu.rmit.sef.stocktradingserver.user.service;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.ICommandHandler;
import edu.rmit.command.core.InitCmd;
import edu.rmit.sef.stocktradingserver.core.model.Entity;
import edu.rmit.sef.stocktradingserver.user.command.AuthenticateCmd;
import edu.rmit.sef.stocktradingserver.user.command.AuthenticateResp;
import edu.rmit.sef.stocktradingserver.user.command.RegisterUserCmd;
import edu.rmit.sef.stocktradingserver.user.command.RegisterUserResp;
import edu.rmit.sef.stocktradingserver.user.exception.DisabledUserException;
import edu.rmit.sef.stocktradingserver.user.model.SystemUser;
import edu.rmit.sef.stocktradingserver.user.repo.UserRepository;
import edu.rmit.sef.stocktradingserver.user.util.JwtUtil;
import edu.rmit.sef.stocktradingserver.user.exception.InvalidUserCredentialsException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class UserAuthService implements UserDetailsService {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        SystemUser user = userRepository.findUserByUsername(username);

        UserDetails userDetails = null;


        if (user != null) {
            userDetails = User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities("default")
                    .build();
        }

        return userDetails;
    }

    @Bean
    public ICommandHandler<InitCmd> initHandler() {

        return executionContext -> {


//            modelMapper.createTypeMap(RegisterCmd.class, SystemUser.class)
//                    .addMapping((s) -> s.getPassword(), (d, p) -> d.setPassword(passwordEncoder.encode((String) p)));
        };

    }

    @Bean
    public ICommandHandler<AuthenticateCmd> authenticateCmdHandler() {

        return executionContext -> {

            AuthenticateCmd cmd = executionContext.getCommand();

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(cmd.getUsername(), cmd.getPassword()));
            } catch (DisabledException e) {
                throw new DisabledUserException("User Inactive");
            } catch (BadCredentialsException e) {
                throw new InvalidUserCredentialsException("Invalid Credentials");
            }

            UserDetails userDetails = loadUserByUsername(cmd.getUsername());

            String token = jwtUtil.generateToken(userDetails);

            AuthenticateResp resp = new AuthenticateResp(userDetails.getUsername(), token);

            cmd.setResponse(resp);
        };

    }

    @Bean
    public ICommandHandler<RegisterUserCmd> registerCmdHandler() {
        return executionContext -> {
            RegisterUserCmd cmd = executionContext.getCommand();
            String username = cmd.getUsername();
            SystemUser user = userRepository.findUserByUsername(username);

            if (user != null) {
                CommandUtil.throwCommandExecutionException("User already exists.");
            }

            user = Entity.newEntity(null, SystemUser.class);
            modelMapper.map(cmd, user);

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.insert(user);

            RegisterUserResp resp = new RegisterUserResp(user.getId());
            cmd.setResponse(resp);

        };
    }

}
