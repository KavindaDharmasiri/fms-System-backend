/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.service.IMPL;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_auth.entity.User;
import net.com.fms_auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CustomUserDetailsServiceIMPL implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);
        User user = userRepository.findByUsernameWithRoles(username).orElseThrow(
                () ->{
                    log.error("User not found with email: {}", username);
                    return new UsernameNotFoundException("User not found with email : " + username);
                }
        );
        log.info("User found: {}, Password hash: {}", user.getUsername(), user.getPassword());
        return user;
    }
}
