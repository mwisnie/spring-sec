package wm.springsec.security;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
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

    @Conditional(Conditions.DaoActiveCondition.class)
    @Configuration
    @Order(85)
    public static class DaoSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {

            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

            auth.inMemoryAuthentication()
                    .withUser("user").password(encoder.encode("123")).roles("USER")
                    .and()
                    .withUser("manager").password(encoder.encode("123")).roles("USER, MANAGEMENT")
                    .and()
                    .withUser("admin").password(encoder.encode("123")).roles("USER, ADMIN");
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


}
