package adelina.luxtravel.configuration;

import adelina.luxtravel.filter.CustomUserDetailService;
import adelina.luxtravel.filter.UserAuthenticationFiler;
import adelina.luxtravel.filter.UserAuthorizationFilter;
import adelina.luxtravel.enumeration.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Security configuration class
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    private static final String ADMIN = RoleType.ADMIN.name();

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
                 auth
                .userDetailsService(customUserDetailService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] allowedGetRequestOnlyForAdmin = {"/users", "/users/email/*", "/bookings", "/traveling-points/*",
                "/traveling-points", "/traveling-points/all/*"};
        String[] allowedPostRequestOnlyForAdmin = {"/traveling-points", "/transports", "/excursions"};
        String[] allowedPatchRequestOnlyForAdmin = {"/users/*/role", "/transports/*/class", "/excursions/*/dates"};
        String[] allowedDeleteRequestOnlyForAdmin = {"/transports/*", "/traveling-points/*", "/excursions/*"};

        http.headers().defaultsDisabled().cacheControl();
        http.csrf().disable();

                 http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, allowedGetRequestOnlyForAdmin).hasRole(ADMIN)
                .antMatchers(HttpMethod.PATCH, allowedPatchRequestOnlyForAdmin).hasRole(ADMIN)
                .antMatchers(HttpMethod.POST, allowedPostRequestOnlyForAdmin).hasRole(ADMIN)
                .antMatchers(HttpMethod.DELETE, allowedDeleteRequestOnlyForAdmin).hasRole(ADMIN)
                .and()
                .addFilter(new UserAuthenticationFiler(authenticationManager()))
                .addFilter(new UserAuthorizationFilter(authenticationManager()))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}