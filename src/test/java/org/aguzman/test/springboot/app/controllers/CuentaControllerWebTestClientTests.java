package org.aguzman.test.springboot.app.controllers;


import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.aguzman.test.springboot.app.models.TransaccionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;


@SpringBootTest(webEnvironment = RANDOM_PORT)//to test in any port no used
class CuentaControllerWebTestClientTests {
    private ObjectMapper objectMapper;
    @Autowired
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testTransfer() throws JsonProcessingException {
        //given
        TransaccionDto dto = new TransaccionDto();
        dto.setCuentaOrigenId(1L);
        dto.setCuentaDestinoId(2L);
        dto.setBancoId(1L);
        dto.setMonto(new BigDecimal("100"));
        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("mensaje", "Transferencia realizada con éxito!");
        response.put("transaccion", dto);

        //when
        client.post().uri("/api/cuentas/transferir")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.mensaje").isNotEmpty()
            .jsonPath("$.mensaje").value(is("Transferencia realizada con éxito!"))
            .jsonPath("$.mensaje")
            .value(valor -> assertEquals("Transferencia realizada con éxito!", valor))
            .jsonPath("$.mensaje").isEqualTo("Transferencia realizada con éxito!")
            .jsonPath("$.transaccion.cuentaOrigenId").isEqualTo(dto.getCuentaOrigenId())
            .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
            .json(objectMapper.writeValueAsString(response))
        ;
    }
}
