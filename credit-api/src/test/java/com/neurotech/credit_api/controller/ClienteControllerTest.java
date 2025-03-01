package com.neurotech.credit_api.controller;

import com.neurotech.credit_api.dto.ClienteDTO;
import com.neurotech.credit_api.model.TipoCredito;
import com.neurotech.credit_api.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class ClienteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClienteService service;

    @InjectMocks
    private ClienteController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testCreateClient() throws Exception {
        ClienteDTO clienteDTO = new ClienteDTO(1L, "João Silva", 30, 8000.0);
        when(service.save(any(ClienteDTO.class))).thenReturn(clienteDTO);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"João Silva\",\"age\":30,\"income\":8000}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("João Silva"));
    }

    @Test
    void testGetAllClients() throws Exception {
        List<ClienteDTO> clientes = Arrays.asList(
                new ClienteDTO(1L, "João Silva", 30, 8000.0),
                new ClienteDTO(2L, "Maria Souza", 40, 12000.0)
        );
        when(service.findAll()).thenReturn(clientes);

        mockMvc.perform(get("/api/clientes/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("João Silva"))
                .andExpect(jsonPath("$[1].name").value("Maria Souza"));
    }

    @Test
    void testGetClientById() throws Exception {
        ClienteDTO clienteDTO = new ClienteDTO(1L, "João Silva", 30, 8000.0);
        when(service.findById(1L)).thenReturn(Optional.of(clienteDTO));

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("João Silva"));
    }

    @Test
    void testGetClientById_NotFound() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetEligibleClients() throws Exception {
        List<ClienteDTO> eligibleClients = List.of(new ClienteDTO(1L, "Carlos Lima", 28, 10000.0));
        when(service.findEligibleClients()).thenReturn(eligibleClients);

        mockMvc.perform(get("/api/clientes/eligible"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Carlos Lima"));
    }

    @Test
    void testAvaliarCredito() throws Exception {
        when(service.avaliarCredito(1L)).thenReturn(TipoCredito.JUROS_FIXOS);

        mockMvc.perform(get("/api/clientes/1/avaliar-credito"))
                .andExpect(status().isOk())
                .andExpect(content().string("Tipo de crédito disponível: JUROS_FIXOS"));
    }

    @Test
    void testAvaliarCredito_NotFound() throws Exception {
        when(service.avaliarCredito(1L)).thenThrow(new IllegalArgumentException("Cliente não encontrado"));

        mockMvc.perform(get("/api/clientes/1/avaliar-credito"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAvaliarVeiculo() throws Exception {
        ClienteDTO cliente = new ClienteDTO(1L, "João Silva", 30, 8000.0);
        when(service.findById(1L)).thenReturn(Optional.of(cliente));
        when(service.podeFinanciarHatch(cliente)).thenReturn(true);
        when(service.podeFinanciarSUV(cliente)).thenReturn(false);

        mockMvc.perform(get("/api/clientes/1/avaliar-veiculo"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cliente pode financiar Hatch."));
    }

    @Test
    void testAvaliarVeiculo_NotFound() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clientes/1/avaliar-veiculo"))
                .andExpect(status().isNotFound());
    }
}
