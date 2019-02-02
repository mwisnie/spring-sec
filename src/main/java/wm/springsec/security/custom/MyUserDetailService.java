package wm.springsec.security.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userSQL = "select * from users where username = ?";
        User user = jdbcTemplate.queryForObject(userSQL, new Object[]{username}, new UserMapper());

        if (user == null) {
            throw new UsernameNotFoundException("Username " + username + " not found.");
        }

        // FIXME: possibly no wrapper, support for multiple authorities
        String authoritySQL = "select * from authorities where username = ?";
        AuthorityWrapper authorityWrapper = jdbcTemplate.queryForObject(authoritySQL, new Object[]{username}, new AuthorityMapper());


        if (authorityWrapper.getAuthority() != null) {
            user.addAuthority(authorityWrapper);
        }

        return user;
    }

}
