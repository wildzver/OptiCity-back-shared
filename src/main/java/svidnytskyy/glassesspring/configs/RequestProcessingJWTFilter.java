package svidnytskyy.glassesspring.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;


public class RequestProcessingJWTFilter extends GenericFilterBean {

    // react on every url (but we can change it if implement another filter)
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        Authentication authentication = null;
//

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // and check presents of token in header Authorization
        String token = httpServletRequest.getHeader("Authorization");
        // if present
        if (token != null) {
            // parse it and retrive body subject from
            Claims claims = Jwts.parser()
                    .setSigningKey("yes".getBytes())
                    .parseClaimsJws(token.replace("Bearer", ""))
                    .getBody();

            String user = claims.getSubject();
            String role = claims.get("ROLE", String.class);

            System.out.println(user + "!!!!!!!!!!!---!!!!!");

            //after parse of token we create Authentication object
            authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));
        }
        // and set it to global security context
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}