package com.blackstone.account.configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Data
public class RabbitProperties {

    private String username;

    private String password;

    private String host;
    private String exchange;
    private String queue;
    private Routing routing;

    @Data
    public static class Routing {
        private String created;
        private String updated;
        private String deleted;
    }
}
