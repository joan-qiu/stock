package com.qqy.stockWealth.dal.entity;

import java.math.BigDecimal;

public class ShareHoldInfo {
    private Long id;

    private String stockId;

    private Integer stockNo;

    private BigDecimal stockBaseValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public Integer getStockNo() {
        return stockNo;
    }

    public void setStockNo(Integer stockNo) {
        this.stockNo = stockNo;
    }

    public BigDecimal getStockBaseValue() {
        return stockBaseValue;
    }

    public void setStockBaseValue(BigDecimal stockBaseValue) {
        this.stockBaseValue = stockBaseValue;
    }
}