package com.prueba.franquicia.dto;

import com.prueba.franquicia.model.Producto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductoConSucursalDTOTest {

    @Test
    public void testConstructorConParametros() {
        Producto producto = new Producto();
        String sucursalId = "123";
        String sucursalNombre = "Sucursal principal";

        ProductoConSucursalDTO dto = new ProductoConSucursalDTO(producto, sucursalId, sucursalNombre);

        assertEquals(producto, dto.getProducto());
        assertEquals(sucursalId, dto.getSucursalId());
        assertEquals(sucursalNombre, dto.getSucursalNombre());
    }

    @Test
    public void testConstructorVacioYSettersYGetters() {
        Producto producto = new Producto();
        String sucursalId = "456";
        String sucursalNombre = "Sucursal 2";

        ProductoConSucursalDTO dto = new ProductoConSucursalDTO();
        dto.setProducto(producto);
        dto.setSucursalId(sucursalId);
        dto.setSucursalNombre(sucursalNombre);

        assertEquals(producto, dto.getProducto());
        assertEquals(sucursalId, dto.getSucursalId());
        assertEquals(sucursalNombre, dto.getSucursalNombre());
    }
}
