package com.co.eatupapi.services.commercial.sales.impl;

import com.co.eatupapi.dto.commercial.sales.SaleAsyncResponseDTO;
import com.co.eatupapi.dto.commercial.sales.SaleDetailDTO;
import com.co.eatupapi.dto.commercial.sales.SalePatchDTO;
import com.co.eatupapi.dto.commercial.sales.SaleRequestDTO;
import com.co.eatupapi.dto.commercial.sales.SaleResponseDTO;
import com.co.eatupapi.messaging.commercial.sales.SaleEventPublisher;
import com.co.eatupapi.messaging.commercial.sales.dto.SaleDetailEventMessage;
import com.co.eatupapi.messaging.commercial.sales.dto.SaleEventMessage;
import com.co.eatupapi.messaging.commercial.sales.dto.SaleEventType;
import com.co.eatupapi.repositories.commercial.sales.SaleRepository;
import com.co.eatupapi.services.commercial.sales.SaleService;
import com.co.eatupapi.utils.commercial.sales.exceptions.SaleNotFoundException;
import com.co.eatupapi.utils.commercial.sales.exceptions.SaleValidationException;
import com.co.eatupapi.utils.commercial.sales.mapper.SaleMapper;
import com.co.eatupapi.utils.commercial.sales.validation.ValidationUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleServiceImpl implements SaleService {
    private static final String VENTA_NO_ENCONTRADA = "No existe una venta con el id: ";
    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;
    private final SaleEventPublisher saleEventPublisher;

    public SaleServiceImpl(SaleRepository saleRepository, SaleMapper saleMapper, SaleEventPublisher saleEventPublisher) {
        this.saleRepository = saleRepository;
        this.saleMapper = saleMapper;
        this.saleEventPublisher = saleEventPublisher;
    }

    @Override @Transactional
    public SaleAsyncResponseDTO createSale(SaleRequestDTO request) {
        validateRequiredSalePayload(request);
        UUID saleId = UUID.randomUUID();
        SaleEventMessage message = buildEventMessage(SaleEventType.SALE_CREATE_REQUESTED, saleId, request.getSellerId(), request.getLocationId(), request.getTableId(), request.getDetails());
        saleEventPublisher.publishSaleCreateRequested(message);
        return buildAsyncResponse(saleId, "Solicitud de creación de venta enviada a procesamiento.", SaleEventType.SALE_CREATE_REQUESTED);
    }

    @Override @Transactional(readOnly = true)
    public SaleResponseDTO getSaleById(UUID id) { return saleMapper.toDto(saleRepository.findById(id).orElseThrow(() -> new SaleNotFoundException(VENTA_NO_ENCONTRADA + id))); }

    @Override @Transactional(readOnly = true)
    public List<SaleResponseDTO> getAllSales() { return saleRepository.findAll().stream().map(saleMapper::toDto).toList(); }

    @Override @Transactional
    public SaleAsyncResponseDTO updateSale(UUID id, SaleRequestDTO request) {
        ValidationUtils.requireObject(id, "El id de venta es obligatorio.");
        validateRequiredSalePayload(request);
        SaleEventMessage message = buildEventMessage(SaleEventType.SALE_UPDATE_REQUESTED, id, request.getSellerId(), request.getLocationId(), request.getTableId(), request.getDetails());
        saleEventPublisher.publishSaleUpdateRequested(message);
        return buildAsyncResponse(id, "Solicitud de actualización de venta enviada a procesamiento.", SaleEventType.SALE_UPDATE_REQUESTED);
    }

    @Override @Transactional
    public SaleAsyncResponseDTO patchSale(UUID id, SalePatchDTO request) {
        ValidationUtils.requireObject(id, "El id de venta es obligatorio.");
        ValidationUtils.requireObject(request, "El payload de patch es obligatorio.");
        List<SaleDetailEventMessage> details = request.details() == null ? null : buildDetailMessages(request.details());
        BigDecimal totalAmount = details == null ? null : details.stream().map(SaleDetailEventMessage::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        SaleEventMessage message = buildBaseEvent(SaleEventType.SALE_PATCH_REQUESTED, id);
        message.setSellerId(trimToNull(request.sellerId()));
        message.setLocationId(request.locationId());
        message.setTableId(trimToNull(request.tableId()));
        message.setDetails(details);
        message.setTotalAmount(totalAmount);
        saleEventPublisher.publishSalePatchRequested(message);
        return buildAsyncResponse(id, "Solicitud de actualización parcial de venta enviada a procesamiento.", SaleEventType.SALE_PATCH_REQUESTED);
    }

    @Override @Transactional
    public SaleAsyncResponseDTO deleteSale(UUID id) {
        ValidationUtils.requireObject(id, "El id de venta es obligatorio.");
        SaleEventMessage message = buildBaseEvent(SaleEventType.SALE_DELETE_REQUESTED, id);
        message.setDetails(Collections.emptyList());
        saleEventPublisher.publishSaleDeleteRequested(message);
        return buildAsyncResponse(id, "Solicitud de eliminación de venta enviada a procesamiento.", SaleEventType.SALE_DELETE_REQUESTED);
    }

    private void validateRequiredSalePayload(SaleRequestDTO request) {
        ValidationUtils.requireObject(request, "El payload de venta es obligatorio.");
        ValidationUtils.requireText(request.getSellerId(), "sellerId");
        ValidationUtils.requireObject(request.getLocationId(), "La locationId es obligatoria.");
        ValidationUtils.requireText(request.getTableId(), "tableId");
        ValidationUtils.requireObject(request.getDetails(), "La lista de detalles es obligatoria.");
        if (request.getDetails().isEmpty()) throw new SaleValidationException("La venta debe tener al menos una línea de detalle.");
        buildDetailMessages(request.getDetails());
    }

    private List<SaleDetailEventMessage> buildDetailMessages(List<SaleDetailDTO> details) {
        if (details.isEmpty()) throw new SaleValidationException("La venta debe tener al menos una línea de detalle.");
        return details.stream().map(this::mapDetail).toList();
    }

    private SaleDetailEventMessage mapDetail(SaleDetailDTO detail) {
        ValidationUtils.requireObject(detail, "Cada línea de detalle es obligatoria.");
        ValidationUtils.requireObject(detail.getRecipeId(), "El recipeId es obligatorio en cada línea.");
        ValidationUtils.requirePositive(detail.getQuantity(), "La cantidad debe ser mayor que cero.");
        ValidationUtils.requirePositive(detail.getUnitPrice(), "El precio unitario debe ser mayor que cero.");
        ValidationUtils.requireText(detail.getRecipeLineComment(), "recipeLineComment");
        ValidationUtils.validateMaxLength(detail.getRecipeLineComment(), 500, "recipeLineComment");
        ValidationUtils.validateMaxLength(detail.getLineDisplayName(), 255, "lineDisplayName");
        SaleDetailEventMessage message = new SaleDetailEventMessage();
        message.setSaleDetailId(UUID.randomUUID());
        message.setRecipeId(detail.getRecipeId());
        message.setQuantity(detail.getQuantity());
        message.setUnitPrice(detail.getUnitPrice());
        message.setSubtotal(detail.getUnitPrice().multiply(detail.getQuantity()));
        message.setRecipeLineComment(detail.getRecipeLineComment().trim());
        message.setLineDisplayName(trimToNull(detail.getLineDisplayName()));
        return message;
    }

    private SaleEventMessage buildEventMessage(SaleEventType type, UUID saleId, String sellerId, UUID locationId, String tableId, List<SaleDetailDTO> details) {
        List<SaleDetailEventMessage> detailMessages = buildDetailMessages(details);
        BigDecimal totalAmount = detailMessages.stream().map(SaleDetailEventMessage::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        SaleEventMessage event = buildBaseEvent(type, saleId);
        event.setSellerId(trimToNull(sellerId));
        event.setLocationId(locationId);
        event.setTableId(trimToNull(tableId));
        event.setDetails(detailMessages);
        event.setTotalAmount(totalAmount);
        return event;
    }

    private SaleEventMessage buildBaseEvent(SaleEventType type, UUID saleId) {
        SaleEventMessage event = new SaleEventMessage();
        event.setEventId(UUID.randomUUID());
        event.setEventType(type.name());
        event.setSaleId(saleId);
        event.setOccurredAt(LocalDateTime.now());
        return event;
    }

    private SaleAsyncResponseDTO buildAsyncResponse(UUID saleId, String message, SaleEventType type) {
        SaleAsyncResponseDTO response = new SaleAsyncResponseDTO();
        response.setSaleId(saleId);
        response.setMessage(message);
        response.setEventType(type.name());
        response.setPublishedAt(LocalDateTime.now());
        return response;
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
