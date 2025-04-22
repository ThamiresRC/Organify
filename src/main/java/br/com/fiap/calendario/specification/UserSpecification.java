package br.com.fiap.calendario.specification;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.calendario.model.User;

public class UserSpecification {

    public static Specification<User> nomeContains(String nome) {
        return (root, query, cb) -> nome == null ? null : cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<User> emailContains(String email) {
        return (root, query, cb) -> email == null ? null : cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }
}
