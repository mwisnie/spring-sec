package wm.springsec.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        String encodedPassword = "$2a$04$PVAMCIP5AEVFIbdDu7LCneXIuERwIRAlk7.582nO8RYXw2KvC2Fza";

        auth.inMemoryAuthentication()
                .withUser("user").password(encodedPassword).roles("USER")
                .and()
                .withUser("manager").password(encodedPassword).roles("USER, MANAGEMENT")
                .and()
                .withUser("admin").password(encodedPassword).roles("USER, ADMIN");
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeRequests()
                .antMatchers("/everyone/**").hasRole("USER")
                .antMatchers("/management/**").hasRole("MANAGEMENT")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/doLogin")
                    .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied");
    }


}
