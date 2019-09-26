package edu.rmit.sef.stocktradingserver.user.service;

import edu.rmit.command.core.CommandUtil;
import edu.rmit.command.core.ICommandHandler;
import edu.rmit.command.core.InitCmd;
import edu.rmit.sef.core.command.CreateEntityResp;
import edu.rmit.sef.core.model.Entity;
import edu.rmit.sef.core.security.Authority;
import edu.rmit.sef.stocktradingserver.core.security.SecurityUtil;
import edu.rmit.sef.stocktradingserver.user.command.ValidateTokenCmd;
import edu.rmit.sef.stocktradingserver.user.command.ValidateTokenResp;
import edu.rmit.sef.stocktradingserver.user.exception.DisabledUserException;
import edu.rmit.sef.stocktradingserver.user.exception.InvalidUserCredentialsException;
import edu.rmit.sef.stocktradingserver.user.exception.JwtTokenMalformedException;
import edu.rmit.sef.stocktradingserver.user.exception.JwtTokenMissingException;
import edu.rmit.sef.stocktradingserver.user.repo.UserRepository;
import edu.rmit.sef.user.command.*;
import edu.rmit.sef.user.model.SystemUser;
import edu.rmit.sef.user.model.SystemUserPrincipal;
import io.jsonwebtoken.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Configuration
public class UserHandler implements UserDetailsService {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SecurityUtil securityUtil;


    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.token.validity}")
    private long tokenValidity;

    @Value("${edu.rmit.sef.stocktrading.server.admin.password}")
    private String defaultAdminPassword;


    private ConcurrentMap<String, List<String>> authorityCache = new ConcurrentHashMap<>();


    public String generateToken(SystemUser details) {
        Claims claims = Jwts.claims()
                .setSubject(details.getUsername())
                .setId(details.getId());

        claims.put("authorities", details.getAuthorities());

        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + tokenValidity;
        Date exp = new Date(expMillis);

        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(nowMillis)).setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }


    public Claims validateToken(final String token) {

        Jws<Claims> claims = null;

        try {
            claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        } catch (SignatureException ex) {
            throw new JwtTokenMalformedException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new JwtTokenMalformedException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenMalformedException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new JwtTokenMalformedException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new JwtTokenMissingException("JWT claims string is empty.");
        }

        return claims.getBody();
    }

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
        } else {
            throw new UsernameNotFoundException("User not found.");
        }

        return userDetails;
    }

    @Bean
    public ICommandHandler<InitCmd> initHandler() {

        return executionContext -> {
            if (userRepository.count() == 0) {
                RegisterUserCmd registerUserCmd = new RegisterUserCmd();
                registerUserCmd.setFirstName("System");
                registerUserCmd.setLastName("Administrator");
                registerUserCmd.setUsername("administrator");
                registerUserCmd.setPassword(defaultAdminPassword);
                registerUserCmd.setCompany("RMIT");
                CreateEntityResp registerUserResp = executionContext.getCommandService().execute(registerUserCmd).join();
                SystemUser systemUser = userRepository.findById(registerUserResp.getId()).get();
                systemUser.getAuthorities().add(Authority.ADMIN);
                userRepository.save(systemUser);
            }
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

            SystemUser user = userRepository.findUserByUsername(cmd.getUsername());
            String token = generateToken(user);

            AuthenticateResp resp = new AuthenticateResp(user.getFirstName(), user.getLastName(), token, user.getLastSeenOn());
            cmd.setResponse(resp);


            user.setLastSeenOn(new Date());
            userRepository.save(user);
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
            user.setLastSeenOn(new Date());

            if (cmd.getAuthorities() == null) {
                user.setAuthorities(new ArrayList<>());
            }

            if (user.getAuthorities().contains(Authority.ADMIN)) {
                if (!securityUtil.hasAuthority(executionContext.getUserId(), Authority.ADMIN)) {
                    CommandUtil.throwSecurityException();
                }
            }

            if (!user.getAuthorities().contains(Authority.USER)) {
                user.getAuthorities().add(Authority.USER);
            }

            user.validate();

            userRepository.insert(user);

            CreateEntityResp resp = new CreateEntityResp(user.getId());
            cmd.setResponse(resp);

        };
    }

    @Bean
    public ICommandHandler<ValidateTokenCmd> validateTokenCmdHandler() {
        return executionContext -> {

            ValidateTokenCmd cmd = executionContext.getCommand();
            String token = cmd.getToken();

            Claims body = validateToken(token);

            //Optional<SystemUser> user = userRepository.findById(body.getId());

            ValidateTokenResp resp = new ValidateTokenResp(new SystemUserPrincipal() {
                @Override
                public String getId() {
                    return body.getId();
                }

                @Override
                public String getUsername() {
                    return body.getSubject();
                }

                @Override
                public List<String> getAuthorities() {
                    return (List<String>) body.get("authorities");
                }

                @Override
                public String getName() {
                    return body.getId();
                }
            });
            cmd.setResponse(resp);

        };
    }


    @Bean
    public ICommandHandler<GetCurrentUserCmd> getCurrentUserCmd() {
        return executionContext -> {

            GetCurrentUserCmd cmd = executionContext.getCommand();
            String id = executionContext.getUserId();
            Optional<SystemUser> user = userRepository.findById(id);


            GetCurrentUserResp resp = new GetCurrentUserResp(user.get());
            cmd.setResponse(resp);

        };
    }

    @Bean
    public ICommandHandler<FindUserByIdCmd> findUserByIdHandler() {
        return executionContext -> {

            FindUserByIdCmd cmd = executionContext.getCommand();
            String id = cmd.getUserId();
            Optional<SystemUser> user = userRepository.findById(id);

            if (!user.isPresent()) {
                CommandUtil.throwRecordNotFoundException();
            }

            FindUserByIdResp resp = new FindUserByIdResp();
            resp.setUser(user.get());

            cmd.setResponse(resp);

        };
    }

    @Bean
    public ICommandHandler<HasAuthorityCmd> hasAuthorityHandler() {
        return executionContext -> {

            HasAuthorityCmd cmd = executionContext.getCommand();
            String userId = cmd.getUserId();


            boolean result = false;

            if (userId == null) {

                result = false;

            } else if (SystemUser.SYSTEM_USER_ID.compareTo(userId) == 0) {

                result = true;

            } else {

                List<String> ownedAuthorities = authorityCache.get(userId);

                if (ownedAuthorities == null) {

                    FindUserByIdCmd findUserByIdCmd = new FindUserByIdCmd();
                    findUserByIdCmd.setUserId(userId);

                    FindUserByIdResp resp = executionContext
                            .getCommandService()
                            .execute(findUserByIdCmd).join();

                    ownedAuthorities = resp.getUser().getAuthorities();

                }


                for (String authority : cmd.getAuthorities()) {
                    result = ownedAuthorities.contains(authority);
                }

            }


            HasAuthorityResp resp = new HasAuthorityResp();
            resp.setResult(result);

            cmd.setResponse(resp);

        };
    }


}
