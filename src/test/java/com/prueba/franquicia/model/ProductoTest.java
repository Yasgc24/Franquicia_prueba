package com.prueba.franquicia.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductoTest {

    @Test
    void testAgregarProducto() {
        Producto producto = new Producto("Hamburguesa", 30);
        assertEquals("Hamburguesa", producto.getNombre());
        assertEquals(30, producto.getStock());
    }

    @Test
    void testModificarStock() {
        Producto producto = new Producto("Perro", 20);
        producto.setStock(45);
        assertEquals(45, producto.getStock());
    }
}
