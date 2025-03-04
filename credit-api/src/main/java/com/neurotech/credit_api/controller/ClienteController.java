package com.neurotech.credit_api.controller;

import com.neurotech.credit_api.dto.ClienteDTO;
import com.neurotech.credit_api.model.TipoCredito;
import com.neurotech.credit_api.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um cliente", description = "Adiciona um novo cliente ao sistema",
            tags = {"Clientes"}, responses = {
            @ApiResponse(description = "Created", responseCode = "201",
                    content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<?> createClient(@Valid @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO savedClient = service.save(clienteDTO);
        URI location = URI.create("/api/clientes/" + savedClient.getId());
        return ResponseEntity.created(location).body(savedClient);
    }

    @GetMapping("/all")
    @Operation(summary = "Listar todos os clientes", description = "Recupera uma lista de todos os clientes",
            tags = {"Clientes"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ClienteDTO.class)))),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<List<ClienteDTO>> getAllClients() {
        List<ClienteDTO> clientes = service.findAll();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um cliente pelo ID", description = "Recupera os detalhes de um cliente pelo ID",
            tags = {"Clientes"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<ClienteDTO> getClient(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/eligible")
    @Operation(summary = "Listar clientes elegíveis", description = "Recupera uma lista de clientes elegíveis",
            tags = {"Clientes Elegíveis - EXTRA"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ClienteDTO.class)))),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<List<ClienteDTO>> getEligibleClients() {
        List<ClienteDTO> eligibleClients = service.findEligibleClients();
        return ResponseEntity.ok(eligibleClients);
    }


    @GetMapping("/{id}/avaliar-credito")
    @Operation(summary = "Avaliar tipo de crédito disponível para um cliente", description = "Retorna o tipo de crédito que o cliente pode obter.",
            tags = {"Crédito"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200"),
            @ApiResponse(description = "Not Found", responseCode = "404")
    })
    public ResponseEntity<String> avaliarCredito(@PathVariable Long id) {
        try {
            TipoCredito tipoCredito = service.avaliarCredito(id);
            return ResponseEntity.ok("Tipo de crédito disponível: " + tipoCredito);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/avaliar-veiculo")
    @Operation(summary = "Avaliar se um cliente pode financiar um veículo", description = "Verifica se o cliente pode financiar um Hatch ou SUV.",
            tags = {"Crédito"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200"),
            @ApiResponse(description = "Not Found", responseCode = "404")
    })
    public ResponseEntity<String> avaliarVeiculo(@PathVariable Long id) {
        Optional<ClienteDTO> clienteOpt = service.findById(id)
                .map(dto -> new ClienteDTO(dto.getId(), dto.getName(), dto.getAge(), dto.getIncome()));

        if (clienteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ClienteDTO cliente = clienteOpt.get();
        boolean podeHatch = service.podeFinanciarHatch(cliente);
        boolean podeSUV = service.podeFinanciarSUV(cliente);

        if (podeHatch && podeSUV) return ResponseEntity.ok("Cliente pode financiar Hatch e SUV.");
        if (podeHatch) return ResponseEntity.ok("Cliente pode financiar Hatch.");
        if (podeSUV) return ResponseEntity.ok("Cliente pode financiar SUV.");
        return ResponseEntity.ok("Cliente não é elegível para financiamento de veículos.");
    }

}
