package io.github.felipeboaventura.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import io.github.felipeboaventura.VendasApplication;
import io.github.felipeboaventura.domain.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.expiracao}")
    private String expiracao;

    @Value("${security.jwtchave-assinatura}")
    private String chaveAssinatura;

    public String gerarToken(Usuario user) {
        Algorithm algorithm = Algorithm.HMAC256(chaveAssinatura);
        long expString = Long.valueOf(expiracao);
        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expString);
        Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);


           String token = JWT
                   .create().withIssuer("auth-api")
                   .withSubject(user.getLogin())
                   .withExpiresAt(date)
                   .sign(algorithm);
           return token;
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
            throw new RuntimeException("Erro", e);
        }
    }

//    public static void main(String[] args) {
//        ConfigurableApplicationContext contexto = SpringApplication.run(VendasApplication.class);
//        JwtService service = contexto.getBean(JwtService.class);
//        Usuario usuario = Usuario.builder().login("Felipe").build();
//        String token = service.gerarToken(usuario);
//        System.out.println("token AAAAAAAAAAAAAAAQQQQQQQQQQQQQQQUUUUUUUUUUUUUUUUIIIIIIIIIIII" + token);
//    }
}

