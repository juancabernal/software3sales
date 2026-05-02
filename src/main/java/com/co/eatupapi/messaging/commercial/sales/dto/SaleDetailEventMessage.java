package com.co.eatupapi.messaging.commercial.sales.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class SaleDetailEventMessage {
    private UUID saleDetailId;
    private UUID recipeId;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private String recipeLineComment;
    private String lineDisplayName;

    public UUID getSaleDetailId() { return saleDetailId; }
    public void setSaleDetailId(UUID saleDetailId) { this.saleDetailId = saleDetailId; }
    public UUID getRecipeId() { return recipeId; }
    public void setRecipeId(UUID recipeId) { this.recipeId = recipeId; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public String getRecipeLineComment() { return recipeLineComment; }
    public void setRecipeLineComment(String recipeLineComment) { this.recipeLineComment = recipeLineComment; }
    public String getLineDisplayName() { return lineDisplayName; }
    public void setLineDisplayName(String lineDisplayName) { this.lineDisplayName = lineDisplayName; }
}
