package com.raizesdonordeste.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.raizesdonordeste.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import com.raizesdonordeste.api.exception.ApiError;
import org.springframework.http.MediaType;
import tools.jackson.databind.json.JsonMapper;
import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider(userDetailsService);

        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JsonMapper jsonMapper) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint(
                                (request, response, authException) -> {

                                    ApiError erro = new ApiError();

                                    erro.setTimestamp(LocalDateTime.now());
                                    erro.setStatus(401);
                                    erro.setErro("NAO_AUTENTICADO");
                                    erro.setMensagem(
                                            "Autenticação necessária para acessar este recurso."
                                    );
                                    erro.setPath(request.getRequestURI());

                                    response.setStatus(401);
                                    response.setContentType(
                                            MediaType.APPLICATION_JSON_VALUE
                                    );

                                    jsonMapper.writeValue(
                                            response.getWriter(),
                                            erro
                                    );
                                }
                        )

                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {

                                    ApiError erro = new ApiError();

                                    erro.setTimestamp(LocalDateTime.now());
                                    erro.setStatus(403);
                                    erro.setErro("ACESSO_NEGADO");
                                    erro.setMensagem(
                                            "Você não possui permissão para acessar este recurso."
                                    );
                                    erro.setPath(request.getRequestURI());

                                    response.setStatus(403);
                                    response.setContentType(
                                            MediaType.APPLICATION_JSON_VALUE
                                    );

                                    jsonMapper.writeValue(
                                            response.getWriter(),
                                            erro
                                    );
                                }
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
