package br.com.fiap.calendario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.calendario.model.Calendar;
import br.com.fiap.calendario.repository.CalendarRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/calendar")
@Cacheable("calendars")
public class CalendarController {

    @Autowired
    private CalendarRepository repository;

    @GetMapping
    @Cacheable
    @Operation(summary = "Listar todos os eventos", description = "Retorna a lista de todos os eventos cadastrados no calendário", tags = "Calendar")
    public List<Calendar> index(){
        return repository.findAll();
    }
    
    @PostMapping
    @CacheEvict(value = "calendars", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar um novo evento", responses = @ApiResponse(responseCode = "400", description = "Dados inválidos"))
    public Calendar create(@RequestBody Calendar calendar){
        return repository.save(calendar);
    }

    @GetMapping("{id}")
    public Calendar get(@PathVariable Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento " + id + " não encontrado"));
    }
    
    @DeleteMapping("{id}")
    @CacheEvict(value = "calendars", allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id){
        Calendar event = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento " + id + " não encontrado"));
        repository.delete(event);
    }
    
    @PutMapping("{id}")
    @CacheEvict(value = "calendars", allEntries = true)
    public Calendar update(@PathVariable Long id, @RequestBody Calendar updatedCalendar){
        Calendar event = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento " + id + " não encontrado"));
       
        event.setName(updatedCalendar.getName());
        event.setType(updatedCalendar.getType());
        return repository.save(event);
    }
}