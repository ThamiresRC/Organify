package br.com.fiap.calendario.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.fiap.calendario.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import br.com.fiap.calendario.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Page<User> findByNomeContaining(String nome, Pageable pageable);
    
    Page<User> findByEmailContaining(String email, Pageable pageable);
    
    Page<User> findByNomeContainingAndEmailContaining(String nome, String email, Pageable pageable);
    
    Optional<User> findByEmail(String email);

}
