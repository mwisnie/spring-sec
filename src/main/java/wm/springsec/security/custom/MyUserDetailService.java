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
        String SQL = "select * from users where username = ?";
        User user = jdbcTemplate.queryForObject(SQL, new Object[]{username}, new UserMapper());

        if (user == null) {
            throw new UsernameNotFoundException("Username " + username + " not found.");
        }

        // todo: select roles for user

        return user;
    }

}
