package wm.springsec;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.logging.Logger;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "wm.springsec")
@PropertySource("classpath:persistence.properties")
public class AppConfig {

    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    private Environment env;

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.addDialect(new SpringSecurityDialect());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        return viewResolver;
    }

    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource securityDataSource = new ComboPooledDataSource();

        try {
            securityDataSource.setDriverClass(env.getProperty("jdbc.driver"));
        } catch (PropertyVetoException exc) {
            throw new RuntimeException(exc);
        }

        securityDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
        securityDataSource.setUser(env.getProperty("jdbc.user"));
        securityDataSource.setPassword(env.getProperty("jdbc.password"));

        securityDataSource.setInitialPoolSize(getIntPropertyAsInt("connection.pool.initialPoolSize"));
        securityDataSource.setMinPoolSize(getIntPropertyAsInt("connection.pool.minPoolSize"));
        securityDataSource.setMaxPoolSize(getIntPropertyAsInt("connection.pool.maxPoolSize"));
        securityDataSource.setMaxIdleTime(getIntPropertyAsInt("connection.pool.maxIdleTime"));

        logger.info("Attempting connection with:");
        logger.info("jdbc.driver: " + env.getProperty("jdbc.driver"));
        logger.info("jdbc.url: " + env.getProperty("jdbc.url"));
        logger.info("jdbc.user: " + env.getProperty("jdbc.user"));
        logger.info("jdbc.password: " + env.getProperty("jdbc.password"));

        return securityDataSource;
    }

    private int getIntPropertyAsInt(String propName) {
        return Integer.parseInt((env.getProperty(propName)));
    }

}
