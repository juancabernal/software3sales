package com.co.eatupapi.messaging.commercial.sales;

import com.co.eatupapi.messaging.commercial.sales.dto.SaleEventMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SaleEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.sales-create}")
    private String salesCreateExchange;
    @Value("${rabbitmq.routing-key.sales-create-request}")
    private String salesCreateRoutingKey;
    @Value("${rabbitmq.exchange.sales-update}")
    private String salesUpdateExchange;
    @Value("${rabbitmq.routing-key.sales-update-request}")
    private String salesUpdateRoutingKey;
    @Value("${rabbitmq.exchange.sales-patch}")
    private String salesPatchExchange;
    @Value("${rabbitmq.routing-key.sales-patch-request}")
    private String salesPatchRoutingKey;
    @Value("${rabbitmq.exchange.sales-delete}")
    private String salesDeleteExchange;
    @Value("${rabbitmq.routing-key.sales-delete-request}")
    private String salesDeleteRoutingKey;

    public SaleEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishSaleCreateRequested(SaleEventMessage message) { rabbitTemplate.convertAndSend(salesCreateExchange, salesCreateRoutingKey, message); }
    public void publishSaleUpdateRequested(SaleEventMessage message) { rabbitTemplate.convertAndSend(salesUpdateExchange, salesUpdateRoutingKey, message); }
    public void publishSalePatchRequested(SaleEventMessage message) { rabbitTemplate.convertAndSend(salesPatchExchange, salesPatchRoutingKey, message); }
    public void publishSaleDeleteRequested(SaleEventMessage message) { rabbitTemplate.convertAndSend(salesDeleteExchange, salesDeleteRoutingKey, message); }
}
