package ua.task.test.users.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.task.test.users.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;

@Service
public class RequestFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestFilter.class);
    private static final String ADMIN_EMAIL = "admin";

    @Autowired
    UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authorization = httpServletRequest.getHeader("Authorization");
        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            // credentials = username:password
            final String[] values = credentials.split(":", 2);
            boolean isCorrectCredentials = userService.checkUserExistsByEmailAndPassword(values[0], values[1]);
            if (isCorrectCredentials) {
                String grantedAuthority = "ROLE_AUTHORIZED_USER";
                if (ADMIN_EMAIL.equals(values[0])) {
                    grantedAuthority = "ROLE_AUTHORIZED_ADMIN";
                }
                Collection<? extends GrantedAuthority> authorities = Collections
                        .singletonList(new SimpleGrantedAuthority(grantedAuthority));

                User principal = new User(values[0], values[1], authorities);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(principal, "", authorities);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                LOGGER.error("Wrong credentials specified, returning UNAUTHORIZED");
                httpServletResponse.setStatus(401);
            }
        } else {
            Collection<? extends GrantedAuthority> authorities = Collections
                    .singletonList(new SimpleGrantedAuthority("ROLE_UNAUTHORIZED_USER"));

            User principal = new User("user", "", authorities);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(principal, "", authorities);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
