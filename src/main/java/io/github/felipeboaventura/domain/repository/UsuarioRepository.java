package io.github.felipeboaventura.domain.repository;

import io.github.felipeboaventura.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository  extends JpaRepository<Usuario, Integer> {
    UserDetails findByLogin(String login);
}
