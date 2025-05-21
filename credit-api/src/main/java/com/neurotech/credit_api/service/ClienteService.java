package com.neurotech.credit_api.service;

import com.neurotech.credit_api.converter.ConverterDto;
import com.neurotech.credit_api.dto.ClienteDTO;
import com.neurotech.credit_api.model.Cliente;
import com.neurotech.credit_api.model.TipoCredito;
import com.neurotech.credit_api.repository.ClienteRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public ClienteDTO save(ClienteDTO dto) {
        try {

            Cliente clienteModel = ConverterDto.toModel(dto);
            Cliente clienteSalvo = repository.save(clienteModel);

            return ConverterDto.toDto(clienteSalvo);
        } catch (DataAccessException e) {
            throw new RuntimeException("Erro ao salvar o cliente: " + dto.getName(), e);
        }
    }

    public Optional<ClienteDTO> findById(Long id) {
        return repository.findById(id).map(ConverterDto::toDto);
    }

    public List<ClienteDTO> findAll() {
        return repository.findAll().stream()
                .map(ConverterDto::toDto)
                .collect(Collectors.toList());
    }

    public List<ClienteDTO> findEligibleClients() {
        return repository.findByAgeBetweenAndIncomeBetween(23, 49, 5000.0, 15000.0).stream()
                .map(ConverterDto::toDto)
                .collect(Collectors.toList());
    }

    public TipoCredito avaliarCredito(Long clienteId) {
        Optional<Cliente> clienteOpt = repository.findById(clienteId);

        if (clienteOpt.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado!");
        }

        Cliente cliente = clienteOpt.get();

        if (cliente.getAge() >= 18 && cliente.getAge() <= 25) {
            return TipoCredito.JUROS_FIXOS;
        } else if (cliente.getAge() >= 21 && cliente.getAge() <= 65
                && cliente.getIncome() >= 5000.00 && cliente.getIncome() <= 15000.00) {
            return TipoCredito.JUROS_VARIAVEIS;
        } else if (cliente.getAge() > 65) {
            return TipoCredito.CONSIGNADO;
        }

        return TipoCredito.NAO_ELEGIVEL;
    }
    public ClienteDTO update(Long id, ClienteDTO dto) {
        Optional<Cliente> clienteOpt = repository.findById(id);
        if (clienteOpt.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado!");
        }

        Cliente clienteExistente = clienteOpt.get();
        clienteExistente.setName(dto.getName());
        clienteExistente.setAge(dto.getAge());
        clienteExistente.setIncome(dto.getIncome());

        Cliente clienteAtualizado = repository.save(clienteExistente);
        return ConverterDto.toDto(clienteAtualizado);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Cliente não encontrado!");
        }
        repository.deleteById(id);
    }

    public boolean podeFinanciarHatch(ClienteDTO cliente) {
        return cliente.getIncome() >= 5000.00 && cliente.getIncome() <= 15000.00;
    }

    public boolean podeFinanciarSUV(ClienteDTO cliente) {
        return cliente.getIncome() > 8000.00 && cliente.getAge() > 20;
    }
}
