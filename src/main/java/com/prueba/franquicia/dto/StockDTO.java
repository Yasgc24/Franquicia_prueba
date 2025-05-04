package com.prueba.franquicia.dto;


public class StockDTO {
    private Integer stock;

    public StockDTO() {
    }

    public StockDTO(Integer stock) {
        this.stock = stock;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}