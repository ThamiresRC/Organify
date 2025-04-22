package br.com.fiap.calendario.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.calendario.model.User;
import br.com.fiap.calendario.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@CacheConfig(cacheNames = "users")
@Tag(name = "Usuários", description = "Operações relacionadas ao gerenciamento de usuários")
public class UserController {

    @Autowired
    private UserRepository repository;

    @GetMapping
    @Cacheable
    @Operation(summary = "Listar todos os usuários",
               description = "Retorna a lista de usuários cadastrados",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
               })
    public List<User> index() {
        return repository.findAll();
    }

    @PostMapping
    @CacheEvict(allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar um novo usuário",
               description = "Cria um novo usuário e salva no banco de dados",
               requestBody = @RequestBody(
                   description = "Dados do novo usuário",
                   required = true,
                   content = @Content(schema = @Schema(implementation = User.class))
               ),
               responses = {
                   @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
                   @ApiResponse(responseCode = "400", description = "Dados inválidos")
               })
    public User create(@Valid @org.springframework.web.bind.annotation.RequestBody User user) {
        return repository.save(user);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID",
               description = "Retorna os dados de um usuário pelo seu ID",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
                   @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
               })
    public User show(@PathVariable Long id) {
        Optional<User> found = repository.findById(id);
        return found.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário " + id + " não encontrado"));
    }

    @PutMapping("/{id}")
    @CacheEvict(allEntries = true)
    @Operation(summary = "Atualizar usuário",
               description = "Atualiza os dados de um usuário existente",
               requestBody = @RequestBody(
                   description = "Dados atualizados do usuário",
                   required = true,
                   content = @Content(schema = @Schema(implementation = User.class))
               ),
               responses = {
                   @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
                   @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
               })
    public User update(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody User updatedUser) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário " + id + " não encontrado"));
        user.setNome(updatedUser.getNome());
        user.setEmail(updatedUser.getEmail());
        user.setSenha(updatedUser.getSenha());
        return repository.save(user);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar usuário",
               description = "Remove um usuário do banco de dados pelo ID",
               responses = {
                   @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
                   @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
               })
    public void destroy(@PathVariable Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário " + id + " não encontrado"));
        repository.delete(user);
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar usuários com filtros, paginação e ordenação",
               description = "Retorna uma lista de usuários com base nos filtros e parâmetros de paginação e ordenação.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Lista de usuários filtrados e paginados com sucesso"),
                   @ApiResponse(responseCode = "400", description = "Erro nos parâmetros de busca")
               })
    public Page<User> search(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "nome,asc") String sort) {

        
        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sortParams[0])));

        if (nome != null && email != null) {
            return repository.findByNomeContainingAndEmailContaining(nome, email, pageable);
        } else if (nome != null) {
            return repository.findByNomeContaining(nome, pageable);
        } else if (email != null) {
            return repository.findByEmailContaining(email, pageable);
        } else {
            return repository.findAll(pageable);
        }
    }
}
