package io.github.felipeboaventura.REST.controller;

import io.github.felipeboaventura.REST.dto.AuthDTO;
import io.github.felipeboaventura.REST.dto.LoginResponseDTO;
import io.github.felipeboaventura.REST.dto.UsuarioDTO;
import io.github.felipeboaventura.domain.entity.Usuario;
import io.github.felipeboaventura.domain.repository.UsuarioRepository;
import io.github.felipeboaventura.security.jwt.JwtService;
import io.github.felipeboaventura.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity salvar(@RequestBody @Valid UsuarioDTO usuario) {

        if (this.repository.findByLogin(usuario.getLogin()) != null) return ResponseEntity.badRequest().build();

        String crip = new BCryptPasswordEncoder().encode(usuario.getSenha());
        Usuario newUser = new Usuario(usuario.getLogin(), crip, usuario.getRoles());

        this.repository.save(newUser);

        return emailService()
        //return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO authDTO) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.getLogin(), authDTO.getSenha());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = jwtService.gerarToken((Usuario) auth.getPrincipal());
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor: " + e.getMessage());
        }
    }
}
