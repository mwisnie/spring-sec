package wm.springsec.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import wm.springsec.security.custom.MyUserDetailService;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@PropertySource("classpath:app.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {



    @Conditional(Conditions.InMemoryActiveCondition.class)
    @Configuration
    @Order(90)
    public static class InMemorySecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {

            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

            auth.inMemoryAuthentication()
                    .withUser("user").password(encoder.encode("123")).roles("USER")
                    .and()
                    .withUser("manager").password(encoder.encode("123")).roles("USER", "MANAGEMENT")
                    .and()
                    .withUser("admin").password(encoder.encode("123")).roles("USER", "ADMIN");
        }

        @Override
        public void configure(HttpSecurity httpSecurity) throws Exception {

            httpSecurity.authorizeRequests()
                    .antMatchers("/everyone/**").hasAnyRole("USER")
                    .antMatchers("/management/**").hasAnyRole("MANAGEMENT")
                    .antMatchers("/admin/**").hasAnyRole("ADMIN")
                    .and()
                        .formLogin()
                        .loginPage("/login")
                        .loginProcessingUrl("/doLogin")
                        .permitAll()
                    .and()
                        .logout()
                        .logoutSuccessUrl("/")
                        .permitAll()
                    .and()
                        .exceptionHandling().accessDeniedPage("/access-denied");
        }
    }




    @Conditional(Conditions.JdbcActiveCondition.class)
    @Configuration
    @Order(85)
    public static class JdbcSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private DataSource securityDataSource;

        @Autowired
        private AuthenticationFailureHandler myFailureHandler;

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            // bcrypt sed by default
            auth.jdbcAuthentication().dataSource(securityDataSource);

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
//                        .failureHandler(myFailureHandler) // diagnostic
                        .permitAll()
                    .and()
                        .logout()
                        .logoutSuccessUrl("/")
                        .permitAll()
                    .and()
                        .exceptionHandling().accessDeniedPage("/access-denied");
        }
    }




    @Conditional(Conditions.CustomDaoActiveCondition.class)
    @Configuration
    @Order(85)
    public static class CustomDaoSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private AuthenticationProvider authenticationProvider;

        @Autowired
        private AuthenticationFailureHandler myFailureHandler;

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(authenticationProvider);
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
//                        .failureHandler(myFailureHandler) // diagnostic
                    .permitAll()
                    .and()
                    .logout()
                    .logoutSuccessUrl("/")
                    .permitAll()
                    .and()
                    .exceptionHandling().accessDeniedPage("/access-denied");
        }
    }




    @Conditional(Conditions.NoneActiveCondition.class)
    @Configuration
    @Order(80)
    public static class DisabledSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(HttpSecurity httpSecurity) throws Exception {

            httpSecurity.authorizeRequests()
                    .antMatchers("/*").permitAll();
        }
    }



    @Bean
    public AuthenticationFailureHandler myAuthFailureHandler() {
        return new MyAuthFailureHandler();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailService();
    }

    @Conditional(Conditions.CustomDaoActiveCondition.class)
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Conditional(Conditions.CustomDaoActiveCondition.class)
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(encoder());
        authenticationProvider.setUserDetailsService(userDetailsService());
        return authenticationProvider;
    }

}
