package com.prueba.franquicia.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import java.util.ArrayList;
import java.util.List;

public class Sucursal {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String nombre;
    private List<Producto> productos = new ArrayList<>();

    public Sucursal() {
    }

    public Sucursal(String nombre) {
        this.nombre = nombre;
    }

    public Sucursal(String nombre, List<Producto> productos) {
        this.nombre = nombre;
        this.productos = productos;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public void agregarProducto(Producto producto) {
        this.productos.add(producto);
    }
}