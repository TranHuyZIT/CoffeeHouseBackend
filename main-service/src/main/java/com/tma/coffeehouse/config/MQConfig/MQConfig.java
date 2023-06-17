package com.tma.coffeehouse.config.MQConfig;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    public static final String QUEUE_NAME = "send_mail_queue";
    public static final String TOPIC_EXCHANGE = "topic_exchange";
    public static final String ROUTING_KEY = "send_mail_routing_key";

    public static final String NOTIFICATION_QUEUE_NAME = "notification_queue";
    public static final String DIRECT_EXCHANGE = "direct_exchange";
    public static final String NOTIFICATION_ROUTING_KEY = "notification_routing_key";
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.vhost}")
    private String vhost;
    @Bean
    @Qualifier("send_email")
    public Queue queueEmail(){
        return new Queue(QUEUE_NAME);
    }
    @Bean
    @Qualifier("notification")
    public Queue queueNotification() {
        return new Queue(NOTIFICATION_QUEUE_NAME);
    }
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }
    @Bean
    public DirectExchange directExchange (){
        return new DirectExchange(DIRECT_EXCHANGE);
    }
    @Bean
    public Binding bindingEmail(DirectExchange exchange){
        return BindingBuilder.bind(queueEmail()).to(exchange).with(ROUTING_KEY);
    }
    @Bean Binding bindingNotification (DirectExchange exchange){
        return BindingBuilder.bind(queueNotification()).to(exchange).with(NOTIFICATION_ROUTING_KEY);
    }
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(vhost);
        return connectionFactory;
    }
    @Bean
    public AmqpTemplate template (ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
