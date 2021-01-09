package adelina.luxtravel.filter;

import adelina.luxtravel.exception.InvalidArgumentException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static adelina.luxtravel.utility.Constants.*;

/**
 * Represents custom authentication filter
 */
public class UserAuthenticationFiler extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager userAuthenticationManager;

    @Autowired
    public UserAuthenticationFiler(AuthenticationManager userAuthenticationManager) {
        this.userAuthenticationManager = userAuthenticationManager;

        setFilterProcessesUrl(LOGIN_URL);
    }

    /**
     * Extracts user's credentials from the request
     *
     * @param req http servlet request
     * @param res http servlet response
     * @return the user that is going to be authenticated
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
        try {
            adelina.luxtravel.domain.User credentials = new ObjectMapper().readValue(req.getInputStream(), adelina.luxtravel.domain.User.class);
            String username = credentials.getUsername();
            String password = credentials.getPassword();
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());

            return userAuthenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new InvalidArgumentException("Invalid data");
        }
    }

    /**
     * Forms token to be added in the response after successful authentication
     *
     * @param req   http servlet request
     * @param res   http servlet response
     * @param chain filter chain
     * @param auth  authentication object containing the principal (logged user)
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException {
        User user = (User) auth.getPrincipal();
        String username = user.getUsername();
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        Iterator<GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority authority = iterator.next();

        String token = JWT.create()
                .withSubject(username)
                .withClaim("role", authority.toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET));

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}