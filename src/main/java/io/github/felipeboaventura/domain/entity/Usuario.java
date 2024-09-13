package io.github.felipeboaventura.domain.entity;

import io.github.felipeboaventura.domain.enun.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    @NotNull(message = "{campo.login.obrigatorio}")
    private String login;

    @Column
    @NotNull(message = "{campo.senha.obrigatorio}")
    private String senha;

    @Column
    private Roles roles;

    public Usuario(String login, String senha, Roles roles) {
        this.login = login;
        this.senha = senha;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.roles == Roles.ADMIN ) {
            return List.of(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("USER"));
        } else return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
