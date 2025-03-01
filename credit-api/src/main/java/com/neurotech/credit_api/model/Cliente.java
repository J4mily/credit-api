package com.neurotech.credit_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    @NotNull
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "O nome do cliente não pode ter mais que 100 caracteres")
    private String name;

    @Column(name = "age")
    @NotNull(message = "Idade é obrigatória")
    @Min(value = 18, message = "Idade mínima é 18 anos")
    private Integer age;

    @Column(name = "income")
    @NotNull(message = "Renda é obrigatória")
    @Min(value = 0, message = "Renda não pode ser negativa")
    private Double income;
}