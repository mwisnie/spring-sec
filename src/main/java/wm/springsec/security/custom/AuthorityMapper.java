package wm.springsec.security.custom;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorityMapper implements RowMapper<AuthorityWrapper> {

    @Override
    public AuthorityWrapper mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthorityWrapper authorityWrapper = new AuthorityWrapper();
        authorityWrapper.setUsername(rs.getString("username"));
        authorityWrapper.setAuthority(rs.getString("authority"));
        return authorityWrapper;
    }

}
