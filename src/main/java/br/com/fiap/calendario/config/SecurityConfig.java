package br.com.fiap.calendario.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.method.P;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.fiap.calendario.service.AuthUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    // 1) Expõe o AuthenticationManager para injeção no AuthController
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 2) PasswordEncoder para criptografar/validar senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 3) Configuração CORS global
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));        // ou especifique apenas seus domínios
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      // 1) Desativa CSRF (necessário para POST sem token de formulário)
      .csrf().disable()

      // 2) CORS
      .cors().and()

      // 3) Sessão stateless
      .sessionManagement()
         .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
         .and()

      // 4) Desativa formLogin e httpBasic
      .formLogin().disable()
      .httpBasic().disable()

      // 5) Configura autorização
      .authorizeHttpRequests()
         // LIBERAÇÃO EXPLÍCITA DO LOGIN
         .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

         // Opcional: libera qualquer outro endpoint de /auth (refresh, por exemplo)
         .requestMatchers("/auth/**").permitAll()

         // Libera Swagger e console H2
         .requestMatchers(
             HttpMethod.GET,
             "/v3/api-docs/**",
             "/swagger-ui.html",
             "/swagger-ui/**",
             "/webjars/**",
             "/h2-console/**"
         ).permitAll()

         // Qualquer outra precisa de token
         .anyRequest().authenticated()
         .and()

      // 6) Desabilita frameOptions (para H2 console)
      .headers().frameOptions().disable()
      .and()

      // 7) Adiciona seu filtro JWT
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
    @Bean
    
public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder encoder, AuthUserDetailsService userDetailsService) 
        throws Exception {
    return http
        .getSharedObject(AuthenticationManagerBuilder.class)
        .userDetailsService(userDetailsService)
        .passwordEncoder(encoder)
        .and()
        .build();
}

}