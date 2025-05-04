package com.prueba.franquicia.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StockDTOTest {

    @Test
    public void testConstructorVacio() {
        StockDTO dto = new StockDTO();
        assertNull(dto.getStock());
    }

    @Test
    public void testConstructorConParametro() {
        StockDTO dto = new StockDTO(100);
        assertEquals(100, dto.getStock());
    }

    @Test
    public void testSetterYGetter() {
        StockDTO dto = new StockDTO();
        dto.setStock(50);
        assertEquals(50, dto.getStock());
    }
}
