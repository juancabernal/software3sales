package com.co.eatupapi.messaging.commercial.sales;

import com.co.eatupapi.domain.commercial.sales.SaleDomain;
import java.util.Map;
import java.util.UUID;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SaleEventPublisherImpl implements SaleEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.sales:commercial.sales.exchange}")
    private String salesExchange;

    @Value("${rabbitmq.routing-key.sale-stock-discount:sales.stock-discount}")
    private String saleStockDiscountRoutingKey;

    @Value("${rabbitmq.routing-key.sale-preparation-result:sales.preparation-result}")
    private String salePreparationResultRoutingKey;

    public SaleEventPublisherImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishSaleCreatedEvent(SaleDomain sale) {
        rabbitTemplate.convertAndSend(salesExchange, saleStockDiscountRoutingKey, sale);
    }

    @Override
    public void publishStockDiscountCompletedEvent(UUID saleDetailId) {
        rabbitTemplate.convertAndSend(salesExchange, salePreparationResultRoutingKey,
                Map.of("saleDetailId", saleDetailId, "status", "COMPLETED"));
    }

    @Override
    public void publishStockDiscountFailedEvent(UUID saleDetailId, String reason) {
        rabbitTemplate.convertAndSend(salesExchange, salePreparationResultRoutingKey,
                Map.of("saleDetailId", saleDetailId, "status", "FAILED", "reason", reason));
    }
}
