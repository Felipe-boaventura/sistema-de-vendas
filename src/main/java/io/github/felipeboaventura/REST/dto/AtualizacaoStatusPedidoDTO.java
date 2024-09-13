package io.github.felipeboaventura.REST.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizacaoStatusPedidoDTO {
    @NotEmpty(message = "Status invalido.")
    private String novoStatus;
}
