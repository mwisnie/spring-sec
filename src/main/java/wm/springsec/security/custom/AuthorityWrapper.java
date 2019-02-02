package wm.springsec.security.custom;

import org.springframework.security.core.GrantedAuthority;

public class AuthorityWrapper implements GrantedAuthority {

    private String username;

    private String authority;

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return authority;
    }
}
