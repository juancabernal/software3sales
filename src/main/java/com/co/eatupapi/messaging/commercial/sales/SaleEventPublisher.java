package com.co.eatupapi.messaging.commercial.sales;

import com.co.eatupapi.domain.commercial.sales.SaleDomain;
import java.util.UUID;

public interface SaleEventPublisher {
    void publishSaleCreatedEvent(SaleDomain sale);
    void publishStockDiscountCompletedEvent(UUID saleDetailId);
    void publishStockDiscountFailedEvent(UUID saleDetailId, String reason);
}
