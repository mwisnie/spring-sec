package wm.springsec.security;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;


public class Conditions {

    public static class InMemoryActiveCondition implements ConfigurationCondition {
        @Override
        public ConfigurationCondition.ConfigurationPhase getConfigurationPhase() {
            return ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN;
        }

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return Boolean.parseBoolean(context.getEnvironment().getProperty("inMemoryActive"));
        }
    }

    public static class JdbcActiveCondition implements ConfigurationCondition {
        @Override
        public ConfigurationPhase getConfigurationPhase() {
            return ConfigurationPhase.REGISTER_BEAN;
        }

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return Boolean.parseBoolean(context.getEnvironment().getProperty("defaultDatabaseActive"));
        }
    }

    public static class CustomDaoActiveCondition implements ConfigurationCondition {
        @Override
        public ConfigurationPhase getConfigurationPhase() {
            return ConfigurationPhase.REGISTER_BEAN;
        }

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return Boolean.parseBoolean(context.getEnvironment().getProperty("customDatabaseActive"));
        }
    }


    public static class NoneActiveCondition implements ConfigurationCondition {
        @Override
        public ConfigurationPhase getConfigurationPhase() {
            return ConfigurationPhase.REGISTER_BEAN;
        }

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Environment env = context.getEnvironment();
            return !Boolean.parseBoolean(env.getProperty("inMemoryActive"))
                    && !Boolean.parseBoolean(env.getProperty("defaultDatabaseActive"))
                    && !Boolean.parseBoolean(env.getProperty("customDatabaseActive"));
        }
    }

}
