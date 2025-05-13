package br.com.fiap.calendario.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationMillis;

    // Gera um token JWT
    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);
        return Jwts.builder()
                .setSubject(username)  // Define o nome do usuário no token
                .setIssuedAt(now)      // Define a data de emissão
                .setExpiration(expiry) // Define a data de expiração
                .signWith(SignatureAlgorithm.HS256, secret)  // Assina o token com o segredo
                .compact();
    }

    // Extrai o nome de usuário do token
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Verifica se o token é válido (não expirado)
    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;  // Se o token for inválido ou não puder ser analisado
        }
    }

    // Obtém os Claims do token
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)  // Chave secreta para assinar o token
                .parseClaimsJws(token)  // Analisa o JWT
                .getBody();
    }

    // Verifica se o token foi assinado corretamente e não foi modificado
    public boolean isSignatureValid(String token) {
        try {
            Jwts.parser()
                .setSigningKey(secret)  // Chave secreta para validação da assinatura
                .parseClaimsJws(token);  // Tenta analisar o JWT
            return true;  // Se o token for válido
        } catch (Exception e) {
            return false;  // Se ocorrer algum erro na assinatura
        }
    }
}
