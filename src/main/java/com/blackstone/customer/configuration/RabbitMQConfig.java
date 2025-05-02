package com.blackstone.customer.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    private final RabbitProperties rabbitProperties;

    @Autowired
    public RabbitMQConfig(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }
    @Bean
    Queue customerQueue() {
        return QueueBuilder.durable(rabbitProperties.getQueue()).build();
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(rabbitProperties.getExchange());
    }
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(rabbitProperties.getHost());
        cachingConnectionFactory.setUsername(rabbitProperties.getUsername());
        cachingConnectionFactory.setPassword(rabbitProperties.getPassword());
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
    @Bean
    public Object bindQueueAndExchange() {
        Binding customerCreatedBinding = BindingBuilder.bind(customerQueue()).
                to(directExchange()).with(rabbitProperties.getRouting().getCreated());
        Binding customerUpdatedBinding = BindingBuilder.bind(customerQueue()).
                to(directExchange()).with(rabbitProperties.getRouting().getUpdated());
        Binding customerDeletedBinding = BindingBuilder.bind(customerQueue()).
                to(directExchange()).with(rabbitProperties.getRouting().getDeleted());
        amqpAdmin().declareBinding(customerCreatedBinding);
        amqpAdmin().declareBinding(customerUpdatedBinding);
        amqpAdmin().declareBinding(customerDeletedBinding);
        return null;
    }
}
