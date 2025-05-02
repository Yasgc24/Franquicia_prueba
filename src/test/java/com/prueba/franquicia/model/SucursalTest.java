package com.prueba.franquicia.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SucursalTest {

    @Test
    void testAgregarProductosASucursal() {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre("Belen");

        Producto producto = new Producto("Perro", 30);
        sucursal.getProductos().add(producto);
        
        assertEquals(1, sucursal.getProductos().size());
        assertEquals("Perro", sucursal.getProductos().get(0).getNombre());
    }
}
