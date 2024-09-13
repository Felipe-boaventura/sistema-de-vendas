package io.github.felipeboaventura.REST.controller;

import io.github.felipeboaventura.REST.dto.UsuarioDTO;
import io.github.felipeboaventura.domain.entity.Usuario;
import io.github.felipeboaventura.domain.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    @Autowired
    private UsuarioRepository repository;

    private final PasswordEncoder passwordEncoder;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity salvar(@RequestBody @Valid UsuarioDTO usuario) {

        if (this.repository.findByLogin(usuario.getLogin()) != null) return ResponseEntity.badRequest().build();


            String crip = new BCryptPasswordEncoder().encode(usuario.getSenha());
            this.repository.save(new Usuario(usuario.getLogin(), crip, usuario.getRoles()));
        return ResponseEntity.ok().build();
    }
}
