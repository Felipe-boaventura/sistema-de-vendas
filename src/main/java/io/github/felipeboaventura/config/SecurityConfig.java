package io.github.felipeboaventura.config;

import io.github.felipeboaventura.security.jwt.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Autowired
//    private UsuarioServiceImpl usuarioService;
//
//    @Autowired
//    private PasswordEncoder encoder;
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeConfig -> {
                            authorizeConfig.requestMatchers(HttpMethod.POST, "/api/usuario/**").permitAll();
                            authorizeConfig.requestMatchers("/api/clientes/**").hasAnyRole("USER", "ADMIN");
                            authorizeConfig.requestMatchers("/api/pedidos/**").hasAnyRole("USER", "ADMIN");
                            authorizeConfig.requestMatchers("/api/produtos/**").hasRole("ADMIN");
                            authorizeConfig.anyRequest().authenticated();
                        }

                ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}


