package br.com.fiap.calendario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.calendario.config.JwtUtil;
import br.com.fiap.calendario.model.User;
import br.com.fiap.calendario.service.UserService;

import java.util.Optional;

record AuthRequest(String email, String senha) {}
record AuthResponse(String token) {}

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        try {
            // Verifica se o usuário existe
            User user = userService.findByEmail(req.email())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Autentica credenciais
            var authToken = new UsernamePasswordAuthenticationToken(req.email(), req.senha());
            authManager.authenticate(authToken);

            // Gera o token JWT
            String token = jwtUtil.generateToken(req.email());
            return ResponseEntity.ok(new AuthResponse(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(500).body("Erro de autenticação");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
