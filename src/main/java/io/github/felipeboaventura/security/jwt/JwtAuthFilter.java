package io.github.felipeboaventura.security.jwt;

import io.github.felipeboaventura.domain.entity.Usuario;
import io.github.felipeboaventura.domain.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UsuarioRepository usuarioService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        var token = this.recoverToken(request);
        if (token != null) {
            var login = jwtService.validacao(token);
            Usuario user = usuarioService.findByLogin(login);

            if (token != null) {
                throw new RuntimeException("Unathorized");
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;

        if (!authHeader.split(" ")[0].equals("Bearer")) return null;

        return authHeader.split(" ")[1];
    }
}
