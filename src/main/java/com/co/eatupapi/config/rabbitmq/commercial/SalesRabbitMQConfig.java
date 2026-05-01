package com.co.eatupapi.config.rabbitmq.commercial;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SalesRabbitMQConfig {

    @Value("${rabbitmq.exchange.sales:commercial.sales.exchange}")
    private String salesExchangeName;

    @Value("${rabbitmq.queue.sale-stock-discount:sales.stock-discount}")
    private String saleStockDiscountQueueName;

    @Value("${rabbitmq.queue.sale-preparation-result:sales.preparation-result}")
    private String salePreparationResultQueueName;

    @Value("${rabbitmq.routing-key.sale-stock-discount:sales.stock-discount}")
    private String saleStockDiscountRoutingKey;

    @Value("${rabbitmq.routing-key.sale-preparation-result:sales.preparation-result}")
    private String salePreparationResultRoutingKey;

    @Bean
    public DirectExchange salesExchange() {
        return new DirectExchange(salesExchangeName);
    }

    @Bean
    public Queue saleStockDiscountQueue() {
        return QueueBuilder.durable(saleStockDiscountQueueName).build();
    }

    @Bean
    public Queue salePreparationResultQueue() {
        return QueueBuilder.durable(salePreparationResultQueueName).build();
    }

    @Bean
    public Binding saleStockDiscountBinding(Queue saleStockDiscountQueue, DirectExchange salesExchange) {
        return BindingBuilder.bind(saleStockDiscountQueue).to(salesExchange).with(saleStockDiscountRoutingKey);
    }

    @Bean
    public Binding salePreparationResultBinding(Queue salePreparationResultQueue, DirectExchange salesExchange) {
        return BindingBuilder.bind(salePreparationResultQueue).to(salesExchange).with(salePreparationResultRoutingKey);
    }
}
