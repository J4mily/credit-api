package com.neurotech.credit_api.converter;

import com.neurotech.credit_api.dto.ClienteDTO;
import com.neurotech.credit_api.model.Cliente;
import org.springframework.beans.BeanUtils;

public class ConverterDto {
    // Conversão DTO -> Model
    public static Cliente toModel(ClienteDTO dto) {
        Cliente model = new Cliente();
        BeanUtils.copyProperties(dto, model);
        return model;
    }

    // Conversão Model -> DTO
    public static ClienteDTO toDto(Cliente model) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setAge(model.getAge());
        dto.setIncome(model.getIncome());
        return dto;
    }
}
