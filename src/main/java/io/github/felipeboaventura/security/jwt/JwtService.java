package io.github.felipeboaventura.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import io.github.felipeboaventura.domain.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class JwtService {

    @Value("${security.jwt.expiracao}")
    private String expiracao;

    @Value("${security.jwtchave-assinatura}")
    private String chaveAssinatura;

    public String gerarToken(Usuario user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(chaveAssinatura);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getLogin())
                    .withExpiresAt(expiration())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro na geração de token" + e);
        }
    }

    public String validacao(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(chaveAssinatura);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTCreationException e) {
            throw new RuntimeException("Token inválido ou expirado: " + e.getMessage(), e);
        }
    }

    private Instant expiration() {
        return LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant();
    }
}

