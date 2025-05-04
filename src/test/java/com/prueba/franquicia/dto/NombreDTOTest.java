package com.prueba.franquicia.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NombreDTOTest {

    @Test
    public void testConstructorVacio() {
        NombreDTO dto = new NombreDTO();
        assertNull(dto.getNombre());
    }

    @Test
    public void testConstructorConParametro() {
        NombreDTO dto = new NombreDTO("Producto X");
        assertEquals("Producto X", dto.getNombre());
    }

    @Test
    public void testSetterYGetter() {
        NombreDTO dto = new NombreDTO();
        dto.setNombre("Producto Y");
        assertEquals("Producto Y", dto.getNombre());
    }
}
