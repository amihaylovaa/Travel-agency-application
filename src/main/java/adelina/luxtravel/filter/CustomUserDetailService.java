package adelina.luxtravel.filter;

import adelina.luxtravel.domain.Role;
import adelina.luxtravel.domain.User;
import adelina.luxtravel.enumeration.RoleType;
import adelina.luxtravel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Custom implementation of UserDetailService
 */
@Component
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User with given username does not exists");
        }

        User user = userOptional.get();
        String password = user.getPassword();
        Role role = user.getRole();
        RoleType roleType = role.getRoleType();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + roleType.name());

        grantedAuthorities.add(grantedAuthority);

        return new org.springframework.security.core.userdetails.User(username, password, grantedAuthorities);
    }
}
