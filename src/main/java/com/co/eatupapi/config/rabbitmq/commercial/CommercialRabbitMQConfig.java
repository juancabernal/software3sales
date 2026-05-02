package com.co.eatupapi.config.rabbitmq.commercial;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommercialRabbitMQConfig {

    @Value("${rabbitmq.exchange.commercial}")
    private String exchangeName;

    @Value("${rabbitmq.queue.purchase}")
    private String queueName;

    @Value("${rabbitmq.routing-key.purchase}")
    private String routingKey;

    @Value("${rabbitmq.exchange.sales-create}")
    private String salesCreateExchangeName;

    @Value("${rabbitmq.queue.sales-create-request}")
    private String salesCreateQueueName;

    @Value("${rabbitmq.routing-key.sales-create-request}")
    private String salesCreateRoutingKey;

    @Value("${rabbitmq.exchange.sales-update}")
    private String salesUpdateExchangeName;

    @Value("${rabbitmq.queue.sales-update-request}")
    private String salesUpdateQueueName;

    @Value("${rabbitmq.routing-key.sales-update-request}")
    private String salesUpdateRoutingKey;

    @Value("${rabbitmq.exchange.sales-patch}")
    private String salesPatchExchangeName;

    @Value("${rabbitmq.queue.sales-patch-request}")
    private String salesPatchQueueName;

    @Value("${rabbitmq.routing-key.sales-patch-request}")
    private String salesPatchRoutingKey;

    @Value("${rabbitmq.exchange.sales-delete}")
    private String salesDeleteExchangeName;

    @Value("${rabbitmq.queue.sales-delete-request}")
    private String salesDeleteQueueName;

    @Value("${rabbitmq.routing-key.sales-delete-request}")
    private String salesDeleteRoutingKey;


    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.initialize();
        return admin;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean
    public DirectExchange commercialExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Queue purchaseQueue() {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Binding purchaseBinding(Queue purchaseQueue, DirectExchange commercialExchange) {
        return BindingBuilder
                .bind(purchaseQueue)
                .to(commercialExchange)
                .with(routingKey);
    }

    @Bean
    public DirectExchange salesCreateExchange() {
        return new DirectExchange(salesCreateExchangeName);
    }

    @Bean
    public Queue salesCreateRequestQueue() {
        return QueueBuilder.durable(salesCreateQueueName).build();
    }

    @Bean
    public Binding salesCreateBinding(Queue salesCreateRequestQueue, DirectExchange salesCreateExchange) {
        return BindingBuilder
                .bind(salesCreateRequestQueue)
                .to(salesCreateExchange)
                .with(salesCreateRoutingKey);
    }

    @Bean
    public DirectExchange salesUpdateExchange() {
        return new DirectExchange(salesUpdateExchangeName);
    }

    @Bean
    public Queue salesUpdateRequestQueue() {
        return QueueBuilder.durable(salesUpdateQueueName).build();
    }

    @Bean
    public Binding salesUpdateBinding(Queue salesUpdateRequestQueue, DirectExchange salesUpdateExchange) {
        return BindingBuilder
                .bind(salesUpdateRequestQueue)
                .to(salesUpdateExchange)
                .with(salesUpdateRoutingKey);
    }

    @Bean
    public DirectExchange salesPatchExchange() {
        return new DirectExchange(salesPatchExchangeName);
    }

    @Bean
    public Queue salesPatchRequestQueue() {
        return QueueBuilder.durable(salesPatchQueueName).build();
    }

    @Bean
    public Binding salesPatchBinding(Queue salesPatchRequestQueue, DirectExchange salesPatchExchange) {
        return BindingBuilder
                .bind(salesPatchRequestQueue)
                .to(salesPatchExchange)
                .with(salesPatchRoutingKey);
    }

    @Bean
    public DirectExchange salesDeleteExchange() {
        return new DirectExchange(salesDeleteExchangeName);
    }

    @Bean
    public Queue salesDeleteRequestQueue() {
        return QueueBuilder.durable(salesDeleteQueueName).build();
    }

    @Bean
    public Binding salesDeleteBinding(Queue salesDeleteRequestQueue, DirectExchange salesDeleteExchange) {
        return BindingBuilder
                .bind(salesDeleteRequestQueue)
                .to(salesDeleteExchange)
                .with(salesDeleteRoutingKey);
    }
}
