package io.github.felipeboaventura.REST.dto;

import io.github.felipeboaventura.domain.enun.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private String login;
    private String senha;
    private Roles roles;
}
