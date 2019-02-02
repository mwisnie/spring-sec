package wm.springsec.security.custom;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User u = new User();
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password").replace("{bcrypt}", ""));
        u.setEnabled(rs.getBoolean("enabled"));
        return u;
    }

}
