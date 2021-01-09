package adelina.luxtravel.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static adelina.luxtravel.utility.Constants.*;

/**
 * Represents custom authorization filter
 */
public class UserAuthorizationFilter extends BasicAuthenticationFilter {

    public UserAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * Searches for an 'Authorization' header in the http request
     * in order to get JWT and identify the sender
     *
     * @param req   http request
     * @param res   http response
     * @param chain the filter chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    /**
     * Decodes JWT and extracts data from its payload
     *
     * @param request the incoming http request
     * @return Authentication object containing data for the sender stored in JWT's payload
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);

        if (token != null) {
            String tokenWithoutPrefix = token.replace(TOKEN_PREFIX, "");
            DecodedJWT jwt = JWT.decode(tokenWithoutPrefix);
            String username = jwt.getSubject();
            String role = jwt.getClaim("role").asString();
            List<GrantedAuthority> authorities = new ArrayList<>();
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);

            authorities.add(grantedAuthority);
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }
        return null;
    }
}
