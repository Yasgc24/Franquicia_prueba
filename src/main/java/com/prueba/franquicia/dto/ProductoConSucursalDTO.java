package com.prueba.franquicia.dto;

import com.prueba.franquicia.model.Producto;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ProductoConSucursalDTO {
    
    @JsonProperty("producto")
    private Producto producto;
    
    @JsonProperty("sucursalId")
    private String sucursalId;
    
    @JsonProperty("sucursalNombre")
    private String sucursalNombre;
    
    public ProductoConSucursalDTO() {
    }
    
    public ProductoConSucursalDTO(Producto producto, String sucursalId, String sucursalNombre) {
        this.producto = producto;
        this.sucursalId = sucursalId;
        this.sucursalNombre = sucursalNombre;
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public String getSucursalId() {
        return sucursalId;
    }
    
    public void setSucursalId(String sucursalId) {
        this.sucursalId = sucursalId;
    }
    
    public String getSucursalNombre() {
        return sucursalNombre;
    }
    
    public void setSucursalNombre(String sucursalNombre) {
        this.sucursalNombre = sucursalNombre;
    }
}