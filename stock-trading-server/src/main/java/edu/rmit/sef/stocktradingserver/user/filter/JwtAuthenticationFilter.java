package edu.rmit.sef.stocktradingserver.user.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.rmit.command.core.ICommandService;
import edu.rmit.command.core.ICommandServiceFactory;
import edu.rmit.sef.stocktradingserver.core.util.SecurityUtil;
import edu.rmit.sef.stocktradingserver.user.command.ValidateTokenCmd;
import edu.rmit.sef.stocktradingserver.user.command.ValidateTokenResp;
import edu.rmit.sef.stocktradingserver.user.exception.JwtTokenMissingException;
import edu.rmit.sef.user.model.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private ICommandServiceFactory commandServiceFactory;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer")) {
            throw new JwtTokenMissingException("No JWT token found in the request headers");
        }

        String token = SecurityUtil.getBearerToken(header);

        ICommandService commandService = commandServiceFactory.createService();

        ValidateTokenCmd validateTokenCmd = new ValidateTokenCmd(token);
        ValidateTokenResp validateTokenResp = commandService.execute(validateTokenCmd).join();


        SystemUser user = validateTokenResp.getUser();


        AbstractAuthenticationToken authenticationToken = SecurityUtil.getToken(user);

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityUtil.setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

}
