package br.com.fiap.calendario.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.fiap.calendario.model.User;
import br.com.fiap.calendario.repository.UserRepository;
import br.com.fiap.calendario.specification.UserSpecification;


@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public Page<User> findAll(String nome, String email, Pageable pageable) {
        Specification<User> spec = Specification.where(UserSpecification.nomeContains(nome))
                                               .and(UserSpecification.emailContains(email));
        return repository.findAll(spec, pageable);
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User save(User user) {
        return repository.save(user);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public User update(Long id, User updatedUser) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setNome(updatedUser.getNome());
        user.setEmail(updatedUser.getEmail());
        user.setSenha(updatedUser.getSenha());

        return repository.save(user);
    }


    public Page<User> search(String nome, String email, int page, int size, String[] sort) {
        Specification<User> spec = Specification.where(
            UserSpecification.nomeContains(nome)
            .and(UserSpecification.emailContains(email))
        );

        Sort sorting = Sort.by(Sort.Order.by(sort[0]));
        if (sort.length > 1 && sort[1].equalsIgnoreCase("desc")) {
            sorting = sorting.descending();
        }

        Pageable pageable = PageRequest.of(page, size, sorting);
        return repository.findAll(spec, pageable);
    }
}
