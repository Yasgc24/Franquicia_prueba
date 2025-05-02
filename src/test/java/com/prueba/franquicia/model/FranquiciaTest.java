package com.prueba.franquicia.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class FranquiciaTest {

    @Test
    void testAgregarSucursalAFranquicia() {
        Franquicia franquicia = new Franquicia();
        franquicia.setNombre("Quesudos");

        Sucursal sucursal = new Sucursal("Santa Monica", List.of());
        franquicia.getSucursales().add(sucursal);
        
        assertEquals(1, franquicia.getSucursales().size());
        assertEquals("Santa Monica", franquicia.getSucursales().get(0).getNombre());
    }
}
